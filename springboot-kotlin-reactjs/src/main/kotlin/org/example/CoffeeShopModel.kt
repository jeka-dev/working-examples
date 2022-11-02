package org.example

import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

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