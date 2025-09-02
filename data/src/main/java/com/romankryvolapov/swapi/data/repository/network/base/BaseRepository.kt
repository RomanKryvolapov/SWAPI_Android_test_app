/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.data.repository.network.base

import com.romankryvolapov.swapi.data.utils.CoroutineContextProvider
import com.romankryvolapov.swapi.domain.models.common.ErrorType
import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import com.romankryvolapov.swapi.domain.models.common.ResultEmittedData
import io.ktor.client.call.body
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.RedirectResponseException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.withContext
import kotlinx.serialization.SerializationException
import java.net.UnknownHostException
import javax.net.ssl.SSLPeerUnverifiedException

abstract class BaseRepository(
    private val contextProvider: CoroutineContextProvider
) {

    companion object {
        const val TAG = "BaseRepositoryTag"
    }

    protected suspend inline fun <reified T : Any> getResult(
        noinline call: suspend () -> HttpResponse
    ): ResultEmittedData<T> {
        return try {
            val response = call()
            val statusCode = response.status.value
            val isSuccess = statusCode in 200..299

            if (isSuccess) {
                val responseBody: T = response.body()
                dataSuccess(
                    model = responseBody,
                    message = response.status.description,
                    responseCode = statusCode
                )
            } else {
                val errorText = response.bodyAsText()
                logError("Error response: $errorText", TAG)
                dataError(
                    model = null,
                    error = errorText,
                    title = "Server error",
                    message = errorText,
                    responseCode = statusCode,
                    errorType = ErrorType.SERVER_ERROR,
                    throwable = null
                )
            }
        } catch (exception: RedirectResponseException) {
            logError("Redirect error: ${exception.response.status}", exception, TAG)
            dataError(null, null, "Redirect", exception.message, exception.response.status.value, ErrorType.SERVER_ERROR, exception)
        } catch (exception: ClientRequestException) {
            logError("Client error: ${exception.response.status}", exception, TAG)
            dataError(null, null, "Client error", exception.message, exception.response.status.value, ErrorType.SERVER_ERROR, exception)
        } catch (exception: ServerResponseException) {
            logError("Server error: ${exception.response.status}", exception, TAG)
            dataError(null, null, "Server error", exception.message, exception.response.status.value, ErrorType.SERVER_ERROR, exception)
        } catch (exception: UnknownHostException) {
            logError("No internet: ${exception.message}", exception, TAG)
            dataError(null, null, "No internet connection", "Connect to the Internet and try again", null, ErrorType.NO_INTERNET_CONNECTION, exception)
        } catch (exception: SSLPeerUnverifiedException) {
            logError("SSL error: ${exception.message}", exception, TAG)
            dataError(null, null, "Encryption error", "Encryption error when receiving data from the server", null, ErrorType.EXCEPTION, exception)
        } catch (exception: SerializationException) {
            logError("Serialization error: ${exception.message}", exception, TAG)
            dataError(null, null, "Parse error", "Data format incorrect", null, ErrorType.EXCEPTION, exception)
        } catch (exception: Exception) {
            logError("Unknown error: ${exception.message}", exception, TAG)
            dataError(null, null, "Unknown error", exception.message ?: "Unknown error", null, ErrorType.EXCEPTION, exception)
        }
    }

    protected fun <T> dataError(
        model: T?,
        error: Any?,
        title: String?,
        message: String?,
        responseCode: Int?,
        errorType: ErrorType?,
        throwable: Throwable?
    ): ResultEmittedData<T> = ResultEmittedData.error(
        model = model,
        error = error,
        title = title,
        message = message,
        errorType = errorType,
        responseCode = responseCode,
        throwable = throwable
    )

    protected fun <T> dataSuccess(
        model: T,
        message: String?,
        responseCode: Int
    ): ResultEmittedData<T> = ResultEmittedData.success(
        model = model,
        message = message,
        responseCode = responseCode
    )

    protected suspend fun doAsync(
        asyncMethod: suspend () -> Unit
    ) = withContext(contextProvider.io) {
        asyncMethod()
    }

}