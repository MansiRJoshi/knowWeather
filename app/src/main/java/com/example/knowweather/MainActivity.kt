package com.example.knowweather

import android.graphics.Color
import android.location.Geocoder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.*
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import org.json.JSONObject
import java.util.*

//The app shows current location weather by default wen the app is started
class MainActivity : AppCompatActivity(), View.OnClickListener {
    var cityEntered: String = ""
    lateinit var enterCity: EditText
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val searchIc = findViewById(R.id.ivSearchId) as ImageView

        // click listener for Search Icon
        searchIc.setOnClickListener(this)
        val lat = intent.getStringExtra("lat")
        var long = intent.getStringExtra("long")
        enterCity = findViewById(R.id.tietId) as EditText
        window.statusBarColor = Color.parseColor("#1976D2")
        Toast.makeText(this, lat + " " + long, Toast.LENGTH_LONG).show()
        getJsonData(lat, long)

    }

    private fun getJsonData(lat: String?, long: String?) {

        // Instantiate the RequestQueue.
        //I have used Volley here for interacting with the Api, I can use Retrofit as well.

        val API_KEY = "fe3f47f96360b5ee10017ad1cb231e9d"
        val queue = Volley.newRequestQueue(this)
        val url =
            "https://api.openweathermap.org/data/2.5/weather?lat=${lat}&lon=${long}&appid=${API_KEY}"

// Request a string response from the provided URL.

        val jsonRequest = JsonObjectRequest(
            Request.Method.GET, url, null,
            Response.Listener { response ->
                setValues(response)
            },
            Response.ErrorListener { Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show() })

// Add the request to the RequestQueue.
        queue.add(jsonRequest)
    }

    // Setting Values in te view from Response

    private fun setValues(response: JSONObject) {
        var city = findViewById(R.id.cityNameId) as TextView
        var temp = findViewById(R.id.tvTempId) as TextView
        var condition = findViewById(R.id.tvCondId) as TextView

        city.text = response.getString("name")
        condition.text = response.getJSONArray("weather").getJSONObject(0).getString("main")
        var tempr = response.getJSONObject("main").getString("temp")
        tempr = ((((tempr).toFloat() - 273.15)).toInt()).toString()
        temp.text = "${tempr}°C"
    }

    //on click for search icon
    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.ivSearchId -> {
                Toast.makeText(this, "Hiiiiii", Toast.LENGTH_LONG).show()
                cityEntered = enterCity.text.toString()
                val queue = Volley.newRequestQueue(this)
                val API_KEY = "fe3f47f96360b5ee10017ad1cb231e9d"
                var cityName = ""
                if (cityEntered.isNotEmpty()) {
                    cityName = cityEntered
                }
                //for getting coordinates for the city entered in the enter city edit text.
                var gc = Geocoder(this, Locale.getDefault())
                var addresses = gc.getFromLocationName(cityName, 1)
                var address = addresses?.get(0)
                val latCity = address?.latitude
                val longCity = address?.longitude

                val url =
                    "https://api.openweathermap.org/data/2.5/weather?lat=${latCity}&lon=${longCity}&appid=${API_KEY}"

                val jsonRequest = JsonObjectRequest(
                    Request.Method.GET, url, null,
                    Response.Listener { response ->
                        setValues(response)
                    },
                    Response.ErrorListener {
                        Toast.makeText(this, "ERROR", Toast.LENGTH_LONG).show()
                    })
                queue.add(jsonRequest)

            }
        }
    }
}
