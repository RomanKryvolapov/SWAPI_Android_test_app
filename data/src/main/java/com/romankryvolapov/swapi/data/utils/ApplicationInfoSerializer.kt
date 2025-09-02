/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.data.utils

import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyPermanentlyInvalidatedException
import android.security.keystore.KeyProperties
import androidx.datastore.core.Serializer
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.data.BuildConfig.KEY_STORE_KEY_ALIAS
import com.romankryvolapov.swapi.domain.defaultApplicationInfo
import com.romankryvolapov.swapi.domain.mappers.ApplicationInfoMapper
import com.romankryvolapov.swapi.domain.models.application.ApplicationInfo
import com.romankryvolapov.swapi.domain.models.application.ApplicationInfoNullable
import com.squareup.moshi.JsonAdapter
import okio.buffer
import okio.sink
import okio.source
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.InputStream
import java.io.OutputStream
import java.security.KeyStore
import javax.crypto.Cipher
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey
import javax.crypto.spec.GCMParameterSpec

object ApplicationInfoSerializer : Serializer<ApplicationInfo>, KoinComponent {

    const val TAG = "ApplicationInfoSerializerTag"

    private const val ANDROID_KEY_STORE = "AndroidKeyStore"
    private const val TRANSFORMATION = "AES/GCM/NoPadding"
    private const val IV_SIZE = 12
    private const val TAG_LENGTH = 128

    private val adapter: JsonAdapter<ApplicationInfoNullable> by inject()
    private val applicationInfoMapper: ApplicationInfoMapper by inject()

    override val defaultValue: ApplicationInfo = defaultApplicationInfo

    override suspend fun readFrom(input: InputStream): ApplicationInfo {
        logDebug("readFrom", TAG)
        return try {
            var secretKey = getSecretKey()
            input.source().buffer().use { bufferedSource ->
                val all = bufferedSource.readByteArray()
                if (all.size <= IV_SIZE) {
                    logError("readFrom all.size <= IV_SIZE", TAG)
                    return defaultValue
                }
                val iv = all.copyOfRange(0, IV_SIZE)
                val cipherData = all.copyOfRange(IV_SIZE, all.size)
                val cipher = Cipher.getInstance(TRANSFORMATION)
                cipher.init(Cipher.DECRYPT_MODE, secretKey, GCMParameterSpec(TAG_LENGTH, iv))
                val plain = cipher.doFinal(cipherData)
                val json = String(plain, Charsets.UTF_8)
                val result = adapter.fromJson(json)
                if (result != null) {
                    applicationInfoMapper.reverse(result)
                } else {
                    logError("readFrom result == null", TAG)
                    defaultValue
                }
            }
        } catch (e: Exception) {
            logError("readFrom error: ${e.message}", e, TAG)
            defaultValue
        }
    }

    override suspend fun writeTo(
        to: ApplicationInfo,
        output: OutputStream
    ) {
        logDebug("writeTo", TAG)
        try {
            var secretKey = getSecretKey()
            val toNotNullable = applicationInfoMapper.map(to)
            val json = adapter.toJson(toNotNullable).toByteArray(Charsets.UTF_8)
            val cipher = Cipher.getInstance(TRANSFORMATION)
            cipher.init(Cipher.ENCRYPT_MODE, secretKey)
            val iv = cipher.iv
            val cipherText = cipher.doFinal(json)
            output.sink().buffer().use { bufferedSink ->
                bufferedSink.write(iv)
                bufferedSink.write(cipherText)
                bufferedSink.flush()
            }
        } catch (e: Exception) {
            logError("writeTo error: ${e.message}", e, TAG)
            throw e
        }
    }

    private fun getSecretKey(): SecretKey {
        val keyStore = KeyStore.getInstance(ANDROID_KEY_STORE).apply { load(null) }
        if (keyStore.containsAlias(KEY_STORE_KEY_ALIAS)) {
            try {
                return (keyStore.getEntry(KEY_STORE_KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
            } catch (e: KeyPermanentlyInvalidatedException) {
                keyStore.deleteEntry(KEY_STORE_KEY_ALIAS)
            }
        }
        val keyGenerator =
            KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, ANDROID_KEY_STORE)
        val spec = KeyGenParameterSpec.Builder(
            KEY_STORE_KEY_ALIAS,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .build()
        keyGenerator.init(spec)
        keyGenerator.generateKey()
        return (keyStore.getEntry(KEY_STORE_KEY_ALIAS, null) as KeyStore.SecretKeyEntry).secretKey
    }

}