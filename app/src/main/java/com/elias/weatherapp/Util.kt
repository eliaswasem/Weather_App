package com.elias.weatherapp

import androidx.appcompat.app.AppCompatDelegate
import java.util.Locale

fun getLanguageCode(): String {
    return AppCompatDelegate.getApplicationLocales().get(0)?.language
        ?: Locale.getDefault().language
}

