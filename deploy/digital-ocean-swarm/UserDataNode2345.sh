#!/bin/bash

ufw allow in on eth1 to any port 2377 proto tcp
ufw allow in on eth1 to any port 7946
ufw allow in on eth1 to any port 4789 proto udp

# You need to SSH into node1 and `docker swarm join-token worker` to get join-token
docker swarm join --token SWMTKN-1-0vxocicdheh3smn1seebhjtd2hpdivbs9r1p39wcdhr6pgyb08-0n0by6c2ne8w6sydfqvwyk2mm 10.114.16.2:2377