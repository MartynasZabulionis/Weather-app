package lt.marcius.weather

import org.json.JSONObject
import org.junit.Test

import org.junit.Assert.*

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class WeatherInfoTest {
    @Test
    fun isInitializationFromJSON_Correct() {
        val jsonObject = JSONObject("{\"coord\":{\"lon\":-43.2075,\"lat\":-22.9028},\"weather\":[{\"id\":802,\"main\":\"Clouds\",\"description\":\"scattered clouds\",\"icon\":\"03d\"}],\"base\":\"stations\",\"main\":{\"temp\":24.98,\"feels_like\":25.23,\"temp_min\":24.98,\"temp_max\":25,\"pressure\":1019,\"humidity\":65},\"visibility\":10000,\"wind\":{\"speed\":6.17,\"deg\":200},\"clouds\":{\"all\":40},\"dt\":1628687708,\"sys\":{\"type\":1,\"id\":8429,\"country\":\"BR\",\"sunrise\":1628673638,\"sunset\":1628714131},\"timezone\":-10800,\"id\":3451190,\"name\":\"Rio de Janeiro\",\"cod\":200}")
        val weatherInfo = WeatherInfo.fromResponse(jsonObject, ByteArray(0))

        assertEquals(weatherInfo.cityName, "Rio de Janeiro")
        assertEquals(weatherInfo.unixTime, 1628687708)
        assertEquals(weatherInfo.description, "Scattered clouds")
        assertEquals(weatherInfo.temp, 25)
    }
}