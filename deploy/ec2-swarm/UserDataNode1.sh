#!/bin/bash
yum update -y
amazon-linux-extras install -y docker
service docker start
usermod -a -G docker ec2-user
chkconfig docker on

docker swarm init --advertise-addr eth0:2377

docker service create \
--name portainer \
--publish 9000:9000 \
--constraint 'node.role == manager' \
--mount type=bind,src=/var/run/docker.sock,dst=/var/run/docker.sock \
portainer/portainer \
-H unix:///var/run/docker.sock



