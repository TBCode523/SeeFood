package com.example.seefood

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ProgressBar
import android.widget.TextView

/*
    This activity gives a break down of the food and nutrition facts
    Includes a visual
 */
class DetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        supportActionBar!!.setDisplayHomeAsUpEnabled(true)


        val bundle : Bundle? = intent.extras

        val fname: TextView = findViewById(R.id.foodNamedetail)
        val sSize: TextView = findViewById(R.id.servingsizeDetail)
        val cal: TextView = findViewById(R.id.caloriesdetail)
        val tfat: TextView = findViewById(R.id.totalfatdetail)
        val chol: TextView = findViewById(R.id.choldetail)
        val sod: TextView = findViewById(R.id.sodiumdetail)
        val carbs: TextView = findViewById(R.id.carbsdetail)
        val dfib: TextView = findViewById(R.id.dfibdetal)
        val sug: TextView = findViewById(R.id.sugardetail)
        val p: TextView = findViewById(R.id.proteinDetail)



        val caloriesRecommded = 2000
        val totalFatRecommended= 78
        val cholesterolRecomended = 300
        val sodiumRecomended = 2300
        val carbsRecomended = 275
        val dfibersRecomended = 28
        val proteinRecomende = 50
        val addSugarsRec = 50


        fname.text = bundle!!.getString("name")
        sSize.text = bundle.getString("ssize")
        val calories= bundle.getString("calories")
        cal.text = calories
        val totalFat = bundle.getString("totalfat")
        tfat.text = totalFat
        val cholesterol = bundle.getString("cholesterol")
        chol.text = cholesterol
        val sodium = bundle.getString("sodium")
        sod.text = sodium
        val carbohydrates = bundle.getString("carbs")
        carbs.text = carbohydrates
        val diefibers =  bundle.getString("dfiber")
        dfib.text = diefibers
        val suger = bundle.getString("sugar")
        sug.text = suger
        val protein = bundle.getString("protein")
        p.text = protein

        val pcal: ProgressBar = findViewById(R.id.progressBarCalories)
        pcal.max = caloriesRecommded
        pcal.progress = calories!!.toFloat().toInt()

        val pfat: ProgressBar = findViewById(R.id.fatprogress)
        pfat.max = totalFatRecommended
        pfat.progress = totalFat!!.toFloat().toInt()

        val pchol: ProgressBar = findViewById(R.id.cholesterolprogress)
        pchol.max = cholesterolRecomended
        pchol.progress = cholesterol!!.toFloat().toInt()

        val psod: ProgressBar = findViewById(R.id.sodiumprogress)
        psod.max = sodiumRecomended
        psod.progress = sodium!!.toFloat().toInt()

        val pcarbs: ProgressBar = findViewById(R.id.carbsprogress)
        pcarbs.max = carbsRecomended
        pcarbs.progress = carbohydrates!!.toFloat().toInt()

        val pdf: ProgressBar = findViewById(R.id.dfibprogress)
        pdf.max = dfibersRecomended
        pdf.progress = diefibers!!.toFloat().toInt()


        val sugp: ProgressBar = findViewById(R.id.sugarprogress)
        sugp.max = addSugarsRec
        sugp.progress = suger!!.toFloat().toInt()

        val pprot: ProgressBar = findViewById(R.id.proteinprogress)
        pprot.max = proteinRecomende
        pprot.progress = protein!!.toFloat().toInt()




    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
}