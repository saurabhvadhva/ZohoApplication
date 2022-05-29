package com.zohoapplication.di.main.view.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide
import com.zohoapplication.App
import com.zohoapplication.R
import com.zohoapplication.data.model.UserItem
import com.zohoapplication.databinding.FragmentUserDetailsBinding
import com.zohoapplication.di.main.viewmodel.MainViewModel
import com.zohoapplication.utits.Constants
import com.zohoapplication.utits.Status
import com.zohoapplication.utits.Utility
import org.koin.android.viewmodel.ext.android.viewModel
import java.lang.StringBuilder


class UserDetailsFragment : Fragment() {

    private lateinit var mBinding : FragmentUserDetailsBinding
    var mUserItem : UserItem? = null
    private val mMainViewModel: MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mUserItem = it.getParcelable(Constants.DATA)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentUserDetailsBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Constants.BASE_URL = "http://api.weatherstack.com/"
        (requireActivity().applicationContext as App).refreshScope()
        setupObserver()
        setDetails(mUserItem)
    }

    private fun setDetails(userItem: UserItem?) {
        userItem?.let { localUserItem ->
            localUserItem.nameItem?.let {
                val name = StringBuilder()
                it.title.let {
                    name.append(it).append(" ")
                }
                it.first.let {
                    name.append(it).append(" ")
                }
                it.last.let {
                    name.append(it).append(" ")
                }
                mBinding.txtName.text = name.toString().trim().ifEmpty { "-" }
            }
            mBinding.txtEmail.text = localUserItem.email?:"-"
            mBinding.txtPhone.text = localUserItem.phone?:"-"
            mBinding.txtGender.text = localUserItem.gender?:"-"
            Glide.with(mBinding.imgUser.context)
                .load(localUserItem.pictureItem?.large?:"")
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher)
                .into(mBinding.imgUser)

            mBinding.txtCity.text = localUserItem.locationItem?.city?:"-"
            mMainViewModel.getWeather(Constants.KEY,localUserItem.locationItem?.city)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setupObserver() {
        mMainViewModel.currentWeatherDetail.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                Status.SUCCESS -> {
                    Utility.hideProgessDialog(requireContext())
                    mBinding.currentItem = response.data
                    mBinding.txtTemp.text = "${response.data?.temperature}\u2103"
                    mBinding.txtWind.text = "${response.data?.windSpeed}kph"
                    mBinding.txtHumidity.text = "${response.data?.humidity}%"
                    mBinding.txtPressure.text = "${response.data?.pressure}hPa"
                    mBinding.txtVisibility.text = "${response.data?.visibility}km"
                    mBinding.txtDesc.text = response.data?.weatherDescriptions?.joinToString(", ")
                }

                Status.LOADING -> {
                    Utility.showProgessDialog(
                        requireContext(),
                        getString(R.string.please_wait)
                    )
                }
                Status.ERROR -> {
                    //Handle Error
                    Utility.hideProgessDialog(requireContext())
                    Toast.makeText(requireContext(), response.message, Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(userItem: UserItem) =
            UserDetailsFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(Constants.DATA, userItem)
                }
            }
    }
}