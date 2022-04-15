package com.example.a4starter

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    private val mText: MutableLiveData<String>
    val text: LiveData<String>
        get() = mText

    val shapes = MutableLiveData<ArrayList<MyShape>>()

    init {
        mText = MutableLiveData()
        mText.value = "This is shared model"
        shapes.value = ArrayList<MyShape>(0)
    }
}