package com.fulfillment.pickupkjobapplication.network


import com.fulfillment.pickupkjobapplication.models.PatchModel
import com.fulfillment.pickupkjobapplication.models.PickUpJobsAPiResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.PATCH
import retrofit2.http.Path


interface PickupJobService {

    @PATCH("/api/pickjobs/{pickupJobId}")
    suspend fun patchPickUpjob(
        @Path("pickupJobId") pickupJobId: String,
        @Body patchModel: PatchModel
    ): Response<PickUpJobsAPiResponse>
}