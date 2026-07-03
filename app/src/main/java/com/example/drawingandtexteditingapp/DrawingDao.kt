package com.example.drawingandtexteditingapp

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface DrawingDao {
    @Query("SELECT * FROM drawings ORDER BY dateSaved DESC")
    fun getAllDrawings(): Flow<List<DrawingEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDrawing(drawing: DrawingEntity): Long

    @Delete
    suspend fun deleteDrawing(drawing: DrawingEntity)

    @Query("SELECT * FROM drawings WHERE id = :id")
    suspend fun getDrawingById(id: Int): DrawingEntity?
}
