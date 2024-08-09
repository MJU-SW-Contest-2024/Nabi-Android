package com.nabi.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [DiaryEntity::class], version = 1, exportSchema = false)
abstract class DiaryDatabase : RoomDatabase() {

    companion object {

        @Volatile
        private var INSTANCE: DiaryDatabase? = null

        private fun buildDatabase(context: Context): DiaryDatabase =
            Room.databaseBuilder(
                context.applicationContext,
                DiaryDatabase::class.java,
                "temp-diarys"
            ).build()

        fun getInstance(context: Context): DiaryDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
    }

    abstract fun getDiaryDao(): DiaryDAO
}