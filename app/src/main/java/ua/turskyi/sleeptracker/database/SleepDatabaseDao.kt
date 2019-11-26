package ua.turskyi.sleeptracker.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.*

@Dao
interface SleepDatabaseDao{
    @Insert
    fun insert(night: SleepNight)

    @Update
    fun update(night: SleepNight )

    @Query("SELECT * from ${SleepNight.TABLE_NAME} WHERE nightId = :key")
    fun get(key: Long): SleepNight

    //not efficient methods:
//    @Delete
//    fun delete(night: SleepNight)
//
//    @Delete
//    fun deleteAllNights(nights: List<SleepNight>): Int

    @Query("DELETE FROM ${SleepNight.TABLE_NAME}")
    fun clear()

    @Query("SELECT * FROM ${SleepNight.TABLE_NAME} ORDER BY nightId DESC")
    fun getAllNights(): LiveData<List<SleepNight>>

    @Query("SELECT * FROM ${SleepNight.TABLE_NAME} ORDER BY nightId DESC LIMIT 1")
    fun getTonight(): SleepNight?

    /**
     * Selects and returns the night with given nightId.
     */
    @Query("SELECT * from daily_sleep_quality_table WHERE nightId = :key")
    fun getNightWithId(key: Long): LiveData<SleepNight>
}

