#!/bin/bash

docker image build --file Dockerfile --tag artarkatesoft/photo-app-filebeat:7.10.1 --tag artarkatesoft/photo-app-filebeat:latest  .

docker image push artarkatesoft/photo-app-filebeat
docker image push artarkatesoft/photo-app-filebeat:7.10.1

