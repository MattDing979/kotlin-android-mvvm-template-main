package com.debk007.template.repository

import com.debk007.template.datasource.DataSource
import javax.inject.Inject

class RepositoryImpl @Inject constructor(
    private val dataSource: DataSource
) : Repository {

    override suspend fun getBwibbuAll() = dataSource.getBwibbuAll()

    override suspend fun getStockDayAvgAll() = dataSource.getStockDayAvgAll()

    override suspend fun getStockDayAll() = dataSource.getStockDayAll()
}