package lt.marcius.weather.mainactivity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.*
import lt.marcius.weather.R
import lt.marcius.weather.WeatherInfo
import lt.marcius.weather.WeatherInfoActivity
import lt.marcius.weather.databinding.ActivityMainBinding
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel: MainActivityViewModel by viewModels()

    private fun setCircularProgressVisibility(visible: Boolean) {
        binding.enterButton.visibility = if (visible) View.GONE else View.VISIBLE
        binding.circularProgress.visibility = if (visible) View.VISIBLE else View.GONE
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (viewModel.isFetchingFromInternet) setCircularProgressVisibility(true)

        viewModel.run {
            fetchingError.observe(this@MainActivity, { message ->
                if (message == null) return@observe
                showSnackbar(message)
                setCircularProgressVisibility(false)
            })
            didFinishFetchingWeatherInfos.observe(this@MainActivity, {
                if (it == null) return@observe
                startWeatherInfosActivity(it)
                setCircularProgressVisibility(false)
            })
        }

        binding.historyButton.setOnClickListener {
            viewModel.startFetchingWeatherHistory()
        }

        binding.enterButton.setOnClickListener {
            if (binding.editText.text.isEmpty()) {
                showSnackbar(resources.getString(R.string.enterCityName))
                return@setOnClickListener
            }
            setCircularProgressVisibility(true)
            viewModel.startFetchingWeatherInfo(binding.editText.text.toString())
        }
    }
    private fun startWeatherInfosActivity(weatherInfos: Array<WeatherInfo>) {
        val intent = Intent(this, WeatherInfoActivity::class.java)
        intent.putExtra("weatherInfos", weatherInfos)
        startActivity(intent)
    }
    private fun showSnackbar(msg: String) {
        Snackbar.make(binding.root, msg, Snackbar.LENGTH_SHORT).show()
    }
}

