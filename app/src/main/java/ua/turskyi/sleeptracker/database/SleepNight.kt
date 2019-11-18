package ua.turskyi.sleeptracker.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import ua.turskyi.sleeptracker.database.SleepNight.Companion.TABLE_NAME

@Entity(tableName = TABLE_NAME)
data class SleepNight(
    @PrimaryKey(autoGenerate = true) var nightId: Long = 0L,
    @ColumnInfo(name = COLUMN_START_TIME) val startTimeMilli: Long = System.currentTimeMillis(),
    @ColumnInfo(name = COLUMN_END_TIME) var endTimeMilli: Long = startTimeMilli,
    @ColumnInfo(name = COLUMN_RATING) var sleepQuality: Int = -1
) {
    companion object {
        const val TABLE_NAME = "daily_sleep_quality_table"
        const val COLUMN_START_TIME = "start_time_milli"
        const val COLUMN_END_TIME = "end_time_milli"
        const val COLUMN_RATING = "quality_rating"
    }
}