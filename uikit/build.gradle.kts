plugins {
    id("compose-convention")
    alias(libs.plugins.kotlin.compose)
}

androidConfig {}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
    implementation(libs.androidx.compose.foundation.layout)
}