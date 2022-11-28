package com.example.seefood.ui.main.camera

import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.seefood.R
import com.example.seefood.databinding.FragmentCameraBinding
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions

class CameraFragment : Fragment() {

    private var _binding:  FragmentCameraBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val cameraViewModel =
            ViewModelProvider(this)[CameraViewModel::class.java]

        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val root: View = binding.root

        val textView: TextView = binding.textNotifications
        cameraViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }
        //TODO Parse content and push it, the image, & food name to firebase
        val photo1 = BitmapFactory.decodeResource(resources, R.drawable.n)
        val input = InputImage.fromBitmap(photo1,0)
        Log.d("photo", "photo1: $photo1")
        val result = recognizer.process(input).addOnSuccessListener { visionText ->
            Log.d("RECOGNIZER", "TEXT: ${visionText.text}")
            textView.text = visionText.text
            parseText(visionText)
        }.addOnFailureListener{ e ->
            Log.d("RECOGNIZER", "FAILED! ${e.localizedMessage}")
        }
        return root
    }

    private fun parseText(visionText: Text?) {
        //TODO parse vision text
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}