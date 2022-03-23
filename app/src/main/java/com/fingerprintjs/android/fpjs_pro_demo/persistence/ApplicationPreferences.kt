package com.fingerprintjs.android.fpjs_pro_demo.persistence


import android.content.Context
import android.os.Build
import android.preference.PreferenceManager
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.fingerprintjs.android.fpjs_pro_demo.R


interface ApplicationPreferences {
    fun getEndpointUrl(): String
    fun getPublicApiKey(): String

    fun setEndpointUrl(endpointUrl: String)
    fun setPublicApiKey(apiToken: String)
}

class ApplicationPreferencesImpl(context: Context) : ApplicationPreferences {

    private val preferences = createPreferences(context)

    private val defaultEndpointUrl = context.getString(R.string.default_endpoint_url)
    private val defaultPublicApiToken = ""

    private val API_KEY = context.getString(R.string.prefs_public_token_key)
    private val ENDPOINT_URL_KEY = context.getString(R.string.prefs_endpoint_url_key)

    override fun getEndpointUrl() =
        preferences.getString(ENDPOINT_URL_KEY, null) ?: defaultEndpointUrl

    override fun getPublicApiKey() = preferences.getString(API_KEY, null) ?: defaultPublicApiToken

    override fun setEndpointUrl(endpointUrl: String) {
        preferences.edit().putString(ENDPOINT_URL_KEY, endpointUrl).apply()
    }

    override fun setPublicApiKey(apiToken: String) {
        preferences.edit().putString(API_KEY, apiToken).apply()
    }

    private fun createPreferences(context: Context) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            EncryptedSharedPreferences.create(
                context,
                PREFERENCES_FILENAME,
                MasterKey(context),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } else {
            PreferenceManager.getDefaultSharedPreferences(context)
        }
}

private const val PREFERENCES_FILENAME = "fpjs_prefs"