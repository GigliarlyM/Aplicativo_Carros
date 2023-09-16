package com.tentesion.myapplication.data.local

import android.content.ContentValues
import android.content.Context
import android.provider.BaseColumns
import android.text.BoringLayout
import android.util.Log
import com.tentesion.myapplication.data.local.CarrosContract.CarEntry.COLUMN_NAME_BATERIA
import com.tentesion.myapplication.data.local.CarrosContract.CarEntry.COLUMN_NAME_CAR_ID
import com.tentesion.myapplication.data.local.CarrosContract.CarEntry.COLUMN_NAME_POTENCIA
import com.tentesion.myapplication.data.local.CarrosContract.CarEntry.COLUMN_NAME_PRECO
import com.tentesion.myapplication.data.local.CarrosContract.CarEntry.COLUMN_NAME_RECARGA
import com.tentesion.myapplication.data.local.CarrosContract.CarEntry.COLUMN_NAME_URL_PHOTO
import com.tentesion.myapplication.data.local.CarrosContract.CarEntry.TABLE_NAME
import com.tentesion.myapplication.domain.Carro
import java.lang.Exception

class CarRepository(private val context: Context) {

    fun save(carro: Carro): Boolean {
        var isSaved = false

        try {
            val dbHelper = CarsDbHelper(context)
            val db = dbHelper.writableDatabase

            val values = ContentValues().apply {
                put(COLUMN_NAME_CAR_ID, carro.id)
                put(COLUMN_NAME_PRECO, carro.preco)
                put(COLUMN_NAME_BATERIA, carro.bateria)
                put(COLUMN_NAME_POTENCIA, carro.potencia)
                put(COLUMN_NAME_RECARGA, carro.recarga)
                put(COLUMN_NAME_URL_PHOTO, carro.urlFoto)
            }

            val insertId = db?.insert(TABLE_NAME, null, values)

            Log.d("Insert", insertId.toString())

            if (insertId != null) {
                isSaved = true
            }

        } catch (ex: Exception) {
            ex.message?.let {
                Log.e("Erro", it)
            }
        }

        return isSaved
    }

    fun findCarById(id: Int): Carro {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.readableDatabase

        // Lista de colunas a serem exibidas no resultado da Query
        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_CAR_ID,
            COLUMN_NAME_PRECO,
            COLUMN_NAME_POTENCIA,
            COLUMN_NAME_BATERIA,
            COLUMN_NAME_RECARGA,
            COLUMN_NAME_URL_PHOTO
        )

        val filter = "$COLUMN_NAME_CAR_ID = ?"
        val filterValues = arrayOf(id.toString())

        val cursor = db.query(
            TABLE_NAME, // Nome da table
            columns,
            filter,
            filterValues,
            null,
            null,
            null
        )

        var itemId: Long = 0
        var preco: String = ""
        var potencia: String = ""
        var bateria: String = ""
        var recarga: String = ""
        var urlPhoto: String = ""

        with(cursor) {
            while (moveToNext()) {
                itemId = getLong(getColumnIndexOrThrow(COLUMN_NAME_CAR_ID))
                preco = getString(getColumnIndexOrThrow(COLUMN_NAME_PRECO))
                potencia = getString(getColumnIndexOrThrow(COLUMN_NAME_POTENCIA))
                bateria = getString(getColumnIndexOrThrow(COLUMN_NAME_BATERIA))
                recarga = getString(getColumnIndexOrThrow(COLUMN_NAME_RECARGA))
                urlPhoto = getString(getColumnIndexOrThrow(COLUMN_NAME_URL_PHOTO))
            }
        }

        cursor.close()
        return Carro(
            itemId.toInt(),
            preco = preco,
            bateria = bateria,
            potencia = potencia,
            recarga = recarga,
            urlFoto = urlPhoto,
            true
        )

    }

    fun saveIfNotExist(carro: Carro) {
        val car = findCarById(carro.id)

        if (car.id == SEM_VALOR){
            save(car)
        }

    }

    fun getAll(): MutableList<Carro> {
        val dbHelper = CarsDbHelper(context)
        val db = dbHelper.readableDatabase

        // Lista de colunas a serem exibidas no resultado da Query
        val columns = arrayOf(
            BaseColumns._ID,
            COLUMN_NAME_CAR_ID,
            COLUMN_NAME_PRECO,
            COLUMN_NAME_POTENCIA,
            COLUMN_NAME_BATERIA,
            COLUMN_NAME_RECARGA,
            COLUMN_NAME_URL_PHOTO
        )

        val cursor = db.query(
            TABLE_NAME, // Nome da table
            columns,
            null,
            null,
            null,
            null,
            null
        )

        val carros = mutableListOf<Carro>()

        with(cursor) {
            while (moveToNext()) {
                val itemId = getLong(getColumnIndexOrThrow(COLUMN_NAME_CAR_ID))
                val preco = getString(getColumnIndexOrThrow(COLUMN_NAME_PRECO))
                val potencia = getString(getColumnIndexOrThrow(COLUMN_NAME_POTENCIA))
                val bateria = getString(getColumnIndexOrThrow(COLUMN_NAME_BATERIA))
                val recarga = getString(getColumnIndexOrThrow(COLUMN_NAME_RECARGA))
                val urlPhoto = getString(getColumnIndexOrThrow(COLUMN_NAME_URL_PHOTO))

                carros.add(Carro(
                    itemId.toInt(),
                    preco = preco,
                    bateria = bateria,
                    potencia = potencia,
                    recarga = recarga,
                    urlFoto = urlPhoto,
                    true
                ))
            }
        }

        cursor.close()
        return carros
    }

    // @TODO fazer a parte de deletar um carro no db

    companion object {
        val SEM_VALOR = 0
    }
}