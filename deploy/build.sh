#!/usr/bin/env bash

chmod +x ./gradlew && ./gradlew clean yarnBuild jibDockerBuild
docker tag techial.top/knowledge:latest docker.pkg.github.com/techial1042/knowledge/api:latest
docker push docker.pkg.github.com/techial1042/knowledge/api:latest
