package com.nabi.nabi.views.diary.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedDateViewModel : ViewModel() {

    private val _selectedDate = MutableLiveData<String?>()
    val selectedDate: LiveData<String?> get() = _selectedDate

    private val _monthChanged = MutableLiveData<Boolean>()
    val monthChanged: LiveData<Boolean> get() = _monthChanged

    fun clearSelectedDate() {
        _selectedDate.value = null
    }

    fun notifyMonthChanged() {
        _monthChanged.value = true
    }

    fun resetMonthChangedFlag() {
        _monthChanged.value = false
    }
}
