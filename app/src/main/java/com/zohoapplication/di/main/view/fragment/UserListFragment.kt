package com.zohoapplication.di.main.view.fragment

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.zohoapplication.R
import com.zohoapplication.data.model.UserItem
import com.zohoapplication.databinding.FragmentUserListBinding
import com.zohoapplication.di.main.adapter.UsersAdapter
import com.zohoapplication.di.main.viewmodel.MainViewModel
import com.zohoapplication.utits.Constants
import com.zohoapplication.utits.NetworkHelper
import com.zohoapplication.utits.Status
import com.zohoapplication.utits.Utility
import org.koin.android.ext.android.inject
import org.koin.android.viewmodel.ext.android.viewModel


class UserListFragment : Fragment() {

    private val mMainViewModel: MainViewModel by viewModel()
    private lateinit var mAdapter: UsersAdapter
    private lateinit var mBinding: FragmentUserListBinding
    var mResult = 25
    var mLoadMore = false
    val mNetworkHelper: NetworkHelper by inject()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mBinding = FragmentUserListBinding.inflate(inflater)
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        mResult = 25
        setupObserver()
        initListeners()
    }

    private fun initListeners() {
        mBinding.edtSearchView.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun afterTextChanged(p0: Editable?) {
                mAdapter.filter.filter(p0.toString())
            }
        })
    }

    private fun setupObserver() {
        mMainViewModel.userList.observe(viewLifecycleOwner) { response ->
            response?.let {
                when (response.status) {
                    Status.SUCCESS -> {
                        Utility.hideProgressDialog()
                        if (mResult == 25) {
                            mResult += 25
                            mLoadMore = true
                            response.data?.let { mAdapter.setItems(it)}
                        } else {
                            mResult += 25
                            mLoadMore = true
                            response.data?.let { mAdapter.setItems(it)}
                        }

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

    private fun initViews() {
        with(mBinding) {
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
            mAdapter = UsersAdapter {
                mBinding.edtSearchView.setText("")
                val userItem = it.tag as UserItem
                val bundle = Bundle().apply { putParcelable(Constants.DATA, userItem) }
                findNavController().navigate(R.id.userDetailsFragment, bundle)
            }
            recyclerView.adapter = mAdapter
            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastVisiblePosition: Int = layoutManager.findLastVisibleItemPosition()
                    if (lastVisiblePosition == mAdapter.itemCount - 1) {
                        if (mLoadMore && mBinding.edtSearchView.text.toString().trim().isEmpty()
                            && mNetworkHelper.isNetworkConnected()
                        ) {
                            mLoadMore = false
                            mMainViewModel.getRandomUsers(mResult)
                        }
                    }
                }
            })
        }
    }
}