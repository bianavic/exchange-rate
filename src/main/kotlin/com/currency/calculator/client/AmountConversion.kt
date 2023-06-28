package com.currency.calculator.client

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AmountConversion(
    @SerialName("base_code")
    val base_code: String,
    @SerialName("conversion_rate")
    val conversion_rate: Double,
    @SerialName("conversion_result")
    val conversion_result: Double,
    @SerialName("documentation")
    val documentation: String,
    @SerialName("result")
    val result: String,
    @SerialName("target_code")
    val target_code: String,
    @SerialName("terms_of_use")
    val terms_of_use: String,
    @SerialName("time_last_update_unix")
    val time_last_update_unix: Int,
    @SerialName("time_last_update_utc")
    val time_last_update_utc: String,
    @SerialName("time_next_update_unix")
    val time_next_update_unix: Int,
    @SerialName("time_next_update_utc")
    val time_next_update_utc: String
)