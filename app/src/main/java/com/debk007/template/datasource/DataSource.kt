package com.debk007.template.datasource

import com.debk007.template.model.BwibbuData
import com.debk007.template.model.StockDayAllData
import com.debk007.template.model.StockDayAvgAllData

interface DataSource {

    suspend fun getBwibbuAll(): Result<List<BwibbuData>>

    suspend fun getStockDayAvgAll(): Result<List<StockDayAvgAllData>>

    suspend fun getStockDayAll(): Result<List<StockDayAllData>>
}