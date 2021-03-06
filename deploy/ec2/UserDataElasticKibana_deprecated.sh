#!/bin/bash
yum update -y
amazon-linux-extras install -y docker
service docker start
usermod -a -G docker ec2-user
chkconfig docker on

docker run -d --restart unless-stopped -p 9200:9200 -p 9300:9300 -v esdata1:/usr/share/elasticsearch/data -e "discovery.type=single-node" --name elastic docker.elastic.co/elasticsearch/elasticsearch:7.10.1
docker run -d --restart unless-stopped --link elastic:elasticsearch -p 5601:5601 docker.elastic.co/kibana/kibana:7.10.1
