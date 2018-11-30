#!/bin/bash
echo '** Stopping spotapp **'
docker stop spotapp-front

echo '** Removing container **'
docker rm spotapp-front

echo '** Removing image **'
docker rmi spotapp-front

echo '** Building spotapp image **'
docker build -t spotapp-front -f Dockerfile-frontend-build .

echo '** Starting spotapp container **'
docker run \
        --mount type=bind,source="$(pwd)"/frontend,target=/home/spotapp/frontend \
        --name spotapp-front \
        -it spotapp-front
