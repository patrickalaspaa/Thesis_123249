#!/bin/bash
echo '** Stopping spotapp **'
docker stop spotapp-builder

echo '** Removing container **'
docker rm spotapp-builder

echo '** Removing image **'
docker rmi spotapp-builder

echo '** Building spotapp image **'
docker build -t spotapp-builder -f Dockerfile-build .

echo '** Starting spotapp container **'
docker run \
        --mount type=bind,source="$(pwd)"/backend,target=/home/spotapp \
        --name spotapp-builder \
        spotapp-builder
