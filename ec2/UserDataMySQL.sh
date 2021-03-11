#!/bin/bash
yum update -y
amazon-linux-extras install -y docker
service docker start
usermod -a -G docker ec2-user
chkconfig docker on

docker run -d --restart unless-stopped -p 23306:3306 \
    --name photo-app-api \
    -e MYSQL_RANDOM_ROOT_PASSWORD=yes \
    -e MYSQL_USER=photo_app_user \
    -e MYSQL_PASSWORD=photo_app_password \
    -e MYSQL_DATABASE=photo_app_db \
    -v photo-app-volume:/var/lib/mysql \
    mysql:8.0.23

