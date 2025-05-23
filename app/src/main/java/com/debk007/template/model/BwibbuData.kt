package com.debk007.template.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BwibbuData(
    @SerialName("Date")
    val date: String,
    @SerialName("Code")
    val code: String,
    @SerialName("Name")
    val name: String,
    @SerialName("PEratio")
    val peRatio: String,
    @SerialName("DividendYield")
    val dividendYield: String,
    @SerialName("PBratio")
    val pbRatio: String
)
