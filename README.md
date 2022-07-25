## `Cats`

### Overview

I wrote this app to refamiliarize myself with Java web applications.

It uses [The Cat API](https://thecatapi.com/) to view cat photos in different ways:
  * random
  * search

### Technologies

  * Spring Boot (web framework)
  * Webflux (REST)
  * Jackson (JSON)
  * Freemarker (templating)

### How to run

**Ensure OpenJDK 18.0.1.1+ is installed.**

Then, in the project root directory:

1. Build the project:

```shell
cats  $ ./gradlew build
```

2. Run the project:

```shell
cats  $ java -jar build/libs/cats-0.0.1-SNAPSHOT.jar
```

3. In a browser, open http://localhost:8080/