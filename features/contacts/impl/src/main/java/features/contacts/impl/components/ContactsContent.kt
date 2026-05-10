package features.contacts.impl.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.wemaka.data.model.Contact
import space12
import space16
import space20
import space24
import space4
import space6
import space8

@Composable
fun ContactsContent(
    contacts: Map<String, List<Contact>>,
    onCallClick: (phone: String) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(horizontal = space12),
        verticalArrangement = Arrangement.spacedBy(2.dp)
    ) {
        contacts.forEach { (letter, contacts) ->
            stickyHeader {
                Text(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(
                            horizontal = space20,
                            vertical = space6
                        ),
                    text = letter,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }

            itemsIndexed(
                items = contacts,
                key = { _, item -> item.id }
            ) { index, contact ->
                val isFirst = index == 0
                val isLast = index == contacts.size - 1
                val isBetween = !isFirst && !isLast
                val isOne = isFirst && isLast

                val topShape = if ((isLast || isBetween) && !isOne) space4 else space20
                val bottomShape = if ((isFirst || isBetween) && !isOne) space4 else space20

                ContactItem(
                    id = contact.id,
                    name = contact.name,
                    phoneNumber = contact.phoneNumber,
                    onClick = {
                        onCallClick(contact.phoneNumber)
                    },
                    photoUri = contact.photoUri,
                    shape = RoundedCornerShape(
                        topStart = topShape,
                        topEnd = topShape,
                        bottomStart = bottomShape,
                        bottomEnd = bottomShape
                    )
                )
            }
        }
    }
}