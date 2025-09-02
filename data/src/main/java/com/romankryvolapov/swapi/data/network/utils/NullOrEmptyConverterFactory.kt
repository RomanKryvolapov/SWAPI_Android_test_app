/**
 * Created & Copyright 2025 by Roman Kryvolapov
 **/
package com.romankryvolapov.swapi.data.network.utils

import com.romankryvolapov.swapi.domain.models.common.LogUtil.logError
import okhttp3.ResponseBody
import okhttp3.ResponseBody.Companion.asResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.lang.reflect.Type

class NullOrEmptyConverterFactory(
    private val gsonConverterFactory: GsonConverterFactory,
) : Converter.Factory() {

    companion object {
        private const val TAG = "NullOrEmptyConverterFactoryTag"
    }

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *> {
        return Converter<ResponseBody, Any?> { body ->
            if (body.contentLength() != 0L) {
                val contentType = body.contentType()
                val source = body.source()
                source.request(Long.MAX_VALUE)
                val bufferClone = source.buffer.clone()
                val subtype = contentType?.subtype
                val clonedBody =
                    bufferClone.clone().asResponseBody(contentType, bufferClone.size)
                try {
                    when {
                        subtype?.contains("json", ignoreCase = true) == true -> {
                            val converter = gsonConverterFactory.responseBodyConverter(
                                type,
                                annotations,
                                retrofit
                            )
                            converter?.convert(body)
                        }

                        else -> {
                            body.string()
                        }
                    }
                } catch (e: Exception) {
                    logError("Content type not valid, Exception: ${e.message}", e, TAG)
                    clonedBody.string()
                }
            } else null
        }
    }
}