package components

import android.content.Context
import com.wemaka.contactsapp.uikit.R

interface PermissionTextProvider {
    fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String
}

class ReadContactsTextProvider : PermissionTextProvider {
    override fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            context.getString(R.string.read_contacts_declined)
        } else {
            context.getString(R.string.read_contacts_description)
        }
    }
}

class WriteContactsTextProvider : PermissionTextProvider {
    override fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            context.getString(R.string.write_contacts_declined)
        } else {
            context.getString(R.string.write_contacts_description)
        }
    }
}

class CallPhoneTextProvider : PermissionTextProvider {
    override fun getDescription(context: Context, isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            context.getString(R.string.call_phone_declined)
        } else {
            context.getString(R.string.call_phone_description)
        }
    }
}