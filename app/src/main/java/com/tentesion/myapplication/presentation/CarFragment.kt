package com.tentesion.myapplication.presentation

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.tentesion.myapplication.R
import com.tentesion.myapplication.data.CarsApi
import com.tentesion.myapplication.data.local.CarRepository
import com.tentesion.myapplication.domain.Carro
import com.tentesion.myapplication.presentation.adapter.CarAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class CarFragment : Fragment() {
    lateinit var fabCalculate: FloatingActionButton
    lateinit var rvListaCarros: RecyclerView
    lateinit var progressBar: ProgressBar
    lateinit var ivNoInternet: ImageView
    lateinit var tvNoInternet: TextView
    lateinit var carsApi: CarsApi

    var listaCarros: ArrayList<Carro> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.car_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRetrofit()
        setupView(view)
        setupListner()
    }

    override fun onResume() {
        super.onResume()
        if (checkForInternet(context)) {
            //callService() -> Essa é outra forma de chamar servicos
            getAllCars()
        } else {
            emptyState()
        }
    }

    fun setupRetrofit() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://igorbag.github.io/cars-api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        carsApi = retrofit.create(CarsApi::class.java)
    }

    fun getAllCars() {
        carsApi.getAllCars().enqueue(object : Callback<List<Carro>> {
            override fun onResponse(call: Call<List<Carro>>, response: Response<List<Carro>>) {
                if (response.isSuccessful) {
                    progressBar.visibility = View.GONE
                    ivNoInternet.visibility = View.GONE
                    tvNoInternet.visibility = View.GONE

                    response.body()?.let {
                        setupList(it)
                    }
                } else {
                    Toast.makeText(context, R.string.response_erro, Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<List<Carro>>, t: Throwable) {
                Toast.makeText(context, R.string.response_erro, Toast.LENGTH_LONG).show()
            }
        })
    }

    fun emptyState() {
        progressBar.visibility = View.GONE
        rvListaCarros.visibility = View.GONE
        ivNoInternet.visibility = View.VISIBLE
        tvNoInternet.visibility = View.VISIBLE
    }

    fun setupView(view: View) {
        view.apply {
            fabCalculate = findViewById(R.id.fab_calculate)
            rvListaCarros = findViewById(R.id.rv_lista_carros)
            progressBar = findViewById(R.id.progressBar)
            ivNoInternet = findViewById(R.id.iv_empty_state)
            tvNoInternet = findViewById(R.id.tv_no_wifi)
        }
    }

    fun setupList(listaCarros: List<Carro>) {
        val adapterCars = CarAdapter(listaCarros)

        rvListaCarros.apply {
            visibility = View.VISIBLE
            adapter = adapterCars
        }
        adapterCars.carItemLister = { carro ->
            val isSaved = CarRepository(requireContext()).saveIfNotExist(carro)
        }
    }

    fun setupListner() {
        fabCalculate.setOnClickListener {
            startActivity(Intent(context, CalcularAutonomiaActivity::class.java))
        }
    }

    fun checkForInternet(context: Context?): Boolean {
        val connectivityManeger =
            context?.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManeger.activeNetwork ?: return false

            val activeNetwork = connectivityManeger.getNetworkCapabilities(network) ?: return false

            return when {
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                else -> false
            }
        } else {
            @Suppress("DEPRECATION")
            val networkInfo = connectivityManeger.activeNetworkInfo ?: return false
            @Suppress("DEPRECATION")
            return networkInfo.isConnected
        }
    }

    // Utilizar o retrofit como abstracao do AsyncTask! :)

}