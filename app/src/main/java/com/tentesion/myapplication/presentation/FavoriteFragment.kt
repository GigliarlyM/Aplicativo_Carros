package com.tentesion.myapplication.presentation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.tentesion.myapplication.R
import com.tentesion.myapplication.data.local.CarRepository
import com.tentesion.myapplication.domain.Carro
import com.tentesion.myapplication.presentation.adapter.CarAdapter

class FavoriteFragment: Fragment() {

    lateinit var rvListaFavorite: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.favorite_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupView(view)
        setupList()
    }

    private fun getCarsOnLocalDb(): MutableList<Carro> {
        val repository = CarRepository(requireContext())
        val carList = repository.getAll()
        return carList
    }

    fun setupView(view: View) {
        view.apply {
            rvListaFavorite = findViewById(R.id.rv_lista_carros_favorite)
        }
    }

    fun setupList(){
        val cars = getCarsOnLocalDb()
        val carroAdapter = CarAdapter(cars, true)
        rvListaFavorite.apply {
            visibility = View.VISIBLE
            adapter = carroAdapter
        }
        carroAdapter.carItemLister = {carro ->
            // @TODO implementar um delete do item car na lista de favoritos
            //CarRepository(requireContext()).deleteCar(carro.id)
        }
    }

}