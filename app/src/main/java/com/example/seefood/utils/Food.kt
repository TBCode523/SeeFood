package com.example.seefood.utils

/*data class Food(val name:String, val ServingSize:String, val calo:String,
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
}*/

data class Food(val name:String, val nutrients:HashMap<String, Float>):java.io.Serializable{
    override fun toString(): String {
        return "name:$name\nnutrients: $nutrients\n"
    }
    fun Food() {}



    @JvmName("getName1")
    fun getName():String{
        return name;
    }
    fun getHM():HashMap<String, Float>{
        return nutrients
    }
}
