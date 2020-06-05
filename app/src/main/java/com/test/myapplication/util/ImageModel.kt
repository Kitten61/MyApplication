package com.test.myapplication.util

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable

data class ImageModel(val name: String?, val description: String?, val id: Int) : Parcelable {
    lateinit var bitmap: Bitmap
    lateinit var base64: String

    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
        bitmap = parcel.readParcelable(Bitmap::class.java.classLoader)!!
        base64 = parcel.readString()!!
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeInt(id)
        parcel.writeParcelable(bitmap, flags)
        parcel.writeString(base64)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ImageModel> {
        override fun createFromParcel(parcel: Parcel): ImageModel {
            return ImageModel(parcel)
        }

        override fun newArray(size: Int): Array<ImageModel?> {
            return arrayOfNulls(size)
        }
    }


}