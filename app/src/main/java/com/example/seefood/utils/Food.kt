package com.example.seefood.utils

import android.graphics.Bitmap
import android.os.Bundle

data class Food(val name:String, val ServingSize:String, val calo:String,
                val tfat:String, val choleserol:String, val sod:String,
                val car:String, val dFib: String, val sug: String, val prot: String
){
    var foodName = name;
    var cal= calo.toInt();
    var totalfat= tfat.toInt();
    var cholesterol = choleserol.toInt();
    var sodium = sod.toInt();
    var carbs = car.toInt();
    var dFiber = dFib.toInt();
    var sugar =sug.toInt();
    var protein = prot.toInt()
    lateinit var image:Bitmap;

    @JvmName("setImage1")
    fun setImage(im:Bitmap){
        this.image = im;
    }
}