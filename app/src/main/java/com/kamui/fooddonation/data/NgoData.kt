package com.kamui.fooddonation.data

import android.os.Parcel
import android.os.Parcelable
import com.google.firebase.firestore.GeoPoint

data class NgoData(
    val role: String? ="",
    var ngoId: String? ="",
    val fcmToken: String? = "",
    val rewardPoint:String? ="",
    var imageUri:String? ="",
    val name: String? ="",
    val email: String? ="",
    val address: String? ="",
    val location: GeoPoint? = null,
    val ngoAddress: String? = "",
    val phone: String? ="",
    val ngoName: String? ="",
    val foodType: String? ="",
    val otherType: String? = "",
    val organizationType: String? ="",
    val foodPickup: String? ="",
    val otherPickup: String? ="",
    val registration: String? ="",
    val certificate: String? ="",
    val password: String? ="",

    ): Parcelable
{
    constructor(parcel: Parcel):this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readParcelable(GeoPoint::class.java.classLoader),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(role)
        parcel.writeString(ngoId)
        parcel.writeString(fcmToken)
        parcel.writeString(rewardPoint)
        parcel.writeString(imageUri)
        parcel.writeString(name)
        parcel.writeString(email)
        parcel.writeString(address)
        parcel.writeValue(location)
        parcel.writeString(ngoAddress)
        parcel.writeString(phone)
        parcel.writeString(ngoName)
        parcel.writeString(foodType)
        parcel.writeString(otherType)
        parcel.writeString(organizationType)
        parcel.writeString(foodPickup)
        parcel.writeString(otherPickup)
        parcel.writeString(registration)
        parcel.writeString(certificate)
        parcel.writeString(password)

    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NgoData> {
        override fun createFromParcel(parcel: Parcel): NgoData {
            return NgoData(parcel)
        }

        override fun newArray(size: Int): Array<NgoData?> {
            return arrayOfNulls(size)
        }
    }

}

