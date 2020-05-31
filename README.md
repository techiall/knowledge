# Knowledge-Graph

Knowledge graph system.

This my graduation project.


### Prerequisites 

*  [Gradle 6.22](https://gradle.org/)
* [OpenJDK 11](https://openjdk.java.net/)
* [Mysql 8.0](https://www.mysql.com/)
* [redis](https://redis.io/)
* [Elasticsearch](https://www.elastic.co/)
* [Node 12.14](https://nodejs.org/)
* [yarn](https://yarnpkg.com/)


### Installing

* git clone and enter dir `git clone https://github.com/techial1042/knowledge.git && cd knowledge`



## Running

```shell

chmod +x ./gradlew

./gradlew clean yarnBuild build -x test

java -jar ./build/libs/knolwedge-0.1.0.jar

```


## Deployment

### docker (recommend)


```shell

gradle clean yarnBuild jibDockerBuild

docker run -d techial.top/knowledge:latest

```

Or enter project dir, running `docker-compose up -d`


### jar

```shell
java -jar ./build/libs/knolwedge-0.1.0.jar
```

