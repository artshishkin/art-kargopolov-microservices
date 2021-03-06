#!/bin/bash
yum update -y
amazon-linux-extras install -y docker
service docker start
usermod -a -G docker ec2-user
chkconfig docker on

docker run -d --restart unless-stopped \
    -e SPRING_CLOUD_CONFIG_URI=http://172.31.38.141:8012 \
    -e SPRING_PROFILES_ACTIVE=mysql,asymmetric,aws \
    -e "logging.level.net.shyshkin=debug" \
    -v /home/ec2-user/api-logs:/application/logs \
    --network host \
    artarkatesoft/photo-app-api-users


mkdir /home/ec2-user/pipeline

echo \
'input {
  file {
    type => "users-ws-log"
    path => "/var/log/users-ws.log"
  }
}
output {
  if [type] == "users-ws-log" {
      elasticsearch {
        hosts => ["172.31.47.170:9200"]
        index => "users-ws-%{+YYYY.MM.dd}"
      }
  }
  stdout { codec => rubydebug }

}' \
> /home/ec2-user/pipeline/logstash-users.conf

docker run -d --restart unless-stopped -p 9600:9600 \
    -v /home/ec2-user/api-logs:/var/log/ \
    -v /home/ec2-user/pipeline:/usr/share/logstash/pipeline/ \
    docker.elastic.co/logstash/logstash:7.10.1
