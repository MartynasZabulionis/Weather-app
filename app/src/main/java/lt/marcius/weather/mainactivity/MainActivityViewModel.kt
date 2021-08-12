package lt.marcius.weather.mainactivity

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import lt.marcius.weather.MyApplication
import lt.marcius.weather.WeatherInfo
import org.json.JSONArray
import org.json.JSONObject
import java.net.URL

class MainActivityViewModel(application: Application) : AndroidViewModel(application) {
    var isFetchingFromInternet = false
    private val _didFinishFetchingWeatherInfos = MutableLiveData<Array<WeatherInfo>?>()
    val didFinishFetchingWeatherInfos: LiveData<Array<WeatherInfo>?> = _didFinishFetchingWeatherInfos
    private val _fetchingError = MutableLiveData<String?>()
    val fetchingError: LiveData<String?> = _fetchingError

    private fun fetchIcon(iconName: String): ByteArray {
        val url = URL("https://openweathermap.org/img/wn/$iconName@2x.png")
        return url.openConnection().getInputStream().readBytes()
    }

    fun startFetchingWeatherInfo(cityName: String) {
        isFetchingFromInternet = true
        val queue = Volley.newRequestQueue(getApplication())
        val url = Uri.parse(
            "https://api.openweathermap.org/data/2.5/weather?q="+
                    cityName +
                    ",LTU&appid=7587eaff3affbf8e56a81da4d6c51d06&units=metric"
        )

        val jsonObjectRequest = JsonObjectRequest(
            url.toString(),
            null,
            { response ->

                val weatherData = (response["weather"] as JSONArray)[0] as JSONObject

                lateinit var weatherInfo: WeatherInfo
                viewModelScope.launch {
                    withContext(Dispatchers.IO) {
                        val iconData = fetchIcon(weatherData["icon"] as String)
                        weatherInfo = WeatherInfo.fromResponse(response, iconData)
                        (getApplication() as MyApplication).
                        db.weatherInfoDao().run {
                            deleteOldestWeatherInfo()
                            insert(weatherInfo)
                        }
                    }
                    _didFinishFetchingWeatherInfos.value = arrayOf(weatherInfo)
                    _didFinishFetchingWeatherInfos.value = null
                    isFetchingFromInternet = false
                }
            },
            {
                if (it.message != null)
                    _fetchingError.value = "An error occurred"
                else {
                    val jsonObject = JSONObject(String(it.networkResponse.data))
                    _fetchingError.value = jsonObject.getString("message")
                }
                _fetchingError.value = null
                isFetchingFromInternet = false
            }
        )

        queue.add(jsonObjectRequest)
    }
    fun startFetchingWeatherHistory() {
        viewModelScope.launch {
            val weatherInfos = withContext(Dispatchers.IO) {
                (getApplication() as MyApplication).db.weatherInfoDao().getAll()
            }
            _didFinishFetchingWeatherInfos.value = weatherInfos.toTypedArray()
            _didFinishFetchingWeatherInfos.value = null
        }
    }
}