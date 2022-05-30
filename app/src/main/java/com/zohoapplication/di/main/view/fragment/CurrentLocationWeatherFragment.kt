package com.zohoapplication.di.main.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.zohoapplication.R
import com.zohoapplication.databinding.FragmentCurrentLocationWeatherBinding
import com.zohoapplication.di.main.viewmodel.MainViewModel
import com.zohoapplication.utits.Constants
import com.zohoapplication.utits.Status
import com.zohoapplication.utits.Utility
import org.koin.android.viewmodel.ext.android.viewModel


class CurrentLocationWeatherFragment : Fragment() {

    private lateinit var mBinding: FragmentCurrentLocationWeatherBinding

    private val mMainViewModel : MainViewModel by viewModel()
    private val mLocality by lazy {
        arguments?.getString(Constants.DATA)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentCurrentLocationWeatherBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObserver()
        mMainViewModel.getWeather(Constants.KEY, mLocality)
        mBinding.txtCity.text = mLocality?:"-"
    }

    @SuppressLint("SetTextI18n")
    private fun setupObserver() {
        mMainViewModel.currentWeatherDetail.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    Utility.hideProgressDialog()
                    mBinding.currentItem = response.data
                    mBinding.txtTemp.text = "${response.data?.temperature}\u2103"
                    mBinding.txtWind.text = "${response.data?.windSpeed}kph"
                    mBinding.txtHumidity.text = "${response.data?.humidity}%"
                    mBinding.txtPressure.text = "${response.data?.pressure}hPa"
                    mBinding.txtVisibility.text = "${response.data?.visibility}km"
                    mBinding.txtDesc.text = response.data?.weatherDescriptions?.joinToString(", ")
                }

                Status.LOADING -> {
                    Utility.showProgressDialog(
                        requireContext(),
                        getString(R.string.please_wait)
                    )
                }
                Status.ERROR -> {
                    //Handle Error
                    Utility.hideProgressDialog()
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_LONG).show()
                }

                Status.NO_CONNECTION -> {
                    //Handle No connection
                    Utility.hideProgressDialog()
                    Toast.makeText(
                        requireContext(),
                        getString(R.string.no_internet_connection),
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
        }
    }
}