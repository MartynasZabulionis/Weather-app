package lt.marcius.weather

import android.app.Application
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

class MyApplication: Application() {

    lateinit var db: AppDatabase
    override fun onCreate() {
        super.onCreate()
        db = Room.databaseBuilder(applicationContext, AppDatabase::class.java, "db").build()
    }
}


@Database(entities = [WeatherInfo::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun weatherInfoDao(): WeatherInfoDao
}