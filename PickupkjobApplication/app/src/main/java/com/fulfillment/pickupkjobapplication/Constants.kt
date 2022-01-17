package com.fulfillment.pickupkjobapplication

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object Constants {

    const val PICKUP_JOB_TABLE_NAME="mobile_android-picking-v2-pickjobs"
    const val USERS_TABLE_NAME="api-users-v1-users"
    const val STATUS_OPEN="OPEN"
    const val STATUS_CLOSED="CLOSED"


    var USER_TOKEN=""


    fun isNetworkAvailable(context:Context):Boolean
    {
        val connectivityManager=context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

         if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M)
         {
             val network=connectivityManager.activeNetwork ?: return false

             val activeNetwork=connectivityManager.getNetworkCapabilities(network) ?: return false

             return when{
                 activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                 activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                 activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                 else-> false
             }

         }else
         {
             val networkInfo=connectivityManager.activeNetworkInfo
             return networkInfo !=null && networkInfo.isConnectedOrConnecting
         }
    }




}