package com.fetch.fetchit.utils

object Constants {

    /**
     * Store type constants used across the app.
     */
    object grocery {
        const val name = "grocery store"
    }

    object hardware {
        const val name = "hardware store"
    }

    object restaurant {
        const val name = "restaurant"
    }

    val storeTypes = listOf(
        grocery.name,
        hardware.name,
        restaurant.name
    )
}