#!/usr/bin/env bash

git fetch && \
git reset --hard origin/master

docker-compose stop
docker rmi top.techial/knowledge/api:latest
mvn clean install package docker:build -DskipTests=true
docker-compose up -d
