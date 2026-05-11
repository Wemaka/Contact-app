package components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.sp
import utils.generateColorFromId

@Composable
fun DefaultAvatar(
    id: String,
    name: String?,
    size: Dp,
    modifier: Modifier = Modifier
) {
    val letter = remember(name) {
        name?.firstOrNull()?.uppercase() ?: "?"
    }

    val color = remember(id) {
        generateColorFromId(id)
    }

    Box(
        modifier = modifier
            .size(size)
            .background(color),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = letter,
            fontWeight = FontWeight.SemiBold,
            fontSize = (size.value / 2).sp,
            color = Color.White.copy(alpha = 0.8f)
        )
    }
}