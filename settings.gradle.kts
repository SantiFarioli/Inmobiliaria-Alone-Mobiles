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

        //  Repositorio JitPack (necesario para SweetAlert)
        maven(url = "https://jitpack.io")
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

        //  Repositorio JitPack (necesario para SweetAlert)
        maven(url = "https://jitpack.io")
    }
}

rootProject.name = "Inmobiliaria Alone"
include(":app")
