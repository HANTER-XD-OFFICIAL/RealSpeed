package com.example.data

import kotlinx.coroutines.flow.Flow

class TestResultRepository(private val testResultDao: TestResultDao) {
    val allResults: Flow<List<TestResult>> = testResultDao.getAllResults()
    val averageDownload: Flow<Double?> = testResultDao.getAverageDownloadSpeed()
    val averageUpload: Flow<Double?> = testResultDao.getAverageUploadSpeed()
    val maxDownload: Flow<Double?> = testResultDao.getMaxDownloadSpeed()

    fun getFilteredResults(networkType: String): Flow<List<TestResult>> {
        return if (networkType == "ALL") {
            testResultDao.getAllResults()
        } else {
            testResultDao.getResultsByNetworkType(networkType)
        }
    }

    suspend fun insertResult(result: TestResult): Long {
        return testResultDao.insertResult(result)
    }

    suspend fun deleteResult(id: Long) {
        testResultDao.deleteResultById(id)
    }

    suspend fun clearHistory() {
        testResultDao.clearAll()
    }
}
