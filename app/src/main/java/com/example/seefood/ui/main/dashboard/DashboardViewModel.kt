package com.example.seefood.ui.main.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf("apple","carrot","spaghetti")
    }
    val text: LiveData<MutableList<String>> = _text


    fun click(newItem: String){
        Log.i("in click", newItem)
        _text.value!!.add(newItem)
    }
}