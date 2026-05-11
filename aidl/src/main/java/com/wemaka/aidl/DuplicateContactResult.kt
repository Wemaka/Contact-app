package com.wemaka.aidl

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DuplicateContactResultAidl(
    val result: DeleteResultAidl,
    val deleteCount: Int
) : Parcelable