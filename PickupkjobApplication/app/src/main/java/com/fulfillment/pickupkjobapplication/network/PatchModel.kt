package com.fulfillment.pickupkjobapplication.network

import android.os.Parcel
import android.os.Parcelable
import java.util.ArrayList

data class PatchModel(
    var version: Int, var actions: ArrayList<Action>?
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.createTypedArrayList(Action.CREATOR)
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(version)
        parcel.writeTypedList(actions)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PatchModel> {
        override fun createFromParcel(parcel: Parcel): PatchModel {
            return PatchModel(parcel)
        }

        override fun newArray(size: Int): Array<PatchModel?> {
            return arrayOfNulls(size)
        }
    }
}