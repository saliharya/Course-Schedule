package com.dicoding.courseschedule.ui.add

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Spinner
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.R
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.TimePickerFragment
import java.util.Locale

class AddCourseActivity : AppCompatActivity(), TimePickerFragment.DialogTimeListener {

    private lateinit var viewModel: AddCourseViewModel
    private lateinit var tvStartTime: TextView
    private lateinit var tvEndTime: TextView

    private val START_TIME_PICKER_TAG = "start_time_picker"
    private val END_TIME_PICKER_TAG = "end_time_picker"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_course)

        supportActionBar?.title = resources.getString(R.string.add_course)

        viewModel = ViewModelProvider(
            this, AddCourseViewModelFactory(DataRepository.getInstance(this))
        )[AddCourseViewModel::class.java]

        tvStartTime = findViewById(R.id.tv_start_time)
        tvEndTime = findViewById(R.id.tv_end_time)

        val ibStartTime: ImageButton = findViewById(R.id.ib_start_time)
        val ibEndTime: ImageButton = findViewById(R.id.ib_end_time)

        ibStartTime.setOnClickListener {
            showTimePickerDialog(START_TIME_PICKER_TAG)
        }

        ibEndTime.setOnClickListener {
            showTimePickerDialog(END_TIME_PICKER_TAG)
        }
    }

    private fun showTimePickerDialog(tag: String) {
        val timePickerFragment = TimePickerFragment()
        timePickerFragment.show(supportFragmentManager, tag)
    }

    override fun onDialogTimeSet(tag: String?, hour: Int, minute: Int) {
        when (tag) {
            START_TIME_PICKER_TAG -> tvStartTime.text =
                String.format(Locale.getDefault(), "%02d:%02d", hour, minute)

            END_TIME_PICKER_TAG -> tvEndTime.text =
                String.format(Locale.getDefault(), "%02d:%02d", hour, minute)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_add, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_insert -> {
                val courseName = findViewById<EditText>(R.id.ed_course_name).text.toString()
                val spinnerDay = findViewById<Spinner>(R.id.spinner_day)
                val day = spinnerDay.selectedItemPosition
                val startTime = tvStartTime.text.toString()
                val endTime = tvEndTime.text.toString()
                val lecturer = findViewById<EditText>(R.id.ed_lecturer).text.toString()
                val note = findViewById<EditText>(R.id.ed_note).text.toString()

                if (courseName.isEmpty() || startTime.isEmpty() || endTime.isEmpty()) {
                    Toast.makeText(
                        this, getString(R.string.input_empty_message), Toast.LENGTH_SHORT
                    ).show()
                } else {
                    viewModel.insertCourse(courseName, day, startTime, endTime, lecturer, note)
                    viewModel.saved.observe(this) { event ->
                        event.getContentIfNotHandled()?.let { isSaved ->
                            if (isSaved) {
                                Toast.makeText(
                                    this, "Course added successfully", Toast.LENGTH_SHORT
                                ).show()
                                finish()
                            } else {
                                Toast.makeText(this, "Failed to add course", Toast.LENGTH_SHORT)
                                    .show()
                            }
                        }
                    }
                }
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }

}

