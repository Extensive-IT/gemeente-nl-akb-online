#!/bin/sh
name='gemeente-nlakbonline'
if [ $(docker ps -a -f "name=$name" --format '{{.Names}}') == $name ]; then
    docker container stop gemeente-nlakbonline
    docker container rm gemeente-nlakbonline
fi

docker run --name gemeente-nlakbonline \
    --link dev-mysql:db \
    -e VIRTUAL_HOST=gemeente-akb \
    -v /opt/kerk/gemeente-akb/:/config \
    -d gemeente/nlakbonline
