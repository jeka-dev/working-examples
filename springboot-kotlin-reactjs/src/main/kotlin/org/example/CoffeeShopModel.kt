package org.example

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
data class CoffeeShopModel(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    var id: Long = -1,
    var name: String = "",
    var address: String = "",
    var phone: String = "",
    var priceOfCoffee: Double = 0.0,
    var powerAccessible: Boolean = true,
    var internetReliability: Short = 3 // 1-5
) {}