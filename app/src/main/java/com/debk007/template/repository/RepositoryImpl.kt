package com.debk007.template.repository

import com.debk007.template.datasource.DataSource
import com.debk007.template.model.BwibbuData
import com.debk007.template.model.StockDayAllData
import com.debk007.template.model.StockDayAvgAllData
import com.debk007.template.util.ApiState
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dataSource: DataSource
) : Repository {

    override suspend fun getBwibbuAll(): ApiState<List<BwibbuData>> = dataSource.getBwibbuAll()

    override suspend fun getStockDayAvgAll(): ApiState<List<StockDayAvgAllData>> =
        dataSource.getStockDayAvgAll()

    override suspend fun getStockDayAll(): ApiState<List<StockDayAllData>> =
        dataSource.getStockDayAll()
}