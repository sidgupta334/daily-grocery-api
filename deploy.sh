docker stop grocerica-app
docker rm grocerica-app
docker image rm grocerica-api
docker build . -t grocerica-api
docker run -p 8080:8080 --name grocerica-app --link grocerica-db:postgres -d grocerica-api