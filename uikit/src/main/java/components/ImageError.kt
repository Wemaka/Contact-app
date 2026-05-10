package components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ImageError(
    modifier: Modifier = Modifier,
    shape: Shape = CircleShape,
    borderWidth: Dp = 1.dp
) {
    Box(
        modifier = modifier
            .background(
                color = MaterialTheme.colorScheme.surface,
                shape = shape
            )
            .clip(shape)
            .border(
                width = borderWidth,
                color = MaterialTheme.colorScheme.outline,
                shape = shape
            )
    )
}