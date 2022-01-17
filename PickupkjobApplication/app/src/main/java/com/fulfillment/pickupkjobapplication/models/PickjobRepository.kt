package com.fulfillment.pickupkjobapplication.models





import com.fulfillment.pickupkjobapplication.database.Firebase
import com.fulfillment.pickupkjobapplication.network.PatchModel
import com.fulfillment.pickupkjobapplication.network.PickupJobService
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PickjobRepository(private val service: PickupJobService) {

    suspend fun patchPickUpjob(pickjobId: String, patchModel: PatchModel):PickUpJobsAPiResponse =
        withContext(Dispatchers.IO) {
            val response = service.patchPickUpjob(pickjobId, patchModel)
            return@withContext response
        }

    suspend fun signInwithEmailidAndPassword(): Unit = Firebase().signInwithEmailidAndPassword()

    suspend fun getAccessToken(user: FirebaseUser): Task<GetTokenResult> =Firebase().getAccessToken(user)

    suspend fun getPickUpJobsList(): Task<QuerySnapshot> =Firebase().getPickUpJobsList()

    fun currentUser()=Firebase().currentUser()


}