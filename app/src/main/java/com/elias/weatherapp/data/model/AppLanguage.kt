package com.elias.weatherapp.data.model

import com.elias.weatherapp.R

enum class AppLanguage(val code: String, val labelResId: Int) {
    ENGLISH("en", R.string.lang_english),
    GERMAN("de", R.string.lang_german);

    companion object {
        fun fromCode(code: String?): AppLanguage {
            return entries.find { it.code == code } ?: ENGLISH
        }
    }
}
