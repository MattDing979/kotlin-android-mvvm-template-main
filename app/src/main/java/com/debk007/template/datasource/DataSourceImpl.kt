package com.debk007.template.datasource

import com.debk007.template.model.BwibbuData
import com.debk007.template.model.StockDayAllData
import com.debk007.template.model.StockDayAvgAllData
import com.debk007.template.network.ApiService
import com.debk007.template.util.ApiState
import javax.inject.Inject

class DataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : DataSource {

    override suspend fun getBwibbuAll(): ApiState<List<BwibbuData>> = try {
        ApiState.Success(apiService.getBwibbuAll())
    } catch (e: Exception) {
        ApiState.Error(errorMsg = e.message.toString())
    }

    override suspend fun getStockDayAvgAll(): ApiState<List<StockDayAvgAllData>> = try {
        ApiState.Success(apiService.getStockDayAvgAll())
    } catch (e: Exception) {
        ApiState.Error(errorMsg = e.message.toString())
    }

    override suspend fun getStockDayAll(): ApiState<List<StockDayAllData>> = try {
        ApiState.Success(apiService.getStockDayAll())
    } catch (e: Exception) {
        ApiState.Error(errorMsg = e.message.toString())
    }
}