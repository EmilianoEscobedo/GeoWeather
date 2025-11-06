package com.istea.geoweather.page.city

import com.istea.geoweather.entity.City

data class CityState(
    val texto: String,
    val filterList: List<City>,
    val favoriteCityList: List<City>
    )