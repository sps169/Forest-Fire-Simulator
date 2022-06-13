buildscript {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.6.10")
        classpath("com.android.tools.build:gradle:7.0.0")
        classpath ("com.google.maps.android:maps-compose:1.0.0")
        classpath ("com.google.android.gms:play-services-maps:18.0.2")
    }
}

group = "me.spste"
version = "1.0"

allprojects {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
    }
}