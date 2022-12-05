package com.example.seefood

import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.text.Spannable
import android.text.SpannableStringBuilder
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.preference.PreferenceFragmentCompat
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.ktx.userProfileChangeRequest
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase


private const val TAG = "SettingsActivity"
class SettingsActivity : AppCompatActivity() {
    private lateinit var builder: AlertDialog.Builder
    private lateinit var updateName: LinearLayoutCompat
    private lateinit var updateEmail: LinearLayoutCompat
    private lateinit var updatePassword: LinearLayoutCompat
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        auth = Firebase.auth

        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings1, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


        // dynamically update textview in settings activity to actual user data?

        // updating the username
        updateName = findViewById(R.id.nameSetting)
        updateName.setOnClickListener {
            Toast.makeText(this, "clicked update name", Toast.LENGTH_SHORT).show()
            // edittext to store the new name value
            val resetName = EditText(this)
            // builder for the dialog box
            val builder = AlertDialog.Builder(this)
            builder.setTitle("Update Name")
            builder.setCancelable(true)
            builder.setMessage("Enter a name of with at least 2 characters.")
            builder.setView(resetName)


            builder.setPositiveButton("UPDATE") { _, _ ->
                //retrieve text from Edittext
                val newName: String = resetName.text.toString()

                if (newName.length >= 2) {
                    Toast.makeText(this,"valid new name", Toast.LENGTH_SHORT).show()
                    val user = auth.currentUser
                    val profileUpdates = userProfileChangeRequest {
                        displayName = newName
                    }

                    user!!.updateProfile(profileUpdates)
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "User profile updated with new name: $newName")
                                Toast.makeText(this,"profile successfully updated", Toast.LENGTH_SHORT).show()
                                user.reload()
                            }
                            else {
                                Log.d(TAG, "User profile NOT updated")
                            }
                        }
                }
                else {
                    Snackbar.make(
                        (findViewById(R.id.settings_layout)) ,
                        "Name must be at least 2 characters",
                        Snackbar.LENGTH_LONG).show()
                }
            }

            builder.setNegativeButton("Cancel") { _, _ ->
                Toast.makeText(this,"update cancelled", Toast.LENGTH_SHORT).show()
            }


            // actual pop up box
            val dialog = builder.create()

            // set update name button to un-clickable until user types 2+ chars
            // implement the TextWatcher callback listener
            /*
            resetName.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                    TODO("Not yet implemented")
                }

                override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

                }

                override fun afterTextChanged(p0: Editable?) {
                    if (p0.toString().trim().length >= 2) {
                        dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = true
                    }
                }

            })
            */
            dialog.show()

            // disable update button by default
            // dialog.getButton(AlertDialog.BUTTON_POSITIVE).isEnabled = false

        }



        // updating the email
        updateEmail = findViewById(R.id.emailSetting)
        updateEmail.setOnClickListener {
            val confirmPass = EditText(this)
            confirmPass.transformationMethod = PasswordTransformationMethod.getInstance()

            val passBuilder = AlertDialog.Builder(this)
            passBuilder.setTitle("Authentication")
            passBuilder.setCancelable(true)
            passBuilder.setMessage("Enter your password again to authenticate your account.")
            passBuilder.setView(confirmPass)

            passBuilder.setPositiveButton("SUBMIT") {_, _ ->

                // retrieve current user email
                val user = FirebaseAuth.getInstance().currentUser
                val currEmail = user?.email.toString()

                //retrieve current password from edittext in the layout
                val currPass = confirmPass.text.toString()

                // check if edditext is empty
                if (currPass.isEmpty()){
                    Toast.makeText(this,
                        "Your current password is required to continue.",
                        Toast.LENGTH_SHORT).show()

                    confirmPass.error = "This field cannot be blank."
                    confirmPass.requestFocus()

                }
                else {
                    // reauthenticateWithCredentials
                    val credentials = EmailAuthProvider.getCredential(currEmail, currPass)
                    user?.reauthenticate(credentials)?.addOnCompleteListener{
                        if (it.isSuccessful){
                            // create 2nd dialog box
                            val resetEmail = EditText(this)
                            resetEmail.inputType = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS

                            val emailBuilder = AlertDialog.Builder(this)
                            emailBuilder.setTitle("Update Email")
                            emailBuilder.setCancelable(true)
                            emailBuilder.setMessage("Enter your new email.")
                            emailBuilder.setView(resetEmail)

                            emailBuilder.setPositiveButton("UPDATE") { _, _ ->
                                null

                            }
                            emailBuilder.setNegativeButton("CANCEL") { _, _ ->
                                // dismiss
                            }

                            val emailDialog = emailBuilder.create()
                            emailDialog.show()

                            // keep dialog active until valid email is provided
                            val submit = emailDialog.getButton(AlertDialog.BUTTON_POSITIVE)
                            submit.setOnClickListener{
                                val newEmail = resetEmail.text.toString()
                                if (newEmail.isEmpty()){
                                    Toast.makeText(this,
                                        "Your new email is required to continue.",
                                        Toast.LENGTH_SHORT).show()

                                    resetEmail.error = "This field cannot be blank."
                                    resetEmail.requestFocus()
                                }
                                else if (newEmail == user.email){
                                    // check that new email provided is different from currEmail
                                    Toast.makeText(this,
                                        "Your new email must NOT match your current email.",
                                        Toast.LENGTH_SHORT).show()

                                    resetEmail.error = "New email was the same as current email."
                                    resetEmail.requestFocus()
                                }
                                else {
                                    // update user data
                                    user.updateEmail(newEmail).addOnCompleteListener(this) { task->
                                        if (task.isSuccessful) {
                                            Toast.makeText(this,
                                                "Your email was successfully updated to $newEmail.",
                                                Toast.LENGTH_SHORT).show()

                                            emailDialog.dismiss()
                                        }
                                        else {
                                            // then it must be a firebase error
                                            Toast.makeText(this,
                                                "Email update failed.",
                                                Toast.LENGTH_SHORT).show()
                                        }
                                    }
                                }

                            }
                        }
                        else {
                            Toast.makeText(this,
                                "The password you entered is incorrect. Try again.",
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
            passBuilder.setNegativeButton("CANCEL") {_, _ ->
                // dismiss
            }

            val passDialog = passBuilder.create()
            passDialog.show()
        }
    }

    // TODO: before work
    // updating password
    // check that new password != currPass
    // new password length >= 6
    // enter password twice before update


    // logout menu inside of settings
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.logout_btn ->{
                Log.i(TAG, "user wants to log out")

                // create alert dialog to confirm logout
                builder = AlertDialog.Builder(this)

                builder.setTitle("LOGOUT")
                builder.setMessage("Are you sure you want to log out of this app?")
                builder.setCancelable(true)
                builder.setPositiveButton("Yes") {
                        _, _ ->
                    Toast.makeText(this,"logging out user", Toast.LENGTH_SHORT).show()
                    // log out and return to main activity
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, AuthActivity::class.java))
                }

                builder.setNegativeButton("No") {
                    _, _ ->
                        Toast.makeText(this,"user remain logged in", Toast.LENGTH_SHORT).show()

                }

                builder.show()
            }

            R.id.delete_user_btn -> {
                Log.d(TAG, "user initiated request to delete Account")

                builder = AlertDialog.Builder(this)
                builder.setTitle("DELETE ACCOUNT")
                builder.setCancelable(true)
                builder.setPositiveButton("Yes") { _,_ ->

                    // delete from firebase
                    // https://firebase.google.com/docs/auth/android/manage-users#delete_a_user
                    val user = Firebase.auth.currentUser!!

                    user.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Log.d(TAG, "User account deleted.")
                            }
                        }

                    // start authActivity
                    FirebaseAuth.getInstance().signOut()
                    startActivity(Intent(this, AuthActivity::class.java))
                }

                builder.setNegativeButton("No") { _,_ ->
                    Toast.makeText(this,"user account remains active", Toast.LENGTH_SHORT).show()
                    // do nothing
                }
            }
        }
        return super.onOptionsItemSelected(item)
    }

    class SettingsFragment : PreferenceFragmentCompat() {
        override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
            setPreferencesFromResource(R.xml.root_preferences, rootKey)
        }
    }


}