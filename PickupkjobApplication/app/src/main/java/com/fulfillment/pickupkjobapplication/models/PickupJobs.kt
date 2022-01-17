package com.fulfillment.pickupkjobapplication.models

import android.os.Parcel
import android.os.Parcelable
import java.util.*

data class PickupJobs(
    var channel: String? = null,
    var created: Date? = null,
    var facilityRef: String? = null,
    var hasScannableCodes: Boolean = false,
    var id: String? = null,
    var labelStatus: String? = null,
    var lastModificationDate: Date? = null,
    var lastModified: Date? = null,
    var orderDate: Date? = null,
    var orderedArticleCount: Int = 0,
    var pickedArticleCount: Int = 0,
    var priority: String? = null,
    var shortId: String? = null,
    var status: String? = null,
    var targetTime: Date? = null,
    var targetVersion: Int = 0,
    var tenantOrderId: String? = null,
    var version: Int = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        Date(parcel.readLong()),
        parcel.readString(),
        parcel.readByte() != 0.toByte(),
        parcel.readString(),
        parcel.readString(),
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        Date(parcel.readLong()),
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        Date(parcel.readLong()),
        parcel.readInt(),
        parcel.readString(),
        parcel.readInt()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(channel)
        created?.let { parcel.writeLong(it?.time) }
        parcel.writeString(facilityRef)
        parcel.writeByte(if (hasScannableCodes) 1 else 0)
        parcel.writeString(id)
        parcel.writeString(labelStatus)
        lastModificationDate?.let { parcel.writeLong(it.time) }
        lastModified?.let { parcel.writeLong(it.time) }
        orderDate?.let { parcel.writeLong(it.time) }
        parcel.writeInt(orderedArticleCount)
        parcel.writeInt(pickedArticleCount)
        parcel.writeString(priority)
        parcel.writeString(shortId)
        parcel.writeString(status)
        targetTime?.let { parcel.writeLong(it.time) }
        parcel.writeInt(targetVersion)
        parcel.writeString(tenantOrderId)
        parcel.writeInt(version)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PickupJobs> {
        override fun createFromParcel(parcel: Parcel): PickupJobs {
            return PickupJobs(parcel)
        }

        override fun newArray(size: Int): Array<PickupJobs?> {
            return arrayOfNulls(size)
        }
    }

    fun toPickjobUiModel() =
        PickjobUiModel(id = "$id", status = "$status", version = version)
}

