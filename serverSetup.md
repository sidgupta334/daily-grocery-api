- Install docker in the instance (Google it)
***sudo yum install -y gcc-c++ make


- Pull Docker Image for PostgreSQL
***docker pull postgres

- Create docker container for postgres DB
**docker run -d --name grocerica-db -e POSTGRES_PASSWORD=grocerica789! -v  /home/ubuntu/bazar-db:/var/lib/postgresql/data -p 5433:5432 postgres

**docker run -d --name treggo-db -e POSTGRES_PASSWORD=treggo789! -v  /home/ubuntu/treggo-db:/var/lib/postgresql/data -p 5432:5432 postgres

- To run some predefined db queries run it like:
**docker exec -it treggo-db psql -U postgres (enters in postgres mode)
**docker exec -it grocerica-db  psql -U postgres -c "SELECT * FROM test;"

- Copy JAR and Dockerfile into Server	

- Run Dockerfile to deploy the JAR file
**docker build . -t grocerica-api

- Run the newly created docker image and link it with postgres container running with details of image it is linked with:
**docker run -p 8080:8080 --name grocerica-app --link grocerica-db:postgres -d grocerica-api



--To update the JAR file:

--- Stop containers: **docker stop grocerica-app
--- Remove Containers: **docker rm grocerica-app
--- Remove images: **docker image rm grocerica-api

(Redo JAR related steps)
