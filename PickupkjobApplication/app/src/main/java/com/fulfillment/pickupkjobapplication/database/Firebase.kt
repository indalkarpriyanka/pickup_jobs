package com.fulfillment.pickupkjobapplication.database

import android.content.Context
import android.util.Log
import android.widget.Toast
import com.fulfillment.pickupkjobapplication.Constants
import com.fulfillment.pickupkjobapplication.models.PickupJobs
import com.fulfillment.pickupkjobapplication.network.Api
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class Firebase {

    private var firebaseFirestore = FirebaseFirestore.getInstance()

    fun signInwithEmailidAndPassword(){
        FirebaseAuth.getInstance()
            .signInWithEmailAndPassword("testuser@ocff-yellowbear-git.com", "test123")
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    val firebaseUser = task.result!!.user!!;
                    val registeredEmail = firebaseUser.email!!

                    getAccessToken(firebaseUser)


                   // Firebase().getPickUpJobsList(firebaseUser.uid,this)
                    //Firebase().getUserDetails(firebaseUser.uid)

                    firebaseUser.displayName?.let { Log.d("username", it) }
//                    Toast.makeText(this, "hello ${firebaseUser.displayName}", Toast.LENGTH_LONG)
//                        .show()
                } else {
                   // Toast.makeText(this, "Sign in failed", Toast.LENGTH_LONG).show()
                    Log.d("Error","sign in failed")
                }
            }

    }

    private fun getAccessToken(user:FirebaseUser)
    {
        user.getIdToken(false).addOnCompleteListener {
            accessTokentask->
            if(accessTokentask.isSuccessful)
            {
                val token=accessTokentask.result?.token

                Constants.USER_TOKEN=token!!

                Log.d("Token",token!!)
            }
        }
    }

    fun getPickUpJobsList(uid: String,context:Context) {

        var finalJobID:String?=null

        println("uid----$uid")
        firebaseFirestore.collection(Constants.PICKUP_JOB_TABLE_NAME)
            .whereEqualTo("facilityRef", "3q1YPzcGJmVufeVR9kTPyB").get()
            .addOnCompleteListener { task ->

                if (task.isSuccessful) {

                    val pickupJobList = ArrayList<PickupJobs>()

                    task.result?.documents?.forEach {
                        println("data " + it.data)
                        var pickupJobs = it.toObject(PickupJobs::class.java)
                        if (pickupJobs != null) {
                            pickupJobList.add(pickupJobs)
                            finalJobID=pickupJobs.id
                        }
                    }
                    CoroutineScope(Dispatchers.IO).launch {

                        val response= finalJobID?.let { Api.retrofitService.patchPickUpjob(it) }

                        Log.d("Data", response.toString())
                    }


                    Log.d("Data", pickupJobList.toString())
                } else {

                    Log.d("Error", task.exception.toString())
                }

            }
    }


    fun getUserDetails(uid: String) {
        firebaseFirestore.collection(Constants.USERS_TABLE_NAME).document().get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    task.result?.data?.toString()?.let { Log.d("Data", it) }

                } else {

                    Log.d("Error", task.exception.toString())
                }

            }
    }


}