#!/usr/bin/env bash

git fetch && \
git reset --hard origin/master

mvn clean install package docker:build -DskipTests=true
docker-compose up -d
docker images | awk '$1 == "<none>" || $2 == "<none>" {print $3}' | xargs docker rmi
