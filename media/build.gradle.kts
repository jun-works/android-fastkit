plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    `maven-publish`
}

android {
    namespace = "com.junworks.fastkit.media"
    compileSdk = 35

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
    publishing {
        singleVariant("release") {
            withSourcesJar()
            withJavadocJar()
        }
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

// 배포 설정
afterEvaluate {
    publishing {
        publications {
            create<MavenPublication>("release") {
                from(components["release"])

                // 그룹 ID는 보통 com.github.유저명.레포명 형식이 되지만,
                // Jitpack은 artifactId를 기준으로 모듈을 구분합니다.
                groupId = "com.github.jun-works.android-fastkit"

                artifactId = "media"

                version = "1.0.0"
            }
        }
    }
}