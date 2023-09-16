package com.tentesion.myapplication.domain

data class Carro(
    val id: Int,
    val preco: String,
    val bateria: String,
    val potencia: String,
    val recarga: String,
    val urlFoto: String,
    var isFavorite: Boolean
)