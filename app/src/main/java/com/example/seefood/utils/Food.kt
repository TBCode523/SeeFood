package com.example.seefood.utils

data class Food(val name:String, val nutrients:HashMap<String, Float>):java.io.Serializable{
    override fun toString(): String {
        return "name:$name\nnutrients: $nutrients\n"
    }
}
