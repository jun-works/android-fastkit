plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    id("com.vanniktech.maven.publish")
    id("signing")
}

mavenPublishing {
    // 배포할 좌표 설정
    coordinates("io.github.jun-works", "android-fastkit-media", "1.0.0")

    pom {
        name.set("FastKit Media")
        description.set("A fast media library for Android")
        inceptionYear.set("2024")
        url.set("https://github.com/jun-works/android-fastkit") // 깃허브 주소

        licenses {
            license {
                name.set("The Apache License, Version 2.0")
                url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                distribution.set("repo")
            }
        }
        developers {
            developer {
                id.set("jun-works")
                name.set("Jun Works")
                url.set("https://github.com/jun-works")
            }
        }
        scm {
            url.set("https://github.com/jun-works/android-fastkit")
            connection.set("scm:git:git://github.com/jun-works/android-fastkit.git")
            developerConnection.set("scm:git:ssh://git@github.com/jun-works/android-fastkit.git")
        }
    }

    // 배포 타겟: Maven Central (Sonatype)
    publishToMavenCentral(com.vanniktech.maven.publish.SonatypeHost.CENTRAL_PORTAL)
}

android {
    namespace = "com.junworks.fastkit.media"
    compileSdk = 34

    defaultConfig {
        minSdk = 24

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val media3Version = "1.5.0"
    api("androidx.media3:media3-exoplayer:$media3Version")
    api("androidx.media3:media3-ui:$media3Version")

    // 기타 필요한 모듈들
    implementation("androidx.media3:media3-exoplayer-hls:${media3Version}") // 이건 내부동작용이라 implementation
}