package com.rhodeon.weathersage.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Country::class), version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun countryDao() : CountryDao
}