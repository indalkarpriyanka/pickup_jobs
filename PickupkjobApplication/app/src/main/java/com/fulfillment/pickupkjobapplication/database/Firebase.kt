package com.fulfillment.pickupkjobapplication.database

import android.util.Log
import com.fulfillment.pickupkjobapplication.utils.Constants
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GetTokenResult
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

object Firebase {

    var firebaseFirestore = FirebaseFirestore.getInstance()
    private var firebaseAuth = FirebaseAuth.getInstance()

    suspend fun signInwithEmailidAndPassword() {
        withContext(Dispatchers.IO) {
            var response = firebaseAuth.signInWithEmailAndPassword(
                "testuser@ocff-yellowbear-git.com",
                "test123"
            )
            if (response.isSuccessful) {
                var firebaseUser = response.result!!.user!!
                // getAccessToken()
                Log.d("username", firebaseUser.displayName.toString())
            } else {
                Log.d("username not found", response.exception.toString())
            }
        }
    }

    suspend fun getAccessToken(user: FirebaseUser): Task<GetTokenResult> {
        return withContext(Dispatchers.IO) {
            user.getIdToken(false)
        }
    }

    suspend fun getPickUpJobsList(): QuerySnapshot? {
        return withContext(Dispatchers.IO) {
            firebaseFirestore.collection(Constants.PICKUP_JOB_TABLE_NAME)
                .whereEqualTo("facilityRef", "3q1YPzcGJmVufeVR9kTPyB").get().await()
        }
    }

    fun currentUser() = firebaseAuth.currentUser
}