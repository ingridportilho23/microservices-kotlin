package br.com.erudio.model

data class Cambio (

    var id: Long = 0,
    var from: String = "",
    var to: String = "",
    var conversionFactor: Double,
    var convertedValue: Double,
    var environment: String? = ""
)