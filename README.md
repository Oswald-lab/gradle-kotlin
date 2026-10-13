# Gradle (Kotlin DSL) Text Utils

Requires JDK 17+ and Gradle 8+ installed.

If there is no `gradlew` file yet, generate the wrapper once:

    gradle wrapper

Then:

    ./gradlew test                       # run tests
    ./gradlew run                        # run with default text
    ./gradlew run --args="Hello world"   # run with your own text
    ./gradlew build                      # build build/libs/*.jar
