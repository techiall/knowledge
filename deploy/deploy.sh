#!/usr/bin/env bash

git fetch && \
git reset --hard origin/master

./mvnw clean install -DskipTests=true && \
cd ./api && \
../mvnw compile jib:dockerBuild -DskipTests=true && \
docker-compose up -d
docker images | awk '$1 == "<none>" || $2 == "<none>" {print $3}' | xargs docker rmi

docker volume create --name=web_data
docker volume create --name=mysql_data
docker volume create --name=neo4j_logs
docker volume create --name=neo4j_conf
docker volume create --name=neo4j_import
docker volume create --name=neo4j_plugins
