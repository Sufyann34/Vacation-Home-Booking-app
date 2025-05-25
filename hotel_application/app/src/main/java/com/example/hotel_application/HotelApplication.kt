package com.example.hotel_application

import android.app.Application
import com.example.hotel_application.utils.UserManager

class HotelApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        UserManager.initialize(this)
    }
} 