// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.android.library) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.hilt) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.ktlint) apply false
}

allprojects {
    apply(plugin = "org.jlleitschuh.gradle.ktlint")

    extensions.configure<org.jlleitschuh.gradle.ktlint.KtlintExtension> {
      version.set("1.2.1")
      android.set(true)
      ignoreFailures.set(true)
      verbose.set(true)
      outputToConsole.set(true)
      outputColorName.set("RED")

      filter {
        include("**/src/main/kotlin/**/*.kt")
        exclude("**/src/test/kotlin/**/*.kt")
        exclude("**/build/**")
      }

      reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.HTML)
      }
    }
}