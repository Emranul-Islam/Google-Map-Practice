package xyz.emranul.google_map_practice

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MainActivity : AppCompatActivity() {

    private val TAG = "MainActivity"
    private lateinit var supportMapFragment: SupportMapFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

         supportMapFragment =
            supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment


        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
            && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    Manifest.permission.ACCESS_COARSE_LOCATION,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ),
                1001
            )
        } else {
            loadMap()
        }


    }

    @SuppressLint("MissingPermission")
    private fun loadMap() {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationProviderClient.lastLocation.addOnCompleteListener { location ->
            if (location.isSuccessful) {

                location.addOnSuccessListener {
                    val latLng = LatLng(it.latitude,it.longitude)
                    supportMapFragment.getMapAsync { map ->

                        map.mapType = GoogleMap.MAP_TYPE_NORMAL
                        map.isMyLocationEnabled = true
                        map.uiSettings.isMyLocationButtonEnabled
                        map.addMarker(MarkerOptions().position(latLng).title("Hey Bro this my Location"))
                        map.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng,15f))
                    }
                }

            } else {
                Log.d(TAG, "loadMap: ${location.exception?.message}")
            }
        }

    }


    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 1001) {
            if (grantResults[0] == ActivityCompat.checkSelfPermission(
                    this,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            ) {
                loadMap()
            } else {
                Log.d(TAG, "onRequestPermissionsResult: Give permission")
            }
        }
    }

}