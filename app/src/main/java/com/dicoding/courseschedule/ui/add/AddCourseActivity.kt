package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageButton
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.courseschedule.R

class AddCourseActivity : AppCompatActivity() {

    private lateinit var viewModel: AddCourseViewModel
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        supportActionBar?.title = resources.getString(R.string.add_course)

        tvStartTime = findViewById(R.id.tv_start_time)
        tvEndTime = findViewById(R.id.tv_end_time)

        val ibStartTime: ImageButton = findViewById(R.id.ib_start_time)
        val ibEndTime: ImageButton = findViewById(R.id.ib_end_time)

        ibStartTime.setOnClickListener {}

        ibEndTime.setOnClickListener {}
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}

