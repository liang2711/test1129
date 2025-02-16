package com.zywl.test1229.bean

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class User(
    val name:String,
    val age:Int
):Parcelable
