package com.buuyst07.cusa

import android.content.Context
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TokenManager(val context: Context) {
    private val masterKey = MasterKey.Builder(context)
        .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
        .build()

    private val encryptedPrefs = EncryptedSharedPreferences.create(
        context,
        "cusa_tokens",
        masterKey,
        EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
        EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
    )

    private val gson = Gson()

    suspend fun saveAccount(account: StreamAccount) = withContext(Dispatchers.IO) {
        val accountJson = gson.toJson(account)
        encryptedPrefs.edit().putString("account_${account.id}", accountJson).apply()
    }

    suspend fun getAccount(accountId: String): StreamAccount? = withContext(Dispatchers.IO) {
        val json = encryptedPrefs.getString("account_$accountId", null)
        return@withContext if (json != null) gson.fromJson(json, StreamAccount::class.java) else null
    }

    suspend fun getAllAccounts(): List<StreamAccount> = withContext(Dispatchers.IO) {
        return@withContext encryptedPrefs.all.values
            .filterIsInstance<String>()
            .mapNotNull { json ->
                try {
                    gson.fromJson(json, StreamAccount::class.java)
                } catch (e: Exception) {
                    null
                }
            }
    }

    suspend fun deleteAccount(accountId: String) = withContext(Dispatchers.IO) {
        encryptedPrefs.edit().remove("account_$accountId").apply()
    }

    suspend fun updateAccessToken(accountId: String, newAccessToken: String, expiresIn: Long? = null) = withContext(Dispatchers.IO) {
        val account = getAccount(accountId)
        if (account != null) {
            val updated = account.copy(
                accessToken = newAccessToken,
                expiresIn = expiresIn
            )
            saveAccount(updated)
        }
    }

    fun getLastUsedAccount(): String? {
        return encryptedPrefs.getString("last_used_account", null)
    }

    fun setLastUsedAccount(accountId: String) {
        encryptedPrefs.edit().putString("last_used_account", accountId).apply()
    }

    fun clearAllAccounts() {
        encryptedPrefs.edit().clear().apply()
    }
}
