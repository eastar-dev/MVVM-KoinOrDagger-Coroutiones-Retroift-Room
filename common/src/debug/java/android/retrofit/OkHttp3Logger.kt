package android.retrofit

import android.log.Log
import okhttp3.Headers
import okhttp3.Interceptor
import okhttp3.Response
import okio.Buffer
import okio.GzipSource
import java.io.EOFException
import java.nio.charset.Charset
import java.util.concurrent.TimeUnit

@Suppress("LocalVariableName")
class OkHttp3Logger : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        if (!LOG) return chain.proceed(request)

        val _out = StringBuilder()
        if (_OUT_1)
            _out.append("--> " + request.method + ":" + request.url)

        val _out_c = StringBuilder()
        if (_OUT_C) {
            val headers = request.headers
            for (i in 0 until headers.size)
                if (headers.name(i) == COOKIE)
                    _out_c.LF.append(headers.name(i) + ": " + headers.value(i))
            _out.LF.append(_out_c)
        }

        val _out_h = StringBuilder()
        if (_OUT_H) {
            val headers = request.headers
            for (i in 0 until headers.size)
                if (headers.name(i) != COOKIE)
                    _out_h.LF.append(headers.name(i) + ": " + headers.value(i))
            _out.LF.append(_out_h)
        }

        val body = request.body
        if (_OUT_2 && body != null && !bodyHasUnknownEncoding(request.headers)) {
            val buffer = Buffer()
            body.writeTo(buffer)
            val contentType = body.contentType()
            var charset: Charset? = UTF8
            if (contentType != null)
                charset = contentType.charset(UTF8)


            if (isPlaintext(buffer)) {
                _out.LF.append(buffer.readString(charset!!))
                _out.LF.append("--> END " + request.method + " (" + body.contentLength())
            } else {
                _out.LF.append("--> END " + request.method + " (binary " + body.contentLength())
            }
        }
        Log.e(_out)

        val startNs = System.nanoTime()
        val response: Response
        try {
            response = chain.proceed(request)
        } catch (e: Exception) {
            Log.w("<-- HTTP FAILED: $e")
            throw e
        }

        val tookMs = TimeUnit.NANOSECONDS.toMillis(System.nanoTime() - startNs)

        val responseBody = response.body
        val contentLength = responseBody!!.contentLength()

        val _in = StringBuilder()
        if (_IN_1)
            _in.append("<-- ${response.code} ${response.message} ${response.request.url} (${tookMs}ms)")

        val _in_c = StringBuilder()
        if (_IN_C) {
            val headers = request.headers
            for (i in 0 until headers.size)
                if (headers.name(i) == SET_COOKIE)
                    _in_c.LF.append(headers.name(i) + ": " + headers.value(i))
            _in.LF.append(_in_c)
        }

        val _in_h = StringBuilder()
        if (_IN_H) {
            val headers = response.headers
            for (i in 0 until headers.size)
                if (headers.name(i) != SET_COOKIE)
                    _in_h.LF.append(headers.name(i) + ": " + headers.value(i))
            _in.LF.append(_in_h)
        }


        if (_IN_2 && response.body != null && !bodyHasUnknownEncoding(response.headers)) {
            val source = responseBody.source()
            source.request(java.lang.Long.MAX_VALUE) // Buffer the entire body.
            var buffer = source.buffer

            var gzippedLength: Long? = null
            val headers = response.headers
            if ("gzip".equals(headers["Content-Encoding"], ignoreCase = true)) {
                gzippedLength = buffer.size
                var gzippedResponseBody: GzipSource? = null
                try {
                    gzippedResponseBody = GzipSource(buffer.clone())
                    buffer = Buffer()
                    buffer.writeAll(gzippedResponseBody)
                } finally {
                    gzippedResponseBody?.close()
                }
            }

            var charset: Charset? = UTF8
            val contentType = responseBody.contentType()
            if (contentType != null)
                charset = contentType.charset(UTF8)


            if (!isPlaintext(buffer))
                _in.LF.append("<-- END HTTP (binary " + buffer.size + "-byte body omitted)")

            if (contentLength != 0L)
                _in.LF.append(buffer.clone().readString(charset!!))

            if (gzippedLength != null)
                _in.LF.append("<-- END HTTP (" + buffer.size + "-byte, " + gzippedLength + "-gzipped-byte body)")
            else
                _in.LF.append("<-- END HTTP (" + buffer.size + "-byte body)")
        }

        if (_IN_LIMIT > 0 && _in.length > _IN_LIMIT)
            _in.setLength(_IN_LIMIT)

        Log.p(if (response.isSuccessful) Log.INFO else Log.WARN, _in)

        return response
    }

    private val StringBuilder.LF: StringBuilder
        get() = if (isNotEmpty())
            append("\n")
        else
            this

    private fun bodyHasUnknownEncoding(headers: Headers): Boolean {
        val contentEncoding = headers.get("Content-Encoding")
        return (contentEncoding != null
            && !contentEncoding.equals("identity", ignoreCase = true)
            && !contentEncoding.equals("gzip", ignoreCase = true))
    }

    @Suppress("unused", "ObjectPropertyName", "ObjectPropertyName", "MayBeConstant")
    companion object {
        var LOG = false
        var _OUT_1 = false
        var _OUT_2 = false
        var _OUT_3 = false
        var _OUT_H = false
        var _OUT_C = false
        var _IN_1 = false
        var _IN_2 = false
        var _IN_3 = false
        var _IN_H = false
        var _IN_C = false
        var _IN_LIMIT = 0

        val COOKIE = "Cookie"
        val SET_COOKIE = "Set-Cookie"

        private val UTF8 = Charset.forName("UTF-8")

        /**
         * Returns true if the body in question probably contains human readable text. Uses a small sample
         * of code points to detect unicode control characters commonly used in binary file signatures.
         */
        internal fun isPlaintext(buffer: Buffer): Boolean {
            try {
                val prefix = Buffer()
                val byteCount = if (buffer.size < 64) buffer.size else 64
                buffer.copyTo(prefix, 0, byteCount)
                for (i in 0..15) {
                    if (prefix.exhausted()) {
                        break
                    }
                    val codePoint = prefix.readUtf8CodePoint()
                    if (Character.isISOControl(codePoint) && !Character.isWhitespace(codePoint)) {
                        return false
                    }
                }
                return true
            } catch (e: EOFException) {
                return false // Truncated UTF-8 sequence.
            }
        }

    }
}
