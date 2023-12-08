package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.courseschedule.R

class AddCourseActivity : AppCompatActivity() {

    private lateinit var viewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)
    }
}
