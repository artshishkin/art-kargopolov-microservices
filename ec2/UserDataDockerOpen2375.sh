#!/bin/bash
yum update -y
amazon-linux-extras install -y docker
service docker start
usermod -a -G docker ec2-user
chkconfig docker on
mkdir -p /etc/systemd/system/docker.service.d
echo "[Service]
        ExecStart=
        ExecStart=/usr/bin/dockerd -H unix:// -H tcp://0.0.0.0:2375" >/etc/systemd/system/docker.service.d/options.conf
systemctl daemon-reload
systemctl restart docker