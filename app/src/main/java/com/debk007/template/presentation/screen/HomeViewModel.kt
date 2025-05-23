package com.debk007.template.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debk007.template.model.BwibbuData
import com.debk007.template.model.StockDayAllData
import com.debk007.template.presentation.item.DailyStockItem
import com.debk007.template.repository.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    private val TAG = HomeViewModel::class.java.simpleName

    private val _uiState = MutableStateFlow(HomeScreenState())
    val uiState = _uiState.asStateFlow()

    private val bwibbuFlow = flow {

        repository.getBwibbuAll()
            .onSuccess { emit(it) }
            .onFailure {
                emit(null)
            }
    }.filterNotNull()

    private val dailyAvgStockFlow = flow {

        repository.getStockDayAvgAll()
            .onSuccess { emit(it) }
            .onFailure {
                emit(null)
            }
    }.filterNotNull()

    private val dailyStockFlow = flow {

        repository.getStockDayAll()
            .onSuccess { emit(it) }
            .onFailure {
                emit(null)
            }
    }.filterNotNull()

    init {
        _uiState.update { HomeScreenState(isLoading = true) }
        combine(
            bwibbuFlow,
            dailyAvgStockFlow,
            dailyStockFlow
        ) { bwibbuStocks, dailyAvgStocks, dailyStocks ->
            val bwibbuMap: Map<String, BwibbuData> = bwibbuStocks.associateBy { it.code }
            val dailyStockMap: Map<String, StockDayAllData> = dailyStocks.associateBy { it.code }
            val dailyStockItems = dailyAvgStocks
                .filter { dailyStockMap[it.code] != null && bwibbuMap[it.code] != null }
                .mapNotNull { dailyAvgStock ->
                    val bwibbuStock = bwibbuMap[dailyAvgStock.code]
                    val dailyStock = dailyStockMap[dailyAvgStock.code]

                    if (bwibbuStock == null || dailyStock == null) {
                        null
                    } else {
                        DailyStockItem(
                            code = dailyStock.code,
                            name = dailyStock.name,
                            openingPrice = dailyStock.openingPrice.toFloatOrNull() ?: 0f,
                            closingPrice = dailyStock.closingPrice.toFloatOrNull() ?: 0f,
                            highestPrice = dailyStock.highestPrice,
                            lowestPrice = dailyStock.lowestPrice,
                            change = dailyStock.change.toFloat(),
                            avgPriceMonthly = dailyAvgStock.avgPriceMonthly.toFloat(),
                            transaction = dailyStock.transaction,
                            tradeVolume = dailyStock.tradeVolume,
                            tradeValue = dailyStock.tradeValue,
                            peRatio = bwibbuStock.peRatio,
                            dividendYield = bwibbuStock.dividendYield,
                            pbRatio = bwibbuStock.pbRatio
                        )
                    }
                }
                .sortedByDescending { it.code }

            _uiState.value = HomeScreenState(dailyStockItems = dailyStockItems)
        }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun setSortStatus(sortStatus: SortType) = viewModelScope.launch(Dispatchers.IO) {
        val items = _uiState.value.dailyStockItems

        _uiState.update {
            val list = when (sortStatus) {
                SortType.Descending -> items.sortedByDescending { it.code }

                SortType.Ascending -> items.sortedBy { it.code }
            }

            HomeScreenState(dailyStockItems = list)
        }
    }

    enum class SortType {
        Ascending,
        Descending
    }
}