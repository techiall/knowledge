#!/usr/bin/env bash

git fetch && \
git reset --hard origin/master

./mvnw clean install package -DskipTests=true
cd ./api
../mvnw clean install package jib:dockerBuild -DskipTests=true
docker-compose up -d
docker images | awk '$1 == "<none>" || $2 == "<none>" {print $3}' | xargs docker rmi