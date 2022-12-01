package com.example.seefood.ui.main.home

import android.text.SpannableStringBuilder
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase

class HomeViewModel : ViewModel() {
    val dbRef = FirebaseDatabase.getInstance().reference.child("Users")
    val auth = Firebase.auth
    val user = auth.currentUser!!
    private val _text = MutableLiveData<String>().apply {
        dbRef.child(user.uid).get().addOnSuccessListener {
            Log.i("HV", "DataSnapShot Value: ${it.value}")
            val nickname = it.child("nickname").value.toString()
            value = "Welcome $nickname"
        }.addOnFailureListener {
            value = "Unable to retrieve user data"
        }
    }
    val text: LiveData<String> = _text
}