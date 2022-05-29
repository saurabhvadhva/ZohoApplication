package com.zohoapplication.di.main.view.fragment

import android.os.Bundle
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
    var loadmore = false
    val mNetworkHelper : NetworkHelper by inject<NetworkHelper>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

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
        setupObserver()
        mResult = 25
        mMainViewModel.getRandomUsers(mResult)
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
                        Utility.hideProgessDialog(requireContext())
                        if (mResult == 25) {
                            mResult += 25
                            loadmore = true
                            response.data?.let { mAdapter.setItems(it) }
                        } else {
                            mResult += 25
                            loadmore = true
                            response.data?.let { mAdapter.addItems(it) }
                        }
                        mMainViewModel._userList.postValue(null)
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
    }

    private fun initViews() {
        with(mBinding) {
            val layoutManager = LinearLayoutManager(requireContext())
            recyclerView.layoutManager = layoutManager
            mAdapter = UsersAdapter {
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
                        if (loadmore && mBinding.edtSearchView.text.toString().trim().isEmpty()
                            && mNetworkHelper.isNetworkConnected()) {
                            loadmore = false
                            mMainViewModel.getRandomUsers(mResult)
                        }
                    }
                }
            })

        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            UserListFragment()
    }
}