#!/bin/bash
yum update -y
amazon-linux-extras install -y docker
service docker start
usermod -a -G docker ec2-user
chkconfig docker on
docker run -d --restart unless-stopped -p 8011:8011 -e SPRING_CLOUD_CONFIG_URI=http://172.31.38.141:8012 -e SPRING_PROFILES_ACTIVE=asymmetric,aws artarkatesoft/photo-app-api-gateway