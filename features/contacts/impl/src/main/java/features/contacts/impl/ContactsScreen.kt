package features.contacts.impl

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.net.toUri
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import components.CallPhoneTextProvider
import components.PermissionDialog
import components.ReadContactsTextProvider
import components.WriteContactsTextProvider
import features.common.PermissionRequest
import features.common.openAppSettings
import features.contacts.impl.components.ContactsContent
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun ContactsScreen() {
    val viewModel: ContactsViewModel = koinViewModel()

    val context = LocalContext.current
    val activity = context as? Activity
    var currentRequest by remember {
        mutableStateOf<PermissionRequest?>(null)
    }

    val multiplePermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions(),
        onResult = { result ->
            val allGranted = result.all { it.value }
            result.keys.forEach { perm ->
                viewModel.hasPermission(
                    permission = perm,
                    isGranted = result[perm] == true
                )
            }
            if (allGranted) {
                currentRequest?.onGranted?.invoke()
            }
        }
    )

    viewModel.visiblePermissionDialogQueue
        .reversed()
        .forEach { permission ->
            val isPermanentlyDeclined = activity?.let {
                !shouldShowRequestPermissionRationale(it, permission)
            } ?: false

            PermissionDialog(
                permissionTextProvider = when (permission) {
                    Manifest.permission.READ_CONTACTS -> ReadContactsTextProvider()
                    Manifest.permission.WRITE_CONTACTS -> WriteContactsTextProvider()
                    Manifest.permission.CALL_PHONE -> CallPhoneTextProvider()
                    else -> return@forEach
                },
                isPermanentlyDeclined = isPermanentlyDeclined,
                onDismiss = viewModel::dismissDialog,
                onConfirm = {
                    viewModel.dismissDialog()

                    multiplePermissionLauncher.launch(
                        arrayOf(permission)
                    )
                },
                onGoToAppSettingsClick = { context.openAppSettings() },
            )
        }

    LaunchedEffect(Unit) {
        viewModel.permissionRequests.collectLatest { request ->

            val deniedPermissions =
                request.permissions.filter { permission ->
                    ContextCompat.checkSelfPermission(
                        context,
                        permission
                    ) != PackageManager.PERMISSION_GRANTED
                }

            if (deniedPermissions.isEmpty()) {
                request.onGranted()
            } else {
                currentRequest = request

                multiplePermissionLauncher.launch(
                    deniedPermissions.toTypedArray()
                )
            }
        }
    }

    LaunchedEffect(Unit) {
        viewModel.onShowContacts()
    }

    ContactsContent(
        contacts = viewModel.groupedContacts.collectAsStateWithLifecycle().value,
        onCallClick = { phone ->
            Intent(Intent.ACTION_DIAL, "tel:$phone".toUri()).also {
                context.startActivity(it)
            }
        }
    )
}