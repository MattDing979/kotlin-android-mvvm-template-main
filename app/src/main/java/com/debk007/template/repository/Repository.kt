package com.debk007.template.repository

import com.debk007.template.model.BwibbuData
import com.debk007.template.model.StockDayAllData
import com.debk007.template.model.StockDayAvgAllData
import com.debk007.template.util.ApiState

interface Repository {

    suspend fun getBwibbuAll(): ApiState<List<BwibbuData>>

    suspend fun getStockDayAvgAll(): ApiState<List<StockDayAvgAllData>>

    suspend fun getStockDayAll(): ApiState<List<StockDayAllData>>
}