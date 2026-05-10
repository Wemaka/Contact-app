plugins {
    id("common-convention")
    alias(libs.plugins.ksp)
}

androidConfig {}

dependencies {
    implementation(project(":core:common"))
    implementation(libs.room.runtime)
    ksp(libs.room.compiler)
}