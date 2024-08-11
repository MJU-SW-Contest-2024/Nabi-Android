package com.nabi.nabi.views.diary.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class SharedDateViewModel : ViewModel() {

    private val _selectedDate = MutableLiveData("")
    val selectedDate: LiveData<String> get() = _selectedDate

    fun changeSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun clearData() {
        _selectedDate.value = ""
    }
}
