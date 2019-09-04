#!/bin/bash
mvn compile sonar:sonar -Dsonar.login=${SONARCLOUD_TOKEN} -Dsonar.host.url=https://sonarcloud.io -Dsonar.projectKey=TheBusyBiscuit_CS-CoreLib2 -Dsonar.organization=thebusybiscuit-github
