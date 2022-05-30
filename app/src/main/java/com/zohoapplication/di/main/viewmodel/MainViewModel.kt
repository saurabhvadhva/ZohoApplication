package com.zohoapplication.di.main.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.zohoapplication.data.dao.UserDao
import com.zohoapplication.data.model.CurrentItem
import com.zohoapplication.data.model.UserItem
import com.zohoapplication.data.repository.MainRepository
import com.zohoapplication.utits.NetworkHelper
import com.zohoapplication.utits.Resource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch


class MainViewModel(
    private val mainRepository: MainRepository,
    private val networkHelper: NetworkHelper
) : ViewModel() {

    init {
        getRandomUsers(25)
    }

    val _userList = MutableLiveData<Resource<List<UserItem>>>()
    val userList: LiveData<Resource<List<UserItem>>>
        get() = _userList

    val _currentWeatherDetail = MutableLiveData<Resource<CurrentItem>>()
    val currentWeatherDetail: LiveData<Resource<CurrentItem>>
        get() = _currentWeatherDetail

    fun getRandomUsers(result: Int) {
        viewModelScope.launch(Dispatchers.Main) {
            _userList.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val deffered = async(Dispatchers.IO) { mainRepository.getRandomUsers(result) }
                deffered.await().let {
                    if (it.isSuccessful) {
                        it.body()?.list?.let {
                            it.forEach { userItem ->
                                userItem.uuid = userItem.loginItem?.uuid.toString()
                                userItem.locationItem?.uuid = userItem.loginItem?.uuid.toString()
                                userItem.pictureItem?.uuid = userItem.loginItem?.uuid.toString()
                                userItem.nameItem?.uuid = userItem.loginItem?.uuid.toString()
                            }
                            mainRepository.insertUsers(it)
                        }
                        _userList.postValue(Resource.success(it.body()?.list))
                    } else _userList.postValue(
                        Resource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            } else {
                val list = mainRepository.getRandomUsersFromLocalDB()
                if(list.isNotEmpty()) {
                    _userList.postValue(Resource.success(list))
                } else
                    _userList.postValue(Resource.noInternet())
            }
        }
    }

    fun getWeather(key: String, state: String?) {
        viewModelScope.launch(Dispatchers.Main) {
            _currentWeatherDetail.postValue(Resource.loading(null))
            if (networkHelper.isNetworkConnected()) {
                val deffered = async(Dispatchers.IO) { mainRepository.getWeather(key, state) }
                deffered.await().let {
                    if (it.isSuccessful) {
                        _currentWeatherDetail.postValue(Resource.success(it.body()?.currentItem))
                    } else _currentWeatherDetail.postValue(
                        Resource.error(
                            it.errorBody().toString(),
                            null
                        )
                    )
                }
            } else {
                _currentWeatherDetail.postValue(Resource.noInternet())
            }
        }
    }
}