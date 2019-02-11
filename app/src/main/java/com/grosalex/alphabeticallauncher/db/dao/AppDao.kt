package com.grosalex.alphabeticallauncher.db.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Query
import android.arch.persistence.room.Update
import com.grosalex.alphabeticallauncher.db.entity.App

@Dao
interface AppDao {
    @Query("SELECT * FROM App")
    fun getAll(): List<App>

    @Query("SELECT * FROM App WHERE isFav")
    fun loadAllFaved(): List<App>

    @Query("SELECT * FROM APP ORDER BY launchedCount")
    fun loadAllOrderByLaunched(): List<App>

    @Update
    fun updateApp(app: App)
}