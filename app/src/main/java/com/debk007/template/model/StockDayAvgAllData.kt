package com.debk007.template.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class StockDayAvgAllData(
    @SerialName("Date")
    val date: String,
    @SerialName("Code")
    val code: String,
    @SerialName("Name")
    val name: String,
    @SerialName("ClosingPrice")
    val closingPrice: String,
    @SerialName("MonthlyAveragePrice")
    val avgPriceMonthly: String
)
