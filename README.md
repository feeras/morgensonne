# Morgensonne

Spring Boot microservice to grab images from construction site and store them as post in Wordpress

## How to start application

Start java application with the environment variables `username` and `password`

- `"-Dusername=USERNAME_HERE"`
- `"-Dpassword=PASSWORD_HERE"`

## How to redeploy application

1. Create artifact `mvn clean install -DskipTests`
2. Transfer artifact to server: `scp .\morgensonne-1.0.0-SNAPSHOT.jar feeras@192.168.1.104:~`
3. Connect to server: `ssh feeras@192.168.1.104`
4. Move artifact jar file from home folder to morgensonne (Old file will be
   overwritten): `mv morgensonne-1.0.0-SNAPSHOT.jar morgensonne/`
5. Search running java process: `ps -ef | grep 'java'`
6. Kill the process: `kill PROCESS_ID`
7. Start application with new
   artifact: `nohup java -Dusername="USERNAME_HERE" "-Dpassword=PASSWORD_HERE" -jar morgensonne-1.0.0-SNAPSHOT.jar > log.log 2>&1 &`
8. Alternative: Run script to avoid reentering credentials: `./start.sh`

## Read logs

1. `tail -f log.log`