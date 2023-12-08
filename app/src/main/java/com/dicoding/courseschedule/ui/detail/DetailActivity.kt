package com.dicoding.courseschedule.ui.detail

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.DayName.Companion.getByNumber

class DetailActivity : AppCompatActivity() {

    companion object {
        const val COURSE_ID = "course_id"
    }

    private lateinit var viewModel: DetailViewModel
    private lateinit var course: Course

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val courseId = intent.getIntExtra(COURSE_ID, 0)

        val repository = DataRepository.getInstance(this)
        viewModel = ViewModelProvider(
            this, DetailViewModelFactory(repository, courseId)
        )[DetailViewModel::class.java]

        viewModel.course.observe(this) { fetchedCourse ->
            fetchedCourse?.let {
                course = it
                showCourseDetail(it)
            }
        }
    }

    private fun showCourseDetail(course: Course?) {
        course?.apply {
            val timeString = getString(R.string.time_format)
            val dayName = getByNumber(day)
            val timeFormat = String.format(timeString, dayName, startTime, endTime)

            val courseNameTextView = findViewById<TextView>(R.id.tv_course_name)
            val timeTextView = findViewById<TextView>(R.id.tv_time)
            val lecturerTextView = findViewById<TextView>(R.id.tv_lecturer)
            val noteTextView = findViewById<TextView>(R.id.tv_note)

            courseNameTextView.text = courseName
            timeTextView.text = timeFormat
            lecturerTextView.text = lecturer
            noteTextView.text = note
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_detail, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_delete -> {
                AlertDialog.Builder(this).apply {
                    setMessage(getString(R.string.delete_alert))
                    setNegativeButton(getString(R.string.no), null)
                    setPositiveButton(getString(R.string.yes)) { _, _ ->
                        viewModel.delete()
                        finish()
                    }
                    show()
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }
}