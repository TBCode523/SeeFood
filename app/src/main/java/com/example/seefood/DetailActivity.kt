package com.example.seefood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.seefood.utils.Food

/*
    This activity gives a break down of the food and nutrition facts
    Includes a visual
 */
class DetailActivity(food:Food) : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
    }
}