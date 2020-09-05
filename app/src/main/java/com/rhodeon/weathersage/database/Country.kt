package com.rhodeon.weathersage.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Country (
    @PrimaryKey val uid: Int,
    @ColumnInfo(name = "Name") val name: String?,
    @ColumnInfo(name = "Code") val code: String?
)