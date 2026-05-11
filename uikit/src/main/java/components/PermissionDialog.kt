package components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import com.wemaka.contactsapp.uikit.R
import theme.ContactsAppTheme

@Composable
fun PermissionDialog(
    permissionTextProvider: PermissionTextProvider,
    isPermanentlyDeclined: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    AlertDialog(
        title = {
            Text(
                text = stringResource(R.string.alert_permission_title)
            )
        },
        text = {
            Text(
                text = permissionTextProvider.getDescription(context, isPermanentlyDeclined)
            )
        },
        confirmButton = {
            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = {
                    if (isPermanentlyDeclined) {
                        onGoToAppSettingsClick()
                    } else {
                        onConfirm()
                    }
                }
            ) {
                Text(
                    text = if (isPermanentlyDeclined) {
                        stringResource(R.string.grant_permission)
                    } else {
                        stringResource(R.string.confirm)
                    },
                    fontWeight = FontWeight.Bold
                )
            }
        },
        onDismissRequest = onDismiss,
        modifier = modifier
    )
}

@Preview
@Composable
private fun Preview() {
    ContactsAppTheme() {
        PermissionDialog(
            permissionTextProvider = ReadContactsTextProvider(),
            isPermanentlyDeclined = false,
            onDismiss = {},
            onConfirm = {},
            onGoToAppSettingsClick = {},
        )
    }
}