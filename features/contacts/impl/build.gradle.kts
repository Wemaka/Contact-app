plugins {
    id("feature-impl-convention")
    alias(libs.plugins.kotlin.compose)
}

androidConfig {}

dependencies {
    implementation(project(":data"))
    implementation(project(":domain"))
    implementation(project(":features:contacts:api"))
    implementation(project(":features:common"))
    implementation(project(":uikit"))

    implementation(libs.coil.compose)
    implementation(libs.coil.network.okhttp)
}