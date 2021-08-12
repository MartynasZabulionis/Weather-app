package lt.marcius.weather

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import lt.marcius.weather.databinding.ActivityWeatherInfoBinding
import lt.marcius.weather.databinding.WeatherBlockBinding
import java.text.SimpleDateFormat
import java.util.*

class WeatherInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWeatherInfoBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWeatherInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backButton.setOnClickListener { onBackPressed() }

        val cityNameColors = listOf(Color.YELLOW, Color.RED, Color.GREEN)
        val tempColors = listOf(Color.GREEN, Color.YELLOW, Color.RED)
        val dateColors = listOf(Color.RED, Color.GREEN, Color.YELLOW)

        fun getColorIndexFromTemp(temp: Int): Int {
            return when {
                temp <= 10 -> 0
                temp >= 20 -> 2
                else -> 1
            }
        }
        val weatherInfos = intent.getParcelableArrayExtra("weatherInfos")!!.filterIsInstance<WeatherInfo>()
        for (weatherInfo in weatherInfos) {
            binding.weatherList.addView(
                WeatherBlockBinding.inflate(layoutInflater, binding.weatherList,false).also {
                    it.description.text = weatherInfo.description
                    val temp = weatherInfo.temp
                    val colorIndex = getColorIndexFromTemp(temp)
                    it.temp.run {
                        text = temp.toString()
                        setTextColor(tempColors[colorIndex])
                    }
                    it.celsiusSign.setTextColor(tempColors[colorIndex])

                    it.cityName.run {
                        text = weatherInfo.cityName
                        setTextColor(cityNameColors[colorIndex])
                    }

                    val format= SimpleDateFormat("EEE\ndd")
                    val date = Date(weatherInfo.unixTime * 1000L)
                    it.date.run {
                        text = format.format(date).toUpperCase(Locale.ROOT)
                        setTextColor(dateColors[colorIndex])
                    }

                    it.weatherIcon.setImageBitmap(BitmapFactory.decodeByteArray(weatherInfo.iconData, 0, weatherInfo.iconData.size))

                }.root
            )
        }
    }
}