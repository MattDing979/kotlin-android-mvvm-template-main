package com.debk007.template.repository

import com.debk007.template.model.BwibbuData
import com.debk007.template.model.StockDayAllData
import com.debk007.template.model.StockDayAvgAllData

interface Repository {

    suspend fun getBwibbuAll(): Result<List<BwibbuData>>

    suspend fun getStockDayAvgAll(): Result<List<StockDayAvgAllData>>

    suspend fun getStockDayAll(): Result<List<StockDayAllData>>
}