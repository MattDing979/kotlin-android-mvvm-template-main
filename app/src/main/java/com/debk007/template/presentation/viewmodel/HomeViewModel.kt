package com.debk007.template.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.debk007.template.model.BwibbuData
import com.debk007.template.model.StockDayAllData
import com.debk007.template.presentation.item.DailyStockItem
import com.debk007.template.repository.Repository
import com.debk007.template.util.ApiState
import com.debk007.template.util.UiState
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

    private val _dailyStockItemsState: MutableStateFlow<UiState<List<DailyStockItem>>> =
        MutableStateFlow(UiState.Loading(false))
    val dailyStockItemsState = _dailyStockItemsState.asStateFlow()

    private val bwibbuFlow = flow {

        when (val result = repository.getBwibbuAll()) {
            is ApiState.Success -> emit(result.data)

            is ApiState.Error -> {
                emit(null)

                _dailyStockItemsState.update { UiState.Error(result.errorMsg) }
            }
        }
    }.filterNotNull()

    private val dailyAvgStockFlow = flow {

        when (val result = repository.getStockDayAvgAll()) {
            is ApiState.Success -> emit(result.data)

            is ApiState.Error -> {
                emit(null)

                _dailyStockItemsState.update { UiState.Error(result.errorMsg) }
            }
        }
    }.filterNotNull()

    private val dailyStockFlow = flow {

        when (val result = repository.getStockDayAll()) {
            is ApiState.Success -> emit(result.data)

            is ApiState.Error -> {
                emit(null)

                _dailyStockItemsState.update { UiState.Error(result.errorMsg) }
            }
        }
    }.filterNotNull()

    init {
        _dailyStockItemsState.update { UiState.Loading(true) }
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

            _dailyStockItemsState.value = UiState.Success(dailyStockItems)
        }
            .flowOn(Dispatchers.IO)
            .launchIn(viewModelScope)
    }

    fun setSortStatus(sortStatus: SortType) = viewModelScope.launch(Dispatchers.IO) {

        if (_dailyStockItemsState.value is UiState.Success) {
            val items = (_dailyStockItemsState.value as UiState.Success<List<DailyStockItem>>).data

            _dailyStockItemsState.update {
                val list = when (sortStatus) {
                    SortType.Descending -> items.sortedByDescending { it.code }

                    SortType.Ascending -> items.sortedBy { it.code }
                }

                UiState.Success(list)
            }
        }
    }

    enum class SortType {
        Ascending,
        Descending
    }
}