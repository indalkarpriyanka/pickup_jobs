package com.fulfillment.pickupkjobapplication.models

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.fulfillment.pickupkjobapplication.Constants
import com.fulfillment.pickupkjobapplication.network.Action
import com.fulfillment.pickupkjobapplication.network.PatchModel
import com.google.firebase.auth.FirebaseUser

class PickjobViewModel(private val repository: PickjobRepository) : ViewModel() {

    private val user by lazy {
        repository.currentUser()
    }
    private var _pickupJobList = MutableLiveData<ArrayList<PickupJobs>>()
    val pickupJobList: MutableLiveData<ArrayList<PickupJobs>>
        get() = _pickupJobList

    suspend fun patchPickUpjob(pickjobUiModel: PickjobUiModel) {

        var actions = ArrayList<Action>()
        actions.add(Action("ModifyPickJob", "CLOSED"))

        var patchModel = PatchModel(pickjobUiModel.version, actions)

        var response = repository.patchPickUpjob(pickjobUiModel.id, patchModel)

        if (response.version > pickjobUiModel.version) {
            getPickUpJobsList()
        }

        Log.d("Response=======>", response.toString())
    }

    suspend fun signInwithEmailidAndPassword() {
        repository.signInwithEmailidAndPassword()
    }

    suspend fun getAccessToken() {

        var accessTokentask = user?.let { repository.getAccessToken(it) }
        if (accessTokentask != null) {
            if (accessTokentask.isSuccessful) {
                var token = accessTokentask.result?.token
                if (token != null) {
                    Constants.USER_TOKEN = token
                }
                Log.d("original Auth", accessTokentask.result?.token.toString())
            }
        }
    }

    suspend fun getPickUpJobsList() {
        val pickupJobList = ArrayList<PickupJobs>()
        var pickupJobsTask = repository.getPickUpJobsList()
        if (pickupJobsTask.isSuccessful) {
            pickupJobsTask.result?.documents?.forEach {
                println("data " + it.data)
                var pickupJobs = it.toObject(PickupJobs::class.java)
                if (pickupJobs != null) {
                    pickupJobList.add(pickupJobs)
                }
            }
            _pickupJobList?.postValue(pickupJobList)
            Log.d("value set", _pickupJobList?.value.toString())

        } else {
            Log.d("Error in pickjob fetch", pickupJobsTask.exception?.message.toString())
        }
    }
}