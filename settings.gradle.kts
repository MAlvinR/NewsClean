pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

rootProject.name = "News Clean"
include(":app")
include(":core:network")
include(":core:data")
include(":core:domain")
include(":feature:home")
include(":feature:detail-article")
include(":feature:category")
include(":feature:sources")
include(":feature:list-article")
