# proofs-of-concept

You can run the project with docker-compose:
```bash
docker-compose up -d
```

If you are using Widnows and end up with an error in the build stage, you can try running this and then cloning the project:
```bash
git config --global core.autocrlf false
```

You can access the openapi ui in your browser after the services have started:
- user-service: http://localhost:9090/swagger-ui.html
- pet-service: http://localhost:9091/swagger-ui.html

If the link don't work, it may be because docker-compose tried to start the services before the databases had finished initialising, to fix this simply re-run:
```bash
docker-compose up -d
```
This is going to start the services again if they are not already running.

To see the logs, run:
```bash
docker-compose logs -f
```

To shutdown the service, run:
```bash
docker-compose down
```
