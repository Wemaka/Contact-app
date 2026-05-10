package utils

import androidx.compose.ui.graphics.Color
import kotlin.random.Random


fun generateColorFromId(id: String): Color {
    val random = Random(id.hashCode())
    val hue = random.nextInt(360).toFloat()
    val saturation = 0.5f + random.nextFloat() * 0.1f  // насыщенность
    val value = 0.6f + random.nextFloat() * 0.1f      // яркость

    return Color.hsv(hue, saturation, value)
}