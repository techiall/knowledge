#!/usr/bin/env bash

git fetch && \
git reset --hard origin/master

chmod +x ./gradlew && \
./gradlew clean yarnBuild jibDockerBuild
docker-compose up -d
docker images | awk '$1 == "<none>" || $2 == "<none>" {print $3}' | xargs docker rmi

echo "deploy success"
