package com.istea.geoweather.page.city

import com.istea.geoweather.entity.City

data class CityState(
    val text: String,
    val filterList: List<City>,
    val favoriteCityList: List<City>

    //wich states from the City entity we will need were?
    val city: String,
    val country: String,
    val temperature: String,
    )
