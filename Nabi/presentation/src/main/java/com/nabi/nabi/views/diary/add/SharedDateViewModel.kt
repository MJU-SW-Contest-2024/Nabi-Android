package com.nabi.nabi.views.diary.add

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.nabi.domain.model.diary.DiarySelectInfo

class SharedDateViewModel : ViewModel() {

    private val _selectedDate = MutableLiveData("")
    val selectedDate: LiveData<String> get() = _selectedDate

    private val _date = MutableLiveData<DiarySelectInfo>()
    val date: LiveData<DiarySelectInfo> get() = _date

    fun changeSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun changeDateInfo(newInfo: DiarySelectInfo){
        _date.value = newInfo
    }

    fun clearData() {
        _selectedDate.value = ""
    }
}
