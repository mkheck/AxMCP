# README #

##AxMCP##

###Alexa interface for the Master Control Program (MCP)###

Spring Boot-based application, deployable to Cloud Foundry. Provides Alexa integration & control of the Master Control Program I developed to monitor & control my home Renewable Energy system from the Cloud. Useful, fun, & hopefully helpful to others who may want to do the same. :)

### How do I start? ###

This is a Maven-based project, so `git clone` it, load it in your favorite IDE (or just `cd` into its project directory on your machine), & `mvn package`. To deploy to Cloud Foundry, just `cf push`. You will need to define the following env vars to make the application run:

* WEBSOCKET_URI
* APPLICATION_ID

NOTE: You probably won't actually run it as-is (even providing a valid WebSocket uri & your own Alexa app id), as you won't have access to _my_ MCP. But please do get your own! Renewable energy is good for the planet and fun to work with, and it gets even pale developers (guilty) out into the sun from time to time. :D

### Developer ###

* Mark Heckler
* @MkHeck
* mark.heckler@gmail.com
