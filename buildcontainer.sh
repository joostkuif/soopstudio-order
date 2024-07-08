#!/bin/bash
./mvnw package -Dnative -Dquarkus.native.container-build=true -Dquarkus.container-image.build=true
docker build -f src/main/docker/Dockerfile.native-micro -t soopstudio/order .
docker tag soopstudio/order:latest jkuif/soopstudio-order:0.0.1
docker push jkuif/soopstudio-order:0.0.1

echo "docker run -d --name soopstudio-order -e TZ=Europe/Amsterdam --detach --restart unless-stopped -p 8080:8080 soopstudio/order"