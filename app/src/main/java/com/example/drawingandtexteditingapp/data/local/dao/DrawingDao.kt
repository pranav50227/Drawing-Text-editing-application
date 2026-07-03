package com.example.drawingandtexteditingapp.data.local.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.drawingandtexteditingapp.data.local.entity.DrawingEntity
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
