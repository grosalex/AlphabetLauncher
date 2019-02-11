package com.grosalex.alphabeticallauncher.db

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.content.Context
import com.grosalex.alphabeticallauncher.db.dao.AppDao
import com.grosalex.alphabeticallauncher.db.entity.App

@Database(entities = arrayOf(App::class), version = 1)
abstract class Db : RoomDatabase() {

    abstract fun appDao(): AppDao

    companion object {
        private var INSTANCE: Db? = null
        const val DB_NAME = "alphabetDbName"

        fun getInstance(context: Context): Db {
            INSTANCE?.let { return it }
            val db = Room.databaseBuilder(context.applicationContext, Db::class.java, DB_NAME)
                    .fallbackToDestructiveMigration()
                    .build()
            INSTANCE = db
            return db
        }
    }
}