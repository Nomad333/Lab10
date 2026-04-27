package com.example.lab10

import java.io.Serializable

data class Car(
    val brand: String,
    val model: String,
    val year: Int,
    val description: String,
    val cost: String,
    val imageResId: Int
) : Serializable