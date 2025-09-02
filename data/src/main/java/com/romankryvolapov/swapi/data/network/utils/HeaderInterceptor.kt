/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.data.network.utils

import com.romankryvolapov.swapi.domain.models.common.LogUtil.logDebug
import com.romankryvolapov.swapi.domain.repository.datastore.DataStoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking

class HeaderInterceptor(
    private val dataStoreRepository: DataStoreRepository,
) : okhttp3.Interceptor {

    companion object {
        private const val TAG = "HeaderInterceptorTag"
    }

    override fun intercept(chain: okhttp3.Interceptor.Chain): okhttp3.Response {
        logDebug("intercept", TAG)
        val original = chain.request()
        val request = original.newBuilder()
            .header("Content-Type", "application/json")
            .header("Cookie", "KEYCLOAK_LOCALE=bg")
            .method(original.method, original.body)
        val token = runBlocking(Dispatchers.IO) {
            dataStoreRepository.readApplicationInfo().accessToken
        }
        if (!token.isNullOrEmpty()) {
            logDebug("add token: $token", TAG)
            request.header("Authorization", "Bearer $token")
        }
        return chain.proceed(request.build())
    }
}