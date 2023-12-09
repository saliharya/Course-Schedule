package com.dicoding.courseschedule.data

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.dicoding.courseschedule.util.QueryType
import com.dicoding.courseschedule.util.QueryUtil
import com.dicoding.courseschedule.util.SortType
import com.dicoding.courseschedule.util.executeThread
import java.util.Calendar

//TODO 4 : Implement repository with appropriate dao
class DataRepository(private val dao: CourseDao) {

    fun getNearestSchedule(type: QueryType): LiveData<Course> {
        val query = QueryUtil.nearestQuery(type)
        return dao.getNearestSchedule(query)
    }

    fun getAllCourse(sortType: SortType): LiveData<PagedList<Course>> {
        val query = QueryUtil.sortedQuery(sortType)
        val config =
            PagedList.Config.Builder().setPageSize(PAGE_SIZE).setEnablePlaceholders(false).build()

        return LivePagedListBuilder(dao.getAll(query), config).build()
    }

    fun getCourse(id: Int): LiveData<Course> {
        return dao.getCourse(id)
    }

    fun getTodaySchedule(): List<Course>? {
        val today = Calendar.getInstance().get(Calendar.DAY_OF_WEEK)
        return MutableLiveData(dao.getTodaySchedule(today)).value
    }

    fun insert(course: Course) = executeThread {
        dao.insert(course)
    }

    fun delete(course: Course) = executeThread {
        dao.delete(course)
    }

    companion object {
        @Volatile
        private var instance: DataRepository? = null
        private const val PAGE_SIZE = 10

        fun getInstance(context: Context): DataRepository? {
            return instance ?: synchronized(DataRepository::class.java) {
                if (instance == null) {
                    val database = CourseDatabase.getInstance(context)
                    instance = DataRepository(database.courseDao())
                }
                return instance
            }
        }
    }
}