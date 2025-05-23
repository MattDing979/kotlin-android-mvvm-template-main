package com.debk007.template.presentation.screen

import com.debk007.template.presentation.item.DailyStockItem

data class HomeScreenState(
    val isLoading: Boolean = false,
    val dailyStockItems: List<DailyStockItem> = emptyList()
)
