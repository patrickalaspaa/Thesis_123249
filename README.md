# spotapp
Clojure version of the HFL application. I'm just trying to learn this stuff, so don't be mean..
Running this application succesfully requires some personal setting
- You need to create a database then define the connection to the database in backend/src/spotapp/app_config.clj
- For the frontend you need a registered Google API key that is defined in frontend/src/const.js

# Start app on localhost:8000
    #Install docker
    apt-get install docker -y
    #To start an interactive container used for development run the start script in the project root
    ./start_dev.sh

# To run a containerized production version of the frontend
  - Install docker
  - Run app-deploy.sh (Builds container that serves at port 8000)


# To build app front end
  - Define backend url and home url in frontend/src/const.js 
  - Install docker
  - run build-front.sh in the root directory
  - Deploy contents of frontend/dist to a web server








https://en.wikibooks.org/wiki/Clojure_Programming/Examples/JDBC_Examples#MySQL
