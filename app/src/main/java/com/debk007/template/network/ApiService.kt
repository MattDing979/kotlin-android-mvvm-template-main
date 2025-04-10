package com.debk007.template.network

import com.debk007.template.model.BwibbuData
import com.debk007.template.model.StockDayAllData
import com.debk007.template.model.StockDayAvgAllData
import retrofit2.http.GET

interface ApiService {

    @GET("exchangeReport/BWIBBU_ALL")
    suspend fun getBwibbuAll(): List<BwibbuData>

    @GET("exchangeReport/STOCK_DAY_AVG_ALL")
    suspend fun getStockDayAvgAll(): List<StockDayAvgAllData>

    @GET("exchangeReport/STOCK_DAY_ALL")
    suspend fun getStockDayAll(): List<StockDayAllData>
}