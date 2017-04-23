# Dots and Boxes

This project is a demonstration project for evaluation purposes. 

It implements a _Dots And Boxes_ gameplay: https://en.wikipedia.org/wiki/Dots_and_Boxes


## Deliverables

- Server side: **Spring Boot** project implementing the players and game
  logic, and exposing a REST API. Built with gradle and ships with Unit 
  and Integration test.

- Client side: (very basic) **Angular 2** web application. It is ugly, but
  allows to interact with and exercise the server. I did not want to
  invest very much in markup etc.

## Requirements

- Java 8.
- Node 7.0.0 or higher.

## Running the Solution

### Server-side

For the unit and integration tests:

    $ cd server
    $ ./gradlew test

I have made the integration tests very verbose as they dump lots of
information on the HTTP exchange.

Running the server (obviously only suitable this way for demoing):

    $ cd server
    $ ./gradlew run

The server outputs some informative logs on game evolution, and dumps
the boards in ASCII form for debugging and to ensure the web client
application has the same, coherent state.

If you use IntelliJ IDEA you can import the project within the `server`dir.

### Client-side

I am using Yarn as package manager better than npm. Please install it
as per https://yarnpkg.com/en/docs/install

Then:

    $ cd client-webapp
    $ yarn
    $ npm run start

The above should launch a development mode for the web application
which should suffice for demonstration purposes.

Then, point your browser to: http://localhost:4200

## Comments

- I have introduced the notion of "players" that need to "register"
  before playing the game. Needs to be done when entering the web
  client.

- This has been my first "serious" Spring project, had to learn some
things on the way but I have to say I have liked it a lot.

- The computer is not particularly smart: it just makes the first
random possible move available.

- I am allowing players to start a new game even if they lose, but not
from the game view itself. They need to get back to "Start a Game".

- I am allowing to specify who starts moving: the computer or the
player.

- The web client interface is ugly, surely not acceptable for anything
  production. But it has the required functionality to exercise the game.
