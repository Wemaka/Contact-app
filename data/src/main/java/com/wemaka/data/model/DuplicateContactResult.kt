package com.wemaka.data.model

import com.wemaka.aidl.DuplicateContactResultAidl

data class DuplicateContactResult(
    val result: DeleteResult,
    val deleteCount: Int
)

internal fun DuplicateContactResultAidl.toExternal() = DuplicateContactResult(
    result = DeleteResult.valueOf(result.name),
    deleteCount = deleteCount
)
