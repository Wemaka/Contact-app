package features.contacts.impl

import android.Manifest
import androidx.lifecycle.viewModelScope
import com.wemaka.data.model.DuplicateContactResult
import com.wemaka.domain.DeleteDuplicateUseCase
import com.wemaka.domain.GetContactsUseCase
import features.common.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class ContactsViewModel(
    private val getContactsUseCase: GetContactsUseCase,
    private val deleteDuplicateUseCase: DeleteDuplicateUseCase
) : BaseViewModel() {
    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _deleteState = MutableStateFlow<DuplicateContactResult?>(null)
    val deleteState = _deleteState.asStateFlow()

    private val isRead = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val groupedContacts = isRead
        .filter { it }
        .flatMapLatest { getContactsUseCase() }
        .map { contacts ->
            contacts.groupBy { it.name.first().uppercase() }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000L),
            initialValue = emptyMap()
        )

    fun hasPermission(permission: String, isGranted: Boolean) {
        onPermissionResult(
            permission = permission,
            isGranted = isGranted
        )

        if (permission == Manifest.permission.READ_CONTACTS) {
            isRead.value = isGranted
        }
    }

    fun onShowContacts() {
        isRead.value = true
    }

    fun onDeleteContactClick() {
        requestPermissions(
            permissions = listOf(
                Manifest.permission.WRITE_CONTACTS
            )
        ) {
            viewModelScope.launch {
                _loading.value = true
                _deleteState.value = deleteDuplicateUseCase()
                _loading.value = false
            }
        }
    }
}
