package com.debk007.template.presentation.item

data class DailyStockItem(
    val code: String,
    val name: String,
    val openingPrice: Float,
    val closingPrice: Float,
    val highestPrice: String,
    val lowestPrice: String,
    val change: Float,
    val avgPriceMonthly: Float,
    val transaction: String,
    val tradeVolume: String,
    val tradeValue: String,
    val peRatio: String,
    val dividendYield: String,
    val pbRatio: String
)
