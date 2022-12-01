package com.example.seefood

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.preference.PreferenceFragmentCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


private const val TAG = "SettingsActivity"
class SettingsActivity : AppCompatActivity() {
    private lateinit var builder: AlertDialog.Builder

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)



        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .replace(R.id.settings, SettingsFragment())
                .commit()
        }
        supportActionBar?.setDisplayHomeAsUpEnabled(true)


    }

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