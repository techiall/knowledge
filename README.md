# Knowledge-Grpah

knowledge graph system.



### Prerequisites 

*  [Apache Maven](https://maven.apache.org/)
* [OpenJDK](https://openjdk.java.net/)
* [Mysql 8.0](https://www.mysql.com/)
* [Neo4j](https://neo4j.com/)



### Installing

1. git clone and enter dir `git clone https://github.com/techial1042/knowledge.git && cd knowledge`
2. build with maven (`mvn clean package -DskipTests=true`)



## Running

```shell
java -jar api/target/api-0.1.0.jars
```



## Deployment

### docker (recommend)

```shell
cd ./api

mvn clean compile jib:dockerBuild -DskipTests=true

docker run -d top.techial/knowledge/api:latest
```

### jar

```shell
java -jar api/target/api-0.1.0.jarss
```

