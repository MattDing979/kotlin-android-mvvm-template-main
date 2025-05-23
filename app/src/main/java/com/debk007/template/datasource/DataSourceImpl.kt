package com.debk007.template.datasource

import com.debk007.template.model.BwibbuData
import com.debk007.template.model.StockDayAllData
import com.debk007.template.model.StockDayAvgAllData
import com.debk007.template.network.ApiService
import javax.inject.Inject

class DataSourceImpl @Inject constructor(
    private val apiService: ApiService
) : DataSource {

    override suspend fun getBwibbuAll() = Result.runCatching { apiService.getBwibbuAll() }

    override suspend fun getStockDayAvgAll() = Result.runCatching { apiService.getStockDayAvgAll() }

    override suspend fun getStockDayAll() = Result.runCatching { apiService.getStockDayAll() }
}