package com.fulfillment.pickupkjobapplication.network

import android.os.Parcel
import android.os.Parcelable

data class Action(
    private var action: String? = null, private var status: String? = null
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString()!!,
        parcel.readString()!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(action)
        parcel.writeString(status)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Action> {
        override fun createFromParcel(parcel: Parcel): Action {
            return Action(parcel)
        }

        override fun newArray(size: Int): Array<Action?> {
            return arrayOfNulls(size)
        }
    }
}
