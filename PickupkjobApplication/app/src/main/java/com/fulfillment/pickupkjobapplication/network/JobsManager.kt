package com.fulfillment.pickupkjobapplication.network


import android.util.Log
import com.fulfillment.pickupkjobapplication.models.PickupJobs

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class JobsManager(private val service: PickupJobService) {

    suspend fun patchPickUpjob(pickupjobModel: PickupJobs) =
        withContext(Dispatchers.IO) {
            val response= pickupjobModel.id?.let { service.patchPickUpjob(it) }

            Log.d("response",response.toString())
        }
}