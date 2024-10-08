package com.example.onthetime.ui.activities

import android.app.Activity
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.onthetime.R

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.Toast
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.example.onthetime.repository.EmployerRepository
//import com.example.onthetime.model.UserDatabase
import com.example.onthetime.ui.fragments.LoginFragment
import com.example.onthetime.ui.fragments.MainFragment
import com.example.onthetime.viewmodel.CreateShiftViewModel
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController

    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 1001
        const val REQUEST_CODE_TAKE_PHOTO = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        val sharedPref = getSharedPreferences("user_data", Activity.MODE_PRIVATE)

        val email = sharedPref.getString("email",null)
        val password = sharedPref.getString("password",null)


        val navHostFragment = supportFragmentManager.findFragmentById(R.id.fragmentContainerView) as NavHostFragment
        navController = navHostFragment.navController


        if (email != null && password != null) {
            changeStartDestination(R.id.mainFragment)
        } else {
            changeStartDestination(R.id.homeFragment)
        }



        if (!isNetworkAvailable(this)) {
            Toast.makeText(this, "İnternet bağlantısı yoxdur!", Toast.LENGTH_LONG).show()

            showNoInternetView()
        }
        else
        {

        }
    }

    private fun changeStartDestination(startDestination: Int) {
        val navGraph = navController.navInflater.inflate(R.navigation.nav_graph)
        navGraph.setStartDestination(startDestination)
        navController.graph  = navGraph
    }



    fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val network = connectivityManager.activeNetwork ?: return false
            val networkCapabilities = connectivityManager.getNetworkCapabilities(network) ?: return false
            return networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                    networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR)
        } else {
            val networkInfo = connectivityManager.activeNetworkInfo ?: return false
            return networkInfo.isConnectedOrConnecting
        }
    }

    private fun showNoInternetView() {
        val rootView = findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, "İnternet bağlantısı yok!", Snackbar.LENGTH_INDEFINITE)
            .setAction("Yeniden Deneyin") {
                if (isNetworkAvailable(this)) {
                    // İnternet bağlantısı geri geldiğinde SnackBar'ı gizleyin
                    Snackbar.make(rootView, "Bağlantı tekrar sağlandı!", Snackbar.LENGTH_SHORT).show()
                }
            }.show()
    }


}