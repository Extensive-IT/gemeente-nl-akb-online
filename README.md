# gemeente-nl-akb-online
Dutch AKB Online Form

## Deployment
```
docker run -p 1080:80 --name dev-smtp djfarrelly/maildev
docker run --name proxy -d -p 80:80 -v //var/run/docker.sock:/tmp/docker.sock:ro jwilder/nginx-proxy
docker run --name gemeente-nlakbonline \
    --link dev-mysql:db \
    --link proxy:gemeente-auth \
    --link dev-smtp:smtp \
    -e VIRTUAL_HOST=gemeente-akb \
    -v /opt/kerk/gemeente-akb/:/config \
    -d gemeente/nlakbonline
```