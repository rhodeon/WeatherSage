package com.rhodeon.weathersage.database

import androidx.room.Dao
import androidx.room.Query

@Dao
interface CountryDao {
    @Query("SELECT DISTINCT Name FROM Country  ")
    fun getCountryList(): List<String>

    @Query("SELECT Code FROM Country WHERE Name LIKE :countryName")
    fun getCountryCode(countryName: String): String
}