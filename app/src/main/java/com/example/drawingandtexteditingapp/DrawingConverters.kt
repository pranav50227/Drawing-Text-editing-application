package com.example.drawingandtexteditingapp

import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.room.TypeConverter
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.JsonReader
import com.squareup.moshi.JsonWriter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory

class DrawingConverters {
    private val colorAdapter = object : JsonAdapter<Color>() {
        override fun fromJson(reader: JsonReader): Color? {
            return if (reader.peek() == JsonReader.Token.NULL) {
                reader.nextNull()
            } else {
                Color(reader.nextInt())
            }
        }

        override fun toJson(writer: JsonWriter, value: Color?) {
            if (value == null) {
                writer.nullValue()
            } else {
                writer.value(value.toArgb())
            }
        }
    }

    private val offsetAdapter = object : JsonAdapter<Offset>() {
        override fun fromJson(reader: JsonReader): Offset? {
            return if (reader.peek() == JsonReader.Token.NULL) {
                reader.nextNull()
            } else {
                val s = reader.nextString()
                val parts = s.split(",")
                if (parts.size == 2) {
                    Offset(parts[0].toFloat(), parts[1].toFloat())
                } else {
                    Offset.Zero
                }
            }
        }

        override fun toJson(writer: JsonWriter, value: Offset?) {
            if (value == null) {
                writer.nullValue()
            } else {
                writer.value("${value.x},${value.y}")
            }
        }
    }

    private val dpAdapter = object : JsonAdapter<Dp>() {
        override fun fromJson(reader: JsonReader): Dp? {
            return if (reader.peek() == JsonReader.Token.NULL) {
                reader.nextNull()
            } else {
                reader.nextDouble().toFloat().dp
            }
        }

        override fun toJson(writer: JsonWriter, value: Dp?) {
            if (value == null) {
                writer.nullValue()
            } else {
                writer.value(value.value)
            }
        }
    }

    private val moshi = Moshi.Builder()
        .add(Color::class.java, colorAdapter)
        .add(Offset::class.java, offsetAdapter)
        .add(Dp::class.java, dpAdapter)
        .addLast(KotlinJsonAdapterFactory())
        .build()

    private val listType = Types.newParameterizedType(List::class.java, Line::class.java)
    private val adapter: JsonAdapter<List<Line>> = moshi.adapter(listType)

    @TypeConverter
    fun fromLineList(value: List<Line>): String {
        return adapter.toJson(value)
    }

    @TypeConverter
    fun toLineList(value: String): List<Line> {
        return try {
            adapter.fromJson(value) ?: emptyList()
        } catch (e: Exception) {
            emptyList()
        }
    }
}
