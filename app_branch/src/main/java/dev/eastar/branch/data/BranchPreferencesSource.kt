package dev.eastar.branch.data

import android.content.SharedPreferences
import android.graphics.RectF
import android.preference.PreferenceManager
import androidx.core.content.edit
import kotlinx.coroutines.Deferred
import org.koin.dsl.module
import org.koin.android.ext.android.inject
import org.koin.java.KoinJavaComponent.inject

import java.util.*

//interface PreferencesService {
//    fun getBranchsRange(): Deferred<Array<RectF>>
//}

class Preferences {
//    FIRST, LAST_BRANCH_SYNC;

//    val pref : SharedPreferences by inject()

    companion object {

        const val DEFAULT_STRING = ""
        const val DEFAULT_FLOAT = -1f
        const val DEFAULT_INT = -1
        const val DEFAULT_LONG = -1L
        const val DEFAULT_BOOLEAN = false

        //실재값에 변화가 있을때만 event가 날라온다
//        fun registerOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
//            PREFERENCES.registerOnSharedPreferenceChangeListener(listener)
//        }
//
//        fun unregisterOnSharedPreferenceChangeListener(listener: SharedPreferences.OnSharedPreferenceChangeListener) {
//            PREFERENCES.unregisterOnSharedPreferenceChangeListener(listener)
//        }
    }
//
//    @JvmOverloads fun getBoolean(DEFAULT: Boolean = DEFAULT_BOOLEAN) = PREFERENCES.getBoolean(name, DEFAULT)
//    @JvmOverloads fun getInt(DEFAULT: Int = DEFAULT_INT) = PREFERENCES.getInt(name, DEFAULT)
//    @JvmOverloads fun getLong(DEFAULT: Long = DEFAULT_LONG) = PREFERENCES.getLong(name, DEFAULT)
//    @JvmOverloads fun getFloat(DEFAULT: Float = DEFAULT_FLOAT) = PREFERENCES.getFloat(name, DEFAULT)
//    @JvmOverloads fun getString(DEFAULT: String? = DEFAULT_STRING): String? = PREFERENCES.getString(name, DEFAULT)
//    @JvmOverloads fun getStringSet(DEFAULT: Set<String>? = null): Set<String>? = PREFERENCES.getStringSet(name, DEFAULT)
//
//    fun set(v: Boolean) = PREFERENCES.edit { putBoolean(name, v) }
//    fun set(v: Int) = PREFERENCES.edit { putInt(name, v) }
//    fun set(v: Long) = PREFERENCES.edit { putLong(name, v) }
//    fun set(v: Float) = PREFERENCES.edit { putFloat(name, v) }
//    fun set(v: String?) = PREFERENCES.edit { putString(name, v) }
//    fun set(v: Set<String>?) = PREFERENCES.edit { putStringSet(name, v) }
//
//    fun contain() = PREFERENCES.contains(name)
//    fun remove() = PREFERENCES.edit().remove(name).commit()
//    fun clear() = PREFERENCES.edit().clear().commit()
//    fun contain(key: String) = PREFERENCES.contains(key)
//    fun remove(key: String) = PREFERENCES.edit().remove(key).commit()
//
//    //////////////////////////////////////////////////////////////////////////////
//    fun `is`(DEFAULT: Boolean = DEFAULT_BOOLEAN) = getBoolean(DEFAULT)
//    fun get(DEFAULT: String? = DEFAULT_STRING): String? = getString(DEFAULT)
//    fun toggle() = set(!getBoolean())
//
//    fun first() = getBoolean().also { set(true) }
//
//    fun once() = first()
//    fun newer(comp: Long) = (getLong(0) < comp).also { if (it) set(comp) }
//    fun uuid() = getString(null) ?: UUID.randomUUID().toString().also { set(it) }
}


val preferencesModule = module {
    single<SharedPreferences> { PreferenceManager.getDefaultSharedPreferences(get()) }
}
