package com.example.seefood.utils

import android.graphics.Color
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.github.mikephil.charting.utils.ColorTemplate

object GRAPH_GENERATOR {
    const val label = "Ratio of nutrients"
    private fun generatePieGraph(pieChart: PieChart, food: Food){
        chartSetup(pieChart)
        addEntries(pieChart, food)

    }
    private fun chartSetup(pieChart:PieChart) {
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.setCenterTextSize(24f)
        pieChart.centerText = "Food Distribution"
        pieChart.description.isEnabled = false

        val legend = pieChart.legend
        legend.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        legend.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        legend.orientation = Legend.LegendOrientation.VERTICAL
        legend.isEnabled = true
        legend.setDrawInside(false)
    }

    private fun addEntries(pieChart:PieChart, food: Food){
        val entries = arrayListOf<PieEntry>()
        val colors = arrayListOf<Int>()
        /*for (k in food.nutrients.keys){
            entries.add(PieEntry(food.nutrients[k]!!, k))
        }*/
        for (c in ColorTemplate.MATERIAL_COLORS){
            colors.add(c)
        }
        for (c in ColorTemplate.VORDIPLOM_COLORS){
            colors.add(c)
        }
        val dataSet = PieDataSet(entries, label)
        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)
        pieChart.data = data
        pieChart.invalidate()
    }
}