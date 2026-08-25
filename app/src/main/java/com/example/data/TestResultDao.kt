package com.example.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface TestResultDao {
    @Query("SELECT * FROM speed_test_results ORDER BY timestamp DESC")
    fun getAllResults(): Flow<List<TestResult>>

    @Query("SELECT * FROM speed_test_results WHERE networkType = :type ORDER BY timestamp DESC")
    fun getResultsByNetworkType(type: String): Flow<List<TestResult>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertResult(result: TestResult): Long

    @Query("DELETE FROM speed_test_results WHERE id = :id")
    suspend fun deleteResultById(id: Long)

    @Query("DELETE FROM speed_test_results")
    suspend fun clearAll()

    @Query("SELECT AVG(downloadSpeedMbps) FROM speed_test_results")
    fun getAverageDownloadSpeed(): Flow<Double?>

    @Query("SELECT AVG(uploadSpeedMbps) FROM speed_test_results")
    fun getAverageUploadSpeed(): Flow<Double?>

    @Query("SELECT MAX(downloadSpeedMbps) FROM speed_test_results")
    fun getMaxDownloadSpeed(): Flow<Double?>
}
