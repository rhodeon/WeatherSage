package com.rhodeon.weathersage.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CountryDao {
    @Query("SELECT DISTINCT Name FROM countries_data")
    fun getCountryList(): List<String>

    @Query("SELECT Code FROM countries_data WHERE Name LIKE :countryName")
    fun getCountryCode(countryName: String): String
}