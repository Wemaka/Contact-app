package features.contacts.impl.components

import MediumBoxShape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.pluralStringResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.wemaka.contactsapp.uikit.R
import com.wemaka.data.model.Contact
import com.wemaka.data.model.DeleteResult
import com.wemaka.data.model.DuplicateContactResult
import components.ButtonWithLoader
import kotlinx.coroutines.launch
import space16
import space8

@Composable
fun ContactsContent(
    contacts: Map<String, List<Contact>>,
    isLoading: Boolean,
    deleteState: DuplicateContactResult?,
    onCallClick: (phone: String) -> Unit,
    onDeleteDuplicateClick: () -> Unit
) {
    val scope = rememberCoroutineScope()
    var deleteAlert by remember { mutableStateOf(false) }
    val snackBarHostState = remember { SnackbarHostState() }

    deleteState?.let {
        val message = if (it.result == DeleteResult.ERROR) {
            stringResource(R.string.duplicate_delete_error)
        } else {
            if (it.deleteCount == 0) {
                stringResource(R.string.duplicate_not_found)
            } else {
                pluralStringResource(
                    R.plurals.deleted_count_contacts, it.deleteCount, it.deleteCount
                )
            }
        }

        scope.launch {
            snackBarHostState.showSnackbar(
                message = message,
                duration = SnackbarDuration.Short
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        snackbarHost = {
            SnackbarHost(snackBarHostState)
        },
        bottomBar = {
            ButtonWithLoader(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = space16)
                    .padding(top = space8),
                text = stringResource(R.string.button_delete_duplicate),
                onClick = { deleteAlert = true },
                height = 56.dp,
                isLoading = isLoading,
                isEnabled = true
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier.padding(innerPadding)
        ) {
            ContactsList(
                contacts = contacts,
                onClick = {
                    onCallClick(it)
                }
            )
        }
    }

    if (deleteAlert) {
        AlertDialog(
            confirmButton = {
                FilledTonalButton(
                    onClick = {
                        onDeleteDuplicateClick()
                        deleteAlert = false
                    }
                ) {
                    Text(
                        text = stringResource(R.string.alert_delete_confirm)
                    )
                }
            },
            onDismissRequest = { deleteAlert = false },
            dismissButton = {
                Button(
                    onClick = { deleteAlert = false }
                ) {
                    Text(
                        text = stringResource(R.string.alert_delete_dismiss)
                    )
                }
            },
            title = {
                Text(
                    text = stringResource(R.string.alert_delete_title)
                )
            },
            text = {
                Text(
                    text = stringResource(R.string.alert_delete_description)
                )
            },
            shape = MediumBoxShape
        )
    }
}