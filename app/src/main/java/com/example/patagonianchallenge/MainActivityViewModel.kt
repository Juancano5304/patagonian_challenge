package com.example.patagonianchallenge

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MainActivityViewModel: ViewModel() {
    val showDialog: LiveData<Boolean>
        get() = _showDialog
    private val _showDialog = MutableLiveData<Boolean>()

    val showView: LiveData<Boolean>
        get() = _showView
    private val _showView = MutableLiveData<Boolean>()

    fun showDialog() {
        _showDialog.postValue(true)
    }

    fun hideDialog() {
        _showDialog.postValue(false)
    }

    fun showView() {
        _showView.postValue(true)
    }

    fun hideView() {
        _showView.postValue(false)
    }

    fun showSessionCount(sessionCounter: Int) {
        if(sessionCounter == 0 || sessionCounter > 10) {
            return
        }
        if(sessionCounter % 2 == 0) {
            showDialog()
        } else {
            showView()
        }
    }

}