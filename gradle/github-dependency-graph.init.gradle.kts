initscript {
    repositories {
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.gradle:github-dependency-graph-gradle-plugin:1.4.1")
    }
}

apply<org.gradle.github.dependencygraph.GitHubDependencyGraphPlugin>()
