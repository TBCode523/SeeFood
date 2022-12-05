package com.example.seefood.ui.main.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.seefood.utils.Food

class HomeViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    fun getSum(foodLst:List<Food>):HashMap<String,Float>{
        val sumNutrients = HashMap<String,Float>(0)
        for (food in foodLst){
            val nutrients = food.nutrients
            for (k in nutrients.keys){
                nutrients[k]?.let { sumNutrients[k]?.plus(it) }
            }
        }
        return sumNutrients
    }
    fun getAverages(sumNutrients:HashMap<String,Float>, foodsLen:Int):HashMap<String,Float>{
        val avgNutrients = HashMap<String,Float>(0)
        for (k in sumNutrients.keys){
            sumNutrients[k]?.let { avgNutrients[k]?.plus(it) }
        }
        return avgNutrients
    }
}