package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.courseschedule.R

class AddCourseActivity : AppCompatActivity() {

    private lateinit var viewModel: AddCourseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        supportActionBar?.title = resources.getString(R.string.add_course)
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

