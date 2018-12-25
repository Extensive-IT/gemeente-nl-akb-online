#!/bin/sh
name='gemeente-nlakbonline'
if [ $(docker ps -a -f "name=$name" --format '{{.Names}}') == $name ]; then
    docker container stop gemeente-nlakbonline
    docker container rm gemeente-nlakbonline
fi

docker run --name gemeente-nlakbonline \
    --link dev-mysql:db \
    --link proxy:gemeente-auth \
    --link dev-smtp:smtp \
    -e VIRTUAL_HOST=gemeente-akb \
    -v "C:\Environment\gemeente-nlakbonline\config":/config \
    -v "C:\Environment\gemeente-nlakbonline\external-data":/external-data \
    -d gemeente/nlakbonline
