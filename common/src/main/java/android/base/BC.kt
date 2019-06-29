package android.base

import android.content.res.Resources
import android.util.DisplayMetrics

object BC {
    val densityDpi = Resources.getSystem().displayMetrics.densityDpi
    val density = Resources.getSystem().displayMetrics.density
    val widthPixels = Resources.getSystem().displayMetrics.widthPixels
    val heightPixels = Resources.getSystem().displayMetrics.heightPixels
    val scaledDensity = Resources.getSystem().displayMetrics.scaledDensity
    val displayMetrics: DisplayMetrics = Resources.getSystem().displayMetrics

    const val LENGTH_resRegNo = 13
    /** 메일주소 인것  */
    const val regularExpressionEmail = "^([\\w-\\.]+)@((?:[\\w]+\\.)+[a-zA-Z]{2,4})$"
    /** 전화번호 인것  */
    const val regularExpressionPhoneNo = "^(0(?:505|70|10|11|16|17|18|19))(\\d{3}|\\d{4})(\\d{4})$"
    /** 파일네임에 적합하지 않은것  */
    const val regularExpressionVFilename = "\\\\/|[&{}?=/\\\\: <>*|\\]\\[\\\"\\']"
    /** 자연수가 아닌것  */
    const val regularExpressionNotdecimal = "\\D"
    /** Password로 사용가능한 문자열  */
    const val regularExpressionPassword = "(((?=.*[a-z])|(?=.*[A-Z])).{8,20})"
    /** 엔터가 포함되어 있지 않은 ""로 둘러 쌓여있는 문자열  */
    const val regularExpressionWrodNotCR = "(\\\"[^\\\"\\r]+\\\")"

    const val KSC5601 = "KSC5601"
    const val UTF8 = "UTF-8"

    const val CRLF = "\r\n"
}
