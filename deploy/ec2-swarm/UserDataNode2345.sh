#!/bin/bash
yum update -y
amazon-linux-extras install -y docker
service docker start
usermod -a -G docker ec2-user
chkconfig docker on

# You need to SSH into node1 and `docker swarm join-token worker` to get join-token
docker swarm join --token SWMTKN-1-35tzt0vocmjs7fs8m6pze83udgzvdyeteveopyfjfyehylxtc2-69n9cg3pbowspyspoqdodnpjv 172.31.44.224:2377



