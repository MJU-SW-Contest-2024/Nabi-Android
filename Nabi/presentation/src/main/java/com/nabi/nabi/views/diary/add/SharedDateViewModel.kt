package com.nabi.nabi.views.diary.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

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
