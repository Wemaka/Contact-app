package features.contacts.impl.components

import MediumBoxShape
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil3.compose.SubcomposeAsyncImage
import components.DefaultAvatar
import components.ImageError
import space12
import space16
import space8
import theme.ContactsAppTheme

@Composable
fun ContactItem(
    id: String,
    name: String,
    phoneNumber: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    photoUri: String? = null,
    shape: Shape = MediumBoxShape
) {
    Row(
        modifier = modifier
            .clip(shape)
            .clickable {
                onClick()
            }
            .background(
                color = MaterialTheme.colorScheme.surfaceContainerHighest,
                shape = shape
            )
            .fillMaxWidth()
            .padding(
                horizontal = space16,
                vertical = space8
            ),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(space12)
    ) {
        SubcomposeAsyncImage(
            modifier = Modifier
                .clip(CircleShape)
                .size(48.dp),
            model = photoUri,
            contentDescription = null,
            contentScale = ContentScale.Crop,
            loading = {
                CircularProgressIndicator(
                    modifier = Modifier.padding(space16)
                )
            },
            error = {
                DefaultAvatar(
                    id = id,
                    name = name,
                    size = 48.dp,
                )
            }
        )

        Column(
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = name,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
            )

            Text(
                text = phoneNumber,
                maxLines = 1
            )
        }
    }
}

@Preview
@Composable
private fun Preview() {
    ContactsAppTheme() {
        ContactItem(
            id = "123",
            name = "Name",
            phoneNumber = "89996665544",
            photoUri = "",
            onClick = {}
        )
    }
}
