package com.wemaka.aidl

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
enum class DeleteResultAidl : Parcelable {
    SUCCESS,
    ERROR,
    NOT_FOUND
}