package com.zohoapplication.di.main.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.zohoapplication.App
import com.zohoapplication.R
import com.zohoapplication.databinding.ActivityDashboardBinding
import com.zohoapplication.utits.Constants
import java.util.*

class DashboardActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var mBinding: ActivityDashboardBinding
    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }
    private lateinit var mFusedLocationClient: FusedLocationProviderClient
    private val permissionId = 2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        initListeners()
    }

    private fun initListeners() {
        mBinding.toolbarDashboard.btnBack.setOnClickListener(this)
        mBinding.fabCurrentForcast.setOnClickListener(this)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            mBinding.toolbarDashboard.txtTitle.text = destination.label
            when (destination.id) {
                R.id.userListFragment -> {
                    Constants.BASE_URL = getString(R.string.random_user_url)
                    mBinding.fabCurrentForcast.visibility = View.VISIBLE
                }

                R.id.userDetailsFragment -> {
                    Constants.BASE_URL = getString(R.string.weather_url)
                    mBinding.fabCurrentForcast.visibility = View.VISIBLE
                }

                R.id.currentLocationWeatherFragment -> {
                    Constants.BASE_URL = getString(R.string.weather_url)
                    mBinding.fabCurrentForcast.visibility = View.GONE
                }
            }
            (applicationContext as App).refreshScope()
        }
    }

    override fun onBackPressed() {
        if (navController.currentDestination?.id == R.id.userListFragment) {
            super.onBackPressed()
            overridePendingTransition(R.anim.left_in, R.anim.left_out)
        } else {
            navController.popBackStack()
        }
    }

    override fun onClick(p0: View?) {
        when (p0?.id) {
            R.id.btn_back -> {
                onBackPressed()
            }

            R.id.fab_current_forcast -> {
                mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
                getLocation()
            }
        }
    }

    @SuppressLint("MissingPermission", "SetTextI18n")
    private fun getLocation() {
        if (checkPermissions()) {
            if (isLocationEnabled()) {
                mFusedLocationClient.lastLocation.addOnCompleteListener(this) { task ->
                    task.result?.let {
                        val location: Location = it
                        location.let { localLocation ->
                            val geocoder = Geocoder(this, Locale.getDefault())
                            val list: List<Address> =
                                geocoder.getFromLocation(
                                    localLocation.latitude,
                                    localLocation.longitude,
                                    1
                                )
                            if (list.isNotEmpty()) {
                                val bundle =
                                    Bundle().apply { putString(Constants.DATA, list[0].locality) }
                                navController.navigate(R.id.currentLocationWeatherFragment, bundle)
                            }
                        }
                    }
                }
            } else {
                Toast.makeText(this, getString(R.string.please_turn_on_location), Toast.LENGTH_LONG)
                    .show()
                val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)
                startActivity(intent)
            }
        } else {
            requestPermissions()
        }
    }

    private fun isLocationEnabled(): Boolean {
        val locationManager: LocationManager =
            getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) || locationManager.isProviderEnabled(
            LocationManager.NETWORK_PROVIDER
        )
    }

    private fun checkPermissions(): Boolean {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            return true
        }
        return false
    }

    private fun requestPermissions() {
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            permissionId
        )
    }

    @SuppressLint("MissingSuperCall")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (requestCode == permissionId) {
            if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                getLocation()
            }
        }
    }
}