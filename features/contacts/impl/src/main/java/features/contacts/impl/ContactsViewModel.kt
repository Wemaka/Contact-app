package features.contacts.impl

import android.Manifest
import androidx.lifecycle.viewModelScope
import com.wemaka.data.provider.ContactProvider
import features.common.BaseViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

class ContactsViewModel(
    private val contactsProvider: ContactProvider
) : BaseViewModel() {
    private val isRead = MutableStateFlow(false)

    @OptIn(ExperimentalCoroutinesApi::class)
    val groupedContacts = isRead
        .filter { it }
        .flatMapLatest { contactsProvider.contactsFlow() }
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
        requestPermissions(
            permissions = listOf(
                Manifest.permission.READ_CONTACTS
            )
        ) {
            isRead.value = true
        }
    }

    fun onDeleteContactClick() {
        requestPermissions(
            permissions = listOf(
                Manifest.permission.WRITE_CONTACTS
            )
        ) {
//            deleteContact()
        }
    }

    fun onCallClick() {
        requestPermissions(
            permissions = listOf(
                Manifest.permission.CALL_PHONE
            )
        ) {
//            startCall()
        }
    }
}