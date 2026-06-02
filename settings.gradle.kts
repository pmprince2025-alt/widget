pluginManagement {
    repositories {
        google()
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

rootProject.name = "WidgetKit"

include(":app")
include(":feature:gallery")
include(":feature:config")
include(":feature:mywidgets")
include(":data")
include(":domain")
include(":core:ui")
include(":widget:clock")
include(":widget:note")
include(":widget:countdown")
