package com.dicoding.courseschedule.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//TODO 1 : Define a local database table using the schema in app/schema/course.json
@Entity(tableName = "course")
data class Course(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    @ColumnInfo(name = "title") val courseName: String,
    @ColumnInfo(name = "title") val day: Int,
    @ColumnInfo(name = "title") val startTime: String,
    @ColumnInfo(name = "title") val endTime: String,
    @ColumnInfo(name = "title") val lecturer: String,
    @ColumnInfo(name = "title") val note: String
)
