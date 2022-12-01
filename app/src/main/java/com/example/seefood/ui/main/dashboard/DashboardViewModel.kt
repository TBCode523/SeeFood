package com.example.seefood.ui.main.dashboard

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DashboardViewModel : ViewModel() {

    private val _text = MutableLiveData<MutableList<String>>().apply {
        value = mutableListOf()
    }
    val text: LiveData<MutableList<String>> = _text

    fun delete(delPos: Int){
        if (_text.value!!.size >=1){
            _text.value?.removeAt(delPos)
        }else{
            _text.value = mutableListOf()
        }


    }


    fun click(newItem: String){
        Log.i("in click", newItem)
        //this will need to change to be access firebase
        _text.value!!.add(newItem)
    }
}