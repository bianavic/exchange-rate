package com.currency.calculator.client.model

import com.google.gson.FieldNamingStrategy
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.lang.reflect.Field

@Serializable
data class ExchangeRatesResponse(
    @SerialName("base_code")
    val baseCode: String,
    @SerialName("conversion_rates")
    var ratesResponse: RatesResponse
)

fun parseJSON(jsonString: String): ExchangeRatesResponse {
    val json = Json { ignoreUnknownKeys = true }
    return json.decodeFromString(jsonString)
}

fun createGson() : Gson {
    return GsonBuilder()
        .setFieldNamingStrategy(object : FieldNamingStrategy {
            override fun translateName(f: Field): String {
                return f.getAnnotation(SerialName::class.java)?.value ?: f.name.toUpperCase()
            }
        })
        .setPrettyPrinting()
        .create()
}