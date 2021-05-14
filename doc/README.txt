



Basketball All-Stars
Authors: Joshua Choi, Aditya Panikkar, Anirudh Venkatraman
Revision: April 25


Introduction: 


For our capstone project, we have decided to make a game which simulates a game of basketball. 2 users play a match against each other, and each person is able to control one player. The winner of the game is the first player to score 12 points. Our unique spin on this game is powerups. These powerups will be randomly selected and will be granted to players in 20 second intervals. The power ups will allow users to obtain boosts such as increased movement speed or increased jump height. Our program caters to those who love gaming, or are just interested in playing basketball. The primary features of our program include a graphical user interface, player controlled characters, a live updating energy bar, a live updating score, and powerups. Our program is meant to provide a fun, exciting experience for the user and hopefully spark some friendly rivalry between players. We wanted to write this program in order to provide entertainment and simulate a new feel for the sport of basketball during this pandemic.


Instructions:
Our program is a two-dimensional basketball game between two individual characters. These characters can be customized by the user and are able to do such things as shoot the ball, block shots, jump, dribble, and dash

The user interactions to control the player are as follows:
- player moves right using right arrow key
- moves left using left arrow key
- jumps using up arrow key
- space to shoot ball
- shift to dash
* Note: DO NOT SHOOT WHEN YOU DO NOT HAVE THE BALL OR AN ERROR WILL ARISE
* Note: IF THE BALL GOES OFF THE SCREEN, IT IS AN AIRBALL -> PRESS ENTER TO RESPAWN IT


When the program is run, the users are greeted with a screen which explains how to play the game. Next, both users are able to choose their team, and team name. Once the preliminary details are finalized, the court is rendered with two hoops at opposite ends[a] (side view of court pictured above).[b] User 1 uses the arrow keys to move, and left shift to dash. User 2 will also use the same controls but from his/her own device (via online). In order to shoot, each player clicks the space bar. Additionally, in order to dash, the player can press the left shift key. We will also provide a polished and aesthetically pleasing graphical user interface.[c] 


Specific Workings of the Game: When the player dashes, this will also count as a “steal” button, meaning that the player will steal with the dash button.[d] When the player dashes, he/she will bolt forward for a set distance of 80 units. Further, to block a shot, the player will have to collide or come into contact with the ball. One can do this by jumping up, but the player will not steal the ball when coming into contact with the ball when it is in possession of the other player. In order to steal the ball from another player, one will have to press “shift” and dash past the opponent.[e][f][g][h] The players will automatically dribble and shoot when the user presses “spacebar”. How the shooting works is the closer the player is to the basket, the more likely he/she is to make it. The user will simply press the shoot button and the game will automatically shoot for them based on rng. Within the 3 point arc, it will be an automatic make unless the opponent blocks the shot.[i][j][k] For more details about how likely a player is to make a shot based on position, see the diagram below. The limiting factor to a player dashing or taking a long shot will be the amount of energy he/she has.[l][m][n][o] The player has one energy, which regenerates every 2.5 seconds. The player can only dash/shoot when the energy is full.
  

Features List (THE ONLY SECTION THAT CANNOT CHANGE LATER):
Must-have Features:
[These are features that we agree you will definitely have by the project due date. A good final project would have all of these completed. At least 5 are required. Each feature should be fully described (at least a few full sentences for each)]
* Graphical User Interface which contains the court, the hoops, the characters, and the scoreboard. The GUI will be able to model a 2D real world gym.
* In game characters which the players are able to control. The player will be able to move left and right, as well as jump, shoot, and dash with the character 
* Realistic physics which dictate how the ball moves through the air
* There will be powerups: jump boost and speed boost.
* Networking capabilities (online gaming). Ability for player to play not only on the same machine, but online
* Live updating energy bar which shows the user the current energy of the player


Want-to-have Features:
[These are features that you would like to have by the project due date, but you’re unsure whether you’ll hit all of them. A good final project would have perhaps half of these completed. At least 5 are required. Again, fully describe each.]
* A more advanced version of our graphical user interface (using graphics outside of the processing library)
* Advanced Scoreboard which contains time remaining and live score
* Ability for the player to pause the game during a play. The user would click the escape button for a pause to happen.
* Sound effects (crowd noise, bouncing ball noise, making basket noise)
* Ability to detect when the balls goes outside the court, resulting in an out of bounds
* Ability to track statistics such as shooting percentage of each player and display statistics at the end of a match
* Sound effects (crowd noise, bouncing ball noise, making basket noise)


Stretch Features:
[These are features that we agree a fully complete version of this program would have, but that you probably will not have time to implement. A good final project does not necessarily need to have any of these completed at all. At least 3 are required. Again, fully describe each.]
* Add a referee character which would be programmed to make calls such as calling fouls or out of bounds calls
* Players are able to have multiple players on their team, and can make substitutions when their player becomes tired
* Make the game 5 v 5 instead of a 1 v 1 gamemode
* Implement a moving interactive audience




Class List:
[This section lists the Java classes that make up the program and very briefly describes what each represents. It’s totally fine to put this section in list format and not to use full sentences.]

Player.java  - represents the player. This class will lay the framework for the players behavior, and will contain the movement and shooting functions

Court.java - Represent the court as a whole and contains the Graphical User Interface. Also contains the hoops

Ball.java - Represents the basketball which players can dribble and shoot with. Tracks the location of the ball, and contains the physics of how the ball moves

Game.java - This class will contain the main method and actually run (start) the program

PlayerData.java - player data for firebase

BallData.java - ball data for firebase

MovingImage.java - allows to draw images and move them around

FirebaseBackend.java - connects to the online features

Scoreboard.java - adds a scoreboard


Credits:
[Gives credit for project components. This includes both internal credit (your group members) and external credit (other people, websites, libraries). To do this:


Anirudh - Network and online capabilities and GUI
Aditya  -  Player class, implement power ups, and GUI
Josh - Ball class, implement realistic physics, and GUI
All three - help put together the whole game and work on game operations

Used images from deviantArt for the court, ball, and player images
Used Mr. Shelby's Firebase backend ideas and Moving Image class ideas
Implemented using java awt graphics