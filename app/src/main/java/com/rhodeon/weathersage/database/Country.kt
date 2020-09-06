package com.rhodeon.weathersage.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "countries_data")
data class Country (
    @PrimaryKey
    @ColumnInfo(name = "Name", typeAffinity = 1) val name: String,
    @ColumnInfo(name = "Code", typeAffinity = 1) val code: String
)