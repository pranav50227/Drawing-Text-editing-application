package com.example.drawingandtexteditingapp

import android.app.Application
import androidx.room.Room
import com.example.drawingandtexteditingapp.data.local.DrawingDatabase
import com.example.drawingandtexteditingapp.data.repository.DrawingRepository

class DrawingApplication : Application() {
    val database: DrawingDatabase by lazy {
        Room.databaseBuilder(
            this,
            DrawingDatabase::class.java,
            "drawing_database"
        ).build()
    }

    val repository: DrawingRepository by lazy {
        DrawingRepository(database.drawingDao())
    }
}
