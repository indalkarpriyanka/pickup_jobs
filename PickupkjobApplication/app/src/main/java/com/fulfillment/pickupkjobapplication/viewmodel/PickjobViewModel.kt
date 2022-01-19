package com.fulfillment.pickupkjobapplication.viewmodel

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.fulfillment.pickupkjobapplication.models.PatchModel
import com.fulfillment.pickupkjobapplication.models.PickUpJobsAPiResponse
import com.fulfillment.pickupkjobapplication.models.PickjobUiModel
import com.fulfillment.pickupkjobapplication.models.PickupJobs
import com.fulfillment.pickupkjobapplication.utils.Constants
import com.fulfillment.pickupkjobapplication.network.Action
import com.fulfillment.pickupkjobapplication.repository.PickjobRepository
import com.google.firebase.auth.FirebaseUser
import retrofit2.Response


class PickjobViewModel(private val repository: PickjobRepository) : ViewModel() {

    private val user by lazy {
        repository.currentUser()
    }
    private var _pickupJobList = MutableLiveData<ArrayList<PickupJobs>>()
    val pickupJobList: MutableLiveData<ArrayList<PickupJobs>>
        get() = _pickupJobList

    private var _isLoading = MutableLiveData<Boolean>()
    val isLoading: MutableLiveData<Boolean>
        get() = _isLoading

    suspend fun patchPickUpjob(id: String, status: String, version: Int) {

        var actions = ArrayList<Action>()
        actions.add(Action("ModifyPickJob", status))

        var patchModel = PatchModel(version, actions)

        var response = repository.patchPickUpjob(id, patchModel)

        handlePickjobPatchResponse(response)

    }

    private fun handlePickjobPatchResponse(response: Response<PickUpJobsAPiResponse>) {

        if (response.isSuccessful) {
            response.body().let { pickUpJobsAPiResponse ->
                //getPickUpJobsList()
                _pickupJobList?.value?.find { it.id == pickUpJobsAPiResponse?.id }?.status =
                    pickUpJobsAPiResponse?.status
                _pickupJobList?.value?.find { it.id == pickUpJobsAPiResponse?.id }?.version =
                    pickUpJobsAPiResponse?.version!!
            }
        } else {
            Log.d("Error", response.message())
        }
    }

    suspend fun signInwithEmailidAndPassword() {
        _isLoading.postValue(true)
        repository.signInwithEmailidAndPassword()
        getAccessToken()
        _isLoading.postValue(false)
    }

    suspend fun getAccessToken() {
        _isLoading.postValue(true)

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
        _isLoading.postValue(false)
    }

    suspend fun getPickUpJobsList() {

        _isLoading.postValue(true)
        val pickupJobList = ArrayList<PickupJobs>()
        var pickupJobsTask = repository.getPickUpJobsList()
        pickupJobsTask?.documents?.forEach {
            println("data " + it.data)
            var pickupJobs = it.toObject(PickupJobs::class.java)
            if (pickupJobs != null) {
                pickupJobList.add(pickupJobs)
            }
        }
        _pickupJobList?.postValue(pickupJobList)
        Log.d("value set", _pickupJobList?.value.toString())
        _isLoading.postValue(false)

    }

    fun getCurrentUser(): FirebaseUser? {
        return repository.currentUser()
    }
}