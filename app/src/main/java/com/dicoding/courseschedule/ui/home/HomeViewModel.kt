package com.dicoding.courseschedule.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.courseschedule.data.Course
import com.dicoding.courseschedule.data.DataRepository
import com.dicoding.courseschedule.util.QueryType
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: DataRepository?) : ViewModel() {

    private val _nearestSchedule = MutableLiveData<Course?>()
    val nearestSchedule: LiveData<Course?> = _nearestSchedule

    fun fetchNearestSchedule() {
        val currentQueryType = _queryType.value ?: QueryType.CURRENT_DAY
        viewModelScope.launch {
            _nearestSchedule.value = repository?.getNearestSchedule(currentQueryType)?.value
        }
    }

    private val _queryType = MutableLiveData<QueryType>()

    init {
        _queryType.value = QueryType.CURRENT_DAY
    }

    fun setQueryType(queryType: QueryType) {
        _queryType.value = queryType
    }
}
