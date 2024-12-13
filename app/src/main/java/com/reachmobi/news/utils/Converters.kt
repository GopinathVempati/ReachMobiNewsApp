package com.reachmobi.news.utils

import androidx.room.TypeConverter
import com.reachmobi.news.domain.model.Source

class Converters {
    @TypeConverter
    fun fromSource(source: Source): String? {
        return source.name
    }

    @TypeConverter
    fun toSource(name: String): Source {
        return Source(id = "default", name = name)
    }
}
