#!/bin/bash
yum update -y
amazon-linux-extras install -y docker
service docker start
usermod -a -G docker ec2-user
chkconfig docker on

docker run -d --restart unless-stopped \
    -e SPRING_CLOUD_CONFIG_URI=http://172.31.38.141:8012 \
    -e SPRING_PROFILES_ACTIVE=asymmetric,aws \
    -v /home/ec2-user/api-logs:/application/logs \
    -e "server.port=8989" \
    -p 8989:8989 \
    artarkatesoft/photo-app-api-albums