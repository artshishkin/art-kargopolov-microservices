#  Linux Troubleshooting Commands

In the next lesson, you will see some Linux network troubleshooting.

Below are some helpful commands. This is for your reference. Docker Swarm initialization is covered in detail in the next lecture.

Problem encountered was that the Swarm nodes could not communincate.

1.  View Network Information:
    -  `ifconfig -a`
2.  View processes listening on ports:
    -  `netstat -tulpn | grep LISTEN`
3.  See if the remote machine can 'see' the target machine:
    -  `ping <hostname or IP>`
4.  Docker communicates via HTTP, thus you can use telnet
    -  `telnet <IP Address> port`
    -  (Use ctrl + c to exit)

The above series of commands told me the Docker process was up and listening on the expected port, the remote machine could ping the target machine, BUT telnet could not connect to the docker listener port. Thus, leading to finding the firewall configuration was blocking communication.

5.  Use the following series of commands to update the firewall settings.

```shell script
ufw allow 22/tcp
ufw allow 2376/tcp
ufw allow 7946/tcp
ufw allow 7946/udp
ufw allow 4789/udp
ufw reload
ufw enable
systemctl restart docker
```
