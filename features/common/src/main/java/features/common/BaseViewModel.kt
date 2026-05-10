package features.common

import androidx.compose.runtime.mutableStateListOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch

data class PermissionRequest(
    val permissions: List<String>,
    val onGranted: () -> Unit = {}
)

open class BaseViewModel : ViewModel() {
    private val _permissionRequests = MutableSharedFlow<PermissionRequest>(replay = 1)
    val permissionRequests = _permissionRequests.asSharedFlow()

    val visiblePermissionDialogQueue = mutableStateListOf<String>()

    fun requestPermissions(
        permissions: List<String>,
        onGranted: () -> Unit = {}
    ) {
        viewModelScope.launch {
            _permissionRequests.emit(
                PermissionRequest(
                    permissions = permissions,
                    onGranted = onGranted
                )
            )
        }
    }

    fun onPermissionResult(
        permission: String,
        isGranted: Boolean
    ) {
        if (!isGranted && !visiblePermissionDialogQueue.contains(permission)) {
            visiblePermissionDialogQueue.add(permission)
        }
    }

    fun dismissDialog() {
        if (visiblePermissionDialogQueue.isNotEmpty()) {
            visiblePermissionDialogQueue.removeAt(0)
        }
    }
}