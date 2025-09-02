package com.romankryvolapov.swapi.data.network.api

import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.client.statement.HttpResponse

class SwapiApi(private val client: HttpClient) {

    suspend fun getPeoples(
        page: Int,
        people: String?,
    ): HttpResponse {
        return client.get("people/") {
            if (!people.isNullOrBlank()) {
                parameter("search", people)
            }
            parameter("page", page)
        }
    }

    suspend fun getPlanet(id: Int): HttpResponse {
        return client.get("planets/$id/").body()
    }

}