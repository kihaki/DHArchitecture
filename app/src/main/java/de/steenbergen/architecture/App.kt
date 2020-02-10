package de.steenbergen.architecture

import android.app.Application
import androidx.room.Room
import de.steenbergen.architecture.sample.ui.login.db.AppDatabase

class App : Application() {

    companion object {
        lateinit var instance: App
            private set

        operator fun not(): App = instance
    }

    val db: AppDatabase by lazy {
        Room.databaseBuilder(
            applicationContext,
            AppDatabase::class.java, "database-name"
        )
            .fallbackToDestructiveMigration()
            .build()
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
    }
}
