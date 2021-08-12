package lt.marcius.weather

import android.os.Parcel
import android.os.Parcelable
import androidx.room.*
import kotlinx.parcelize.Parcelize
import org.json.JSONArray
import org.json.JSONObject
import java.util.*
import kotlin.math.roundToInt

@Parcelize
@Entity
data class WeatherInfo (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val cityName: String,
    val temp: Int,
    val description: String,
    val unixTime: Int,

    @ColumnInfo(typeAffinity = ColumnInfo.BLOB)
    val iconData: ByteArray,
) : Parcelable {
    companion object {
        fun fromResponse(response: JSONObject, iconData: ByteArray): WeatherInfo {
            val weatherData = response.getJSONArray("weather").getJSONObject(0)
            return WeatherInfo(
                cityName = response.getString("name"),
                temp = response.getJSONObject("main").getDouble("temp").roundToInt(),
                description = weatherData.getString("description").capitalize(Locale.ROOT),
                unixTime = response.getInt("dt"),
                iconData = iconData//byteBuffer.array()
            )
        }
    }
}


@Dao
interface WeatherInfoDao {
    @Query("SELECT * FROM WeatherInfo ORDER BY id DESC")
    suspend fun getAll(): List<WeatherInfo>

    @Query("DELETE FROM WeatherInfo WHERE (SELECT COUNT(*) = 5 FROM WeatherInfo) AND id = (SELECT MIN(id) FROM WeatherInfo)")
    suspend fun deleteOldestWeatherInfo(): Unit

    @Insert
    suspend fun insert(weatherInfo: WeatherInfo)
}