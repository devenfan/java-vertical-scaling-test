@echo off

rem Linux OS
docker run --rm -it -p 8080:8080 -p 8787:8787 java-load-test-app:java13-v20191104.1500 /bin/bash

rem Alpine OS
docker run --rm -it -p 8080:8080 -p 8787:8787 java-load-test-app:java13-v20191104.1500 sh
