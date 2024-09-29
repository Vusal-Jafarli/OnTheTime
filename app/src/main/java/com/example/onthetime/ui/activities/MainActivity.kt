package com.example.onthetime.ui.activities

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.onthetime.R

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.view.View
import android.widget.Toast
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    companion object {
        const val REQUEST_CODE_PICK_IMAGE = 1001
        const val REQUEST_CODE_TAKE_PHOTO = 1002
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        if (!isNetworkAvailable(this)) {
            // İnternet bağlantısı yoksa kullanıcıya bir mesaj göster
            Toast.makeText(this, "İnternet bağlantısı yok!", Toast.LENGTH_LONG).show()

            // Veya özel bir görünüm ekleyebilirsiniz
            showNoInternetView()
        }
        else
        {
//            Toast.makeText(this, "İnternet bağlantısı var!", Toast.LENGTH_LONG).show()

        }
//        supportFragmentManager.beginTransaction().replace(R.id.fragmentContainerView,HomeFragment()).commit()
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