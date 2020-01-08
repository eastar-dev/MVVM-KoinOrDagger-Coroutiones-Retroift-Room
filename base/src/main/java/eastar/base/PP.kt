@file:Suppress("unused", "SpellCheckingInspection", "MemberVisibilityCanBePrivate", "FunctionName", "UNUSED_PARAMETER")

package eastar.base

import android.content.Context
import android.content.SharedPreferences
import android.log.Log
import androidx.core.content.edit
import androidx.preference.PreferenceManager
import java.util.*

/**
 * <pre>
 * 아래와 같이 사용하세요
 * 변수 타입을 혼용하여 사용하면 죽음!
 * sample.isit();
 * sample.set(true);
 *
 * sample.getInt();
 * sample.set(1);
 *
 * sample.getLong();
 * sample.set(1L);
 *
 * sample.get();
 * sample.getString();
 * sample.set(&quot;text&quot;);
 *
</pre> *
 */
@Suppress("EnumEntryName")
enum class PP {
    FIRST, LAST_BRANCH_SYNC;

    companion object {
        lateinit var PREFERENCES: SharedPreferences
        fun CREATE(context: Context) {
            PREFERENCES = PreferenceManager.getDefaultSharedPreferences(context).apply {
                registerOnSharedPreferenceChangeListener { _, key ->
                    try {
                        Log.w(key, PREFERENCES.all[key])
                    } catch (e: Exception) {
                    }
                }
            }
        }

        const val DEFAULT_STRING = ""
        const val DEFAULT_FLOAT = -1f
        const val DEFAULT_INT = -1
        const val DEFAULT_LONG = -1L
        const val DEFAULT_BOOLEAN = false
    }

    //실재값에 변화가 있을때만 event가 날라온다
    fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        PREFERENCES.registerOnSharedPreferenceChangeListener(listener)
    }

    fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
        PREFERENCES.unregisterOnSharedPreferenceChangeListener(listener)
    }

    @JvmOverloads
    fun getBoolean(DEFAULT: Boolean = DEFAULT_BOOLEAN) = PREFERENCES.getBoolean(name, DEFAULT)

    @JvmOverloads
    fun getInt(DEFAULT: Int = DEFAULT_INT) = PREFERENCES.getInt(name, DEFAULT)

    @JvmOverloads
    fun getLong(DEFAULT: Long = DEFAULT_LONG) = PREFERENCES.getLong(name, DEFAULT)

    @JvmOverloads
    fun getFloat(DEFAULT: Float = DEFAULT_FLOAT) = PREFERENCES.getFloat(name, DEFAULT)

    @JvmOverloads
    fun getString(DEFAULT: String? = DEFAULT_STRING): String? = PREFERENCES.getString(name, DEFAULT)

    @JvmOverloads
    fun getStringSet(DEFAULT: Set<String>? = null): Set<String>? = PREFERENCES.getStringSet(name, DEFAULT)

    fun set(v: Boolean) = PREFERENCES.edit { putBoolean(name, v) }

    fun set(v: Int) = PREFERENCES.edit { putInt(name, v) }
    fun set(v: Long) = PREFERENCES.edit { putLong(name, v) }
    fun set(v: Float) = PREFERENCES.edit { putFloat(name, v) }
    fun set(v: String?) = PREFERENCES.edit { putString(name, v) }
    fun set(v: Set<String>?) = PREFERENCES.edit { putStringSet(name, v) }

    fun `is`(DEFAULT: Boolean = DEFAULT_BOOLEAN) = getBoolean(DEFAULT)
    fun get(DEFAULT: String? = DEFAULT_STRING): String? = getString(DEFAULT)
    fun toggle() = set(!getBoolean())

    fun contain() = PREFERENCES.contains(name)
    fun remove() = PREFERENCES.edit().remove(name).commit()
    fun clear() = PREFERENCES.edit().clear().commit()
    fun contain(key: String) = PREFERENCES.contains(key)
    fun remove(key: String) = PREFERENCES.edit().remove(key).commit()

    //////////////////////////////////////////////////////////////////////////////
    fun first() = getBoolean().also { set(true) }

    fun once() = first()
    fun newer(comp: Long) = (getLong(0) < comp).also { if (it) set(comp) }
    fun uuid() = getString(null) ?: UUID.randomUUID().toString().also { set(it) }
}
