@echo off

rem Linux OS
docker run -it java-load-test-app:v20191024.1628 /bin/bash

rem Alpine OS
docker run -it java-load-test-app:v20191024.1628 sh
