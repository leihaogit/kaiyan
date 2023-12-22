package com.hal.kaiyan.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverter
import androidx.room.TypeConverters
import com.google.gson.Gson
import com.hal.kaiyan.base.BaseApp
import com.hal.kaiyan.dao.VideoDao
import com.hal.kaiyan.entity.VideoInfoData
import java.util.Date


/**
 * ...
 * @author LeiHao
 * @date 2023/12/13
 * @description 数据库工具类
 */

@Database(entities = [VideoInfoData::class], version = 1, exportSchema = false)
@TypeConverters(ConsumptionConverters::class, DateConverter::class)
abstract class VideoDatabase : RoomDatabase() {
    companion object {
        val instance: VideoDatabase by lazy {
            Room.databaseBuilder(
                BaseApp.appContext.applicationContext, VideoDatabase::class.java, "video_database"
            ).build()
        }
    }

    abstract fun getVideoDao(): VideoDao
}

class ConsumptionConverters {
    @TypeConverter
    fun fromConsumption(consumption: VideoInfoData.Consumption): String {
        val gson = Gson()
        return gson.toJson(consumption)
    }

    @TypeConverter
    fun toConsumption(consumptionString: String): VideoInfoData.Consumption {
        val gson = Gson()
        return gson.fromJson(consumptionString, VideoInfoData.Consumption::class.java)
    }
}

class DateConverter {
    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time
    }
}
