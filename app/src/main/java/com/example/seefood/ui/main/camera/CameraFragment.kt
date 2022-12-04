package com.example.seefood.ui.main.camera

import android.app.Activity
import android.content.Intent
import android.graphics.*
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.FileProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.seefood.databinding.FragmentCameraBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.io.File
import java.util.*

class CameraFragment : Fragment() {

    private var _binding:  FragmentCameraBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    //Added the following instance variables
    private lateinit var imageView: ImageView
    private lateinit var currentPhotoPath: String
    private lateinit var photoFile: File
    private lateinit var cameraViewModel:CameraViewModel
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        cameraViewModel =
            ViewModelProvider(this)[CameraViewModel::class.java]

        _binding = FragmentCameraBinding.inflate(inflater, container, false)
        val root: View = binding.root

        this.imageView = binding.picture
        Log.d("ONCLICK HANDLER", "BEFORE SETTING HANDLER")
        binding.button.setOnClickListener {
            takePicture()
        }
        return root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        auth = Firebase.auth
        val uid = auth.uid!!
        dbRef = FirebaseDatabase.getInstance().reference.child("Users").child(uid)
    }
    private fun saveNutrition(img:Bitmap){

    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun toGrayscale(bmpOriginal: Bitmap): Bitmap {
        val width: Int
        val height: Int
        height = bmpOriginal.height
        width = bmpOriginal.width
        val bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val c = Canvas(bmpGrayscale)
        val paint = Paint()
        val cm = ColorMatrix()
        cm.setSaturation(0f)
        val f = ColorMatrixColorFilter(cm)
        paint.colorFilter = f
        c.drawBitmap(bmpOriginal, 0f, 0f, paint)
        return bmpGrayscale
    }

    private fun takePicture(){
        val pictureIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        photoFile = createImageFile()
        val uri= FileProvider.getUriForFile(this.requireContext(),"com.example.seefood.fileprovider", photoFile)
        pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri)
        startActivityForResult(pictureIntent, 1)
    }
    private fun createImageFile(): File {
        val timeStamp: String= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())
        } else {
            TODO("VERSION.SDK_INT < N")
        }
        val storageDir: File?=this.activity?.getExternalFilesDir(Environment.DIRECTORY_PICTURES)
        return File.createTempFile("JPEG_${timeStamp}_", ".jpg", storageDir).apply{currentPhotoPath = absolutePath}
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(resultCode == Activity.RESULT_OK){
            if(requestCode == 1){
                val uri = FileProvider.getUriForFile(this.requireContext(), "com.example.seefood.fileprovider", photoFile)
                imageView.setImageURI(uri)
                var photo = MediaStore.Images.Media.getBitmap(this.requireActivity().contentResolver, uri)
                photo = toGrayscale(photo)
                val input = photo?.let{ InputImage.fromBitmap(it, 0)}
                val result = input?.let {
                    recognizer.process(it).addOnSuccessListener { visionText ->
                        onRecognizerSuccess(visionText)
                    }.addOnFailureListener{ e ->
                        Log.d("RECOGNIZER", "FAILED! ${e.localizedMessage}")
                    }
                }
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun onRecognizerSuccess(visionText: Text){
        //Do what needs to get done
        Log.d("RECOGNIZER", "TEXT: ${visionText.text}\n")
        Log.d("RESULT", "Map: ${cameraViewModel.parseText(visionText)}\n")
    }
}