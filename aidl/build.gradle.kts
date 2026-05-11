plugins {
    id("common-convention")
    id("kotlin-parcelize")
}

androidConfig {
    buildFeatures.aidl = true

    sourceSets.getByName("main") {
        aidl.directories.add("src/main/java")
    }
}

dependencies {
}
