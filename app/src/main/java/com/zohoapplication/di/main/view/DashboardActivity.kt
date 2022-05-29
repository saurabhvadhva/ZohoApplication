package com.zohoapplication.di.main.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.fragment.NavHostFragment
import com.zohoapplication.App
import com.zohoapplication.R
import com.zohoapplication.databinding.ActivityDashboardBinding
import com.zohoapplication.utits.Constants

class DashboardActivity : AppCompatActivity(),View.OnClickListener {
    private lateinit var mBinding : ActivityDashboardBinding
    var navController : NavController? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        navController?.addOnDestinationChangedListener(object : NavController.OnDestinationChangedListener{
            override fun onDestinationChanged(
                controller: NavController,
                destination: NavDestination,
                arguments: Bundle?
            ) {

                mBinding.toolbar.txtTitle.text = destination.label
                when(destination.id) {
                    R.id.userListFragment -> {
                        Constants.BASE_URL = "https://randomuser.me/"
                    }

                    R.id.userDetailsFragment -> {
                        Constants.BASE_URL = "http://api.weatherstack.com/"
                    }
                }
                (applicationContext as App).refreshScope()
            }

        })
        initListeners()
    }

    private fun initListeners() {
        mBinding.toolbar.btnBack.setOnClickListener(this)
    }

    override fun onBackPressed() {
        if(navController?.currentDestination?.id == R.id.userListFragment) {
            super.onBackPressed()
            overridePendingTransition(R.anim.left_in, R.anim.left_out)
        } else {
            navController?.popBackStack()
        }
    }

    override fun onClick(p0: View?) {
        when(p0?.id) {
            R.id.btn_back -> {
                onBackPressed()
            }
        }
    }
}