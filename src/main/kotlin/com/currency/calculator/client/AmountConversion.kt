package com.currency.calculator.client

import kotlinx.serialization.Contextual
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AmountConversion(
    @SerialName("base_code")
    val base_code: String,
    @SerialName("conversion_rate")
    @Contextual
    val conversion_rate: Double,
    @SerialName("conversion_result")
    @Contextual
    val conversion_result: Double,
    @SerialName("target_code")
    val target_code: String
)