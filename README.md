## `Cats`

### Overview

I wrote this app to refamiliarize myself with Java web applications.

It uses [The Cat API](https://thecatapi.com/) to view cat photos in different ways:
  * random 
    ![View random cat screenshot](images/random.png)
  * search 
    ![Search cats screenshot](images/search.png)

### Technologies

  * Spring Boot (application framework)
  * Webflux (REST)
  * Jackson (JSON)
  * Freemarker (templating)

### How to run

There are 2 ways to run the application:

1. Natively
2. In a Docker container

#### 1. Run natively

To run in a native environment, **ensure OpenJDK 17+ is installed**.

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

#### 2. Run in a Docker container

To run in a Docker container, **ensure you have Docker installed**.

Then, in the project root directory:

1. Build the Docker image:

```shell
cats  $ docker build --rm -t chen.eric/cats:latest .
```

2. Run the Docker container:

```shell
cats  $ docker run -dp 8080:8080 --rm --name cats chen.eric/cats:latest 
```

3. In a browser, open http://localhost:8080/
