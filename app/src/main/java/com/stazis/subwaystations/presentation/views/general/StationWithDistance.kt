package com.stazis.subwaystations.presentation.views.general

import android.os.Parcel
import android.os.Parcelable

class StationWithDistance(val name: String, val distance: Int) : Parcelable {

    companion object CREATOR : Parcelable.Creator<StationWithDistance> {

        override fun createFromParcel(parcel: Parcel) = StationWithDistance(parcel)

        override fun newArray(size: Int) = arrayOfNulls<StationWithDistance?>(size)
    }

    constructor(parcel: Parcel) : this(parcel.readString()!!, parcel.readInt())

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeInt(distance)
    }

    override fun describeContents() = 0
}