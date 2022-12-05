package com.example.seefood

import android.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.core.text.isDigitsOnly
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.seefood.utils.Food
import com.example.seefood.utils.detailAdapter

/*
    This activity gives a break down of the food and nutrition facts
    Includes a visual
 */

class DetailActivity : AppCompatActivity() {
    private lateinit var newRecyclerView: RecyclerView
    private lateinit var namelst: MutableList<MutableList<String>>

    lateinit var nutval : MutableList<String>
    lateinit var nutname : MutableList<String>

    val caloriesRecommded = 2000
    val totalFatRecommended= 78
    val cholesterolRecomended = 300
    val sodiumRecomended = 2300
    val carbsRecomended = 275
    val dfibersRecomended = 28
    val proteinRecomende = 50
    val addSugarsRec = 50
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        val bundle: Bundle? = intent.extras

        //for ((key, value) in map) {
        //println("$key = $value")
        //}
        val foodnam = findViewById(R.id.foodNamedetail) as TextView


        val fd = intent.extras!!.get("namelst") as Food
        foodnam.text = fd.getName()
            Log.d("Saving", "New foodLst: $fd")
        namelst = mutableListOf()
        for ((key, value) in fd.getHM()) {
            namelst.add(mutableListOf(key, value.toString()))
        }

        newRecyclerView = findViewById(R.id.detail_recycler)
        newRecyclerView.layoutManager = LinearLayoutManager(this)
        newRecyclerView.setHasFixedSize(true)
        newRecyclerView.adapter = detailAdapter(namelst);
    }




    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}