package com.fulfillment.pickupkjobapplication.repository

import com.fulfillment.pickupkjobapplication.database.Firebase
import com.fulfillment.pickupkjobapplication.models.PatchModel
import com.fulfillment.pickupkjobapplication.models.PickUpJobsAPiResponse
import com.fulfillment.pickupkjobapplication.network.PickupJobService
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.firestore.QuerySnapshot
import retrofit2.Response


class PickjobRepository(private val service: PickupJobService) {

    suspend fun patchPickUpjob(
        pickjobId: String,
        patchModel: PatchModel
    ): Response<PickUpJobsAPiResponse> =
        service.patchPickUpjob(pickjobId, patchModel)

    suspend fun signInwithEmailidAndPassword(): Unit = Firebase.signInwithEmailidAndPassword()

    suspend fun getAccessToken(user: FirebaseUser): Task<GetTokenResult> =
        Firebase.getAccessToken(user)

    suspend fun getPickUpJobsList(): QuerySnapshot? = Firebase.getPickUpJobsList()

    fun currentUser() = Firebase.currentUser()
}