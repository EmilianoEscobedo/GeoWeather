package com.istea.geoweather.page.about

sealed class AboutIntent{
    object goBackToHome : AboutIntent() // Return to HomePage when the user click in the X.
}

