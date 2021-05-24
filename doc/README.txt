Basketball All-Stars


Authors: Joshua Choi, Aditya Panikkar, Anirudh Venkatraman
Revision: May 23


Introduction:                                         
Basketball All-Stars is a 2D basketball game where users can control a basketball player on a court. The game features online multiplayer capabilities, and each player is matched up against another player who is controlling their corresponding player. The objective of the game is to score the most amount of points within the given time span (2 minutes). Players can move forward, backward, jump up, block shots, and dash past their opponents on the court. Many games which model basketball have been made before, thus in order to create a unique and original game, we added some special elements. Our unique features that set apart our game include powerups and energy. Powerups randomly spawn on the court at set time intervals. There are multiple kinds of power-ups that give players boosts. For example, one power up may give players an advantage in jumping height, while another may allow faster speed. The powerups can be discerned by the color icon that is displayed when it has spawned in: red (greater jump height) or blue (speed). The energy feature of our game determines the number of times a player can shoot and dash. There will be two energy bars which will regenerate as time goes on, and the point of this feature is to prevent players from overly spamming dash or shoot. Furthermore, in the last 20 seconds of the game, both players will have increased probability of making shots. There will be a 20 percent increase in shot making chances. Our program caters to those who love gaming or are just interested in playing basketball. The primary features of our program include an interactive tutorial for new players, a single player gamemode,  a graphical user interface, player-controlled characters, a live-updating energy bar, a live-updating score, and powerups. The main skill element of our game is game awareness. In order to win a game, the user must be able to have a good idea of the opponent's energy and what powerups they may have. Using this awareness, they can adapt their gameplay to benefit them. Our program is meant to provide a fun, exciting experience for the user and hopefully spark some friendly rivalry between players. We wanted to write this program in order to provide entertainment and simulate a new feel for the sport of basketball during this pandemic. See the visual aid below to get a hint of what our game looks like.




Sample Visual Aid:
  







Instructions:
Our program is a competitive two-dimensional basketball game between two players. The user interactions to control the player are as follows:
* Move Right: right arrow key
* Move Left: left arrow key
* Jump: up arrow key
* Shoot: space bar
* Dash: shift key
* Pause Game: escape


When the program is run, the users are greeted with a screen that asks the user if they want to play online , play solo, or if they want to utilize the interactive tutorial to learn how to play the game. Once the user chooses online, they are able to choose between creating their own room or joining a previously created room. When a player joins a room, the court will be set up so that there are two hoops on opposite ends of the court. Once two players join the room, the ref will blow the whistle and the ball will spawn in at the middle of the court. Both players use the left and right arrow keys to move, the shift key to dash, the up arrow key to jump, and the space key to shoot. In order to block a shot, the player must jump into the ball after another player has shot and intersect the ball before it reaches the hoop. Furthermore, the player is able to pause the game and view their detailed statistics. 


Specific Workings of Gameplay:  Before playing the game, the user has an option to walk through an interactive tutorial that shows the main controls of the game. After joining a room with another player, the match begins. Each user can store a maximum of 2 energy points, in which 1 energy point is used when the player shoots or dashes. The energy regenerates one point at a time every few seconds and is shown through a live updating energy bar. Powerups spawn at timed intervals at a random position on the opposite side of the court as the player (so that the player cannot instantly walk through a power-up). When the player picks up the powerup, they receive the designated boost for a few seconds, and the energy bar turns yellow to indicate that the boost has been applied. The powers we have include increased jump height, increased player speed, and automatic make on shot. Each user’s powerups and energy are hidden from the other player. The information is hidden to develop the main strategy of the game, which is awareness. When shooting, the probability of the shot going in is based on the distance from the hoop. The probabilities are illustrated in the image below. When a player has possession of the ball, a 20-second countdown is started. If the player does not shoot the ball by the end of this countdown, the ball will automatically get shot once the player has sufficient energy. The score updates in real-time, and the person who scores the most points in 2 minutes wins the game. 
Graphic Showing Probability of Making a Shot:
  



Features List (THE ONLY SECTION THAT CANNOT CHANGE LATER):
Must-have Features:
[These are features that we agree you will definitely have by the project due date. A good final project would have all of these completed. At least 5 are required. Each feature should be fully described (at least a few full sentences for each)]
* Graphical User Interface which contains the court, the hoops, the characters, and the scoreboard. The GUI will be able to model a real world gym.
* Menu Screen which includes instruction, and ability to choose team names
* In game characters which the players are able to control. The player will be able to move left and right, as well as jump, shoot, and dash with the character 
* Above the court, there will be a scoreboard which contains the real time score
*  Realistic physics which dictate how the ball moves through the air
* There will be powerups: jump boost, speed boost, (if power bar is implemented, then power boost), on fire (player’s probability of making a shot increases)


Want-to-have Features:
[These are features that you would like to have by the project due date, but you’re unsure whether you’ll hit all of them. A good final project would have perhaps half of these completed. At least 5 are required. Again, fully describe each.]
* A more advanced version of our graphical user interface (using graphics outside of the processing library)
* Ability for the player to pause the game during a play. The user would click the escape button for a pause to happen.
* Networking capabilities (online gaming). Ability for player to play not only on the same machine, but online
* Ability to detect when the balls goes outside the court, resulting in an out of bounds
* Ability to track statistics such as shooting percentage of each player and display statistics at the end of a match
* There will be a power bar which will continuously regenerate throughout the game. A long shot will require 3 energy points and a dash will require 1 energy point. There is a limited amount of energy, therefore constraining the amount of moves a player can effectively make
* Sound effects (crowd noise, bouncing ball noise, making basket noise)


Stretch Features:
[These are features that we agree a fully complete version of this program would have, but that you probably will not have time to implement. A good final project does not necessarily need to have any of these completed at all. At least 3 are required. Again, fully describe each.]
*  Add a referee character which would be programmed to make calls such as calling fouls or out of bounds calls
*  Players are able to have multiple players on their team, and can make substitutions when their player becomes tired
*  Make the game 5 v 5 instead of a 1 v 1 gamemode
* Implement a moving interactive audience








Class List:
[This section lists the Java classes that make up the program and very briefly describes what each represents. It’s totally fine to put this section in list format and not to use full sentences.]


* MovingImage.java - lays the framework for the player, ball, and referee classes
* Ball.java - represents the ball, and dictates the shooting and dribbling
* Player.java - represents the player, and dictates the player movement , powerups, and energy
* Referee.java - represents the referee, and dictates the behavior of the ref
* PlayerData.java - stores data about the player so that it is easier to implement in firebase
* BallData.java - stores ball about the player so that it is easier to implement in firebase
* Court.java -  creates player, ball, and referee objects and draws court. Dictates timing for powerup spawning, energy regeneration, and shot clock. Records user input and does tasks accordingly
* CourtTutorial.java - the interactive tutorial
* CourtSolo.java - the single player version of the game which does not have networking capabilites
* FirebaseBackend - creates online networking capabilities
* Home.java - home screen of the program
* PlayerStats.java - prints statistics upon player command




Credits:
[Gives credit for project components. This includes both internal credit (your group members) and external credit (other people, websites, libraries). To do this:


Anirudh - Network and online capabilities and Tutorial
Aditya  -  Player class, implement powerups and energy, pausing feature
Josh - Ball class, implement realistic physics, ending screens, Referee class
All three - help put together the whole game and work on game operations/structure


Libraries: java.awt.graphics, firebase, Mr. Shelby’s demo classes( MovingImage, PlayerData, BallData, FirebaseBackend)