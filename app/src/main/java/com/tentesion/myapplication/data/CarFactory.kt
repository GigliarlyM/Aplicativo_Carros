package com.tentesion.myapplication.data

import com.tentesion.myapplication.domain.Carro

object CarFactory {

    val list = listOf(
        Carro(
            1,
            "R$ 300.000,00",
            "300 kWh",
            "200cv",
            "30 min",
            "www.google.com.br",
            false
        ),
        Carro(
            2,
            "R$ 80.000,00",
            "170 kWh",
            "290cv",
            "50 min",
            "www.google.com.br",
            false
        )
    )
}