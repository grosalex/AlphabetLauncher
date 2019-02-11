package com.grosalex.alphabeticallauncher.db.entity

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class App(@PrimaryKey var packageName: String, var launchedCount: Int, var isFav: Boolean)