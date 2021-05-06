



Basketball All-Stars
Authors: Joshua Choi, Aditya Panikkar, Anirudh Venkatraman
Revision: April 25


Introduction: 


For our capstone project, we have decided to make a game which simulates a game of basketball. 2 users play a match against each other, and each person is able to control one player. The winner of the game is the first player to score 12 points. Our unique spin on this game is powerups. These powerups will be randomly selected and will be granted to players in 20 second intervals. The power ups will allow users to obtain boosts such as increased movement speed or increased jump height. Our program caters to those who love gaming, or are just interested in playing basketball. The primary features of our program include a graphical user interface, player controlled characters, a scoreboard, and some sound effects. Additionally, we will have characters that will have varying features (advantages and disadvantages). For example, a character might be tall but be slower than other characters while another character may be fast but smaller than others. Our program is meant to provide a fun, exciting experience for the user and hopefully spark some friendly rivalry between players. We wanted to write this program in order to provide entertainment and simulate a new feel for the sport of basketball during this pandemic.


Instructions:
Our program is a two-dimensional basketball game between two individual characters. These characters can be customized by the user and are able to do such things as shoot the ball, block shots, dash past other characters, and dunk.
  





When the program is run, the users are greeted with a screen which explains how to play the game. Next, both users are able to choose their team, and team name. Once the preliminary details are finalized, the court is rendered with two hoops at opposite ends[a] (side view of court pictured above).[b] User 1 uses the WASD keys to move, and left shift to dash. User 2 will also use the same controls but from his/her own device (via online). In order to shoot, each player clicks the space bar. Additionally, in order to dash, the player can press the left shift key. We will also provide a pause screen and make sure to create a polished and aesthetically pleasing graphical user interface.[c] [d]


Specific Workings of the Game: When the player dashes, this will also count as a “steal” button, meaning that the player will steal with the dash button.[e] When the player dashes, he/she will bolt forward for a set distance of ___ units. Further, to block a shot, the player will have to collide or come into contact with the ball. One can do this by jumping up, but the player will not steal the ball when coming into contact with the ball when it is in possession of the other player. In order to steal the ball from another player, one will have to press “shift” and dash past the opponent.[f][g][h][i] The players will automatically dribble and shoot when the user presses “spacebar”. How the shooting works is the closer the player is to the basket, the more likely he/she is to make it. The user will simply press the shoot button and the game will automatically shoot for them based on rng. Within the 3 point arc, it will be an automatic make unless the opponent blocks the shot.[j][k][l] For more details about how likely a player is to make a shot based on position, see the diagram below. The limiting factor to a player dashing or taking a long shot will be the amount of energy he/she has. In the “50% chance” range, this will require 4 energy bars and in the “75% range”, this will require 3 energy bars. Finally, in the automatic, this will require 1 energy bar.[m][n][o][p]


  



Features List (THE ONLY SECTION THAT CANNOT CHANGE LATER):
Must-have Features:
[These are features that we agree you will definitely have by the project due date. A good final project would have all of these completed. At least 5 are required. Each feature should be fully described (at least a few full sentences for each)]
* Graphical User Interface which contains the court, the hoops, the characters, and the scoreboard. The GUI will be able to model a 2D real world gym.[q][r][s]
*  Menu Screen which includes instruction, and ability to choose team names[t][u][v]
* In game characters which the players are able to control. The player will be able to move left and right, as well as jump, shoot, and dash with the character 
*  Above the court, there will be a scoreboard which contains the real time score
*  Realistic physics which dictate how the ball moves through the air
* There will be powerups: jump boost, speed boost, and on fire. Jump boost will increase the jump height, speed boost will increase the movement speed, and on fire will increase the probability of making a shot
* Networking capabilities (online gaming). Ability for player to play not only on the same machine, but online


Want-to-have Features:
[These are features that you would like to have by the project due date, but you’re unsure whether you’ll hit all of them. A good final project would have perhaps half of these completed. At least 5 are required. Again, fully describe each.]
* A more advanced version of our graphical user interface (using graphics outside of the processing library)[w]
* Ability for the player to pause the game during a play. The user would click the escape button for a pause to happen.[x]
* Sound effects (crowd noise, bouncing ball noise, making basket noise)
* Ability to detect when the balls goes outside the court, resulting in an out of bounds[y]
* Ability to track statistics such as shooting percentage of each player and display statistics at the end of a match
* There will be a power bar which will continuously regenerate throughout the game. A long shot ( past the 3 point line ) will require 3 energy points and a dash will require 1 energy point. There is a limited amount of energy, therefore constraining the amount of moves a player can effectively make
* Sound effects (crowd noise, bouncing ball noise, making basket noise)


Stretch Features:
[These are features that we agree a fully complete version of this program would have, but that you probably will not have time to implement. A good final project does not necessarily need to have any of these completed at all. At least 3 are required. Again, fully describe each.]
*  Add a referee character which would be programmed to make calls such as calling fouls or out of bounds calls[z]
*  Players are able to have multiple players on their team, and can make substitutions when their player becomes tired
*  Make the game 5 v 5 instead of a 1 v 1 gamemode[aa][ab][ac]
* Implement a moving interactive audience




Class List:
[This section lists the Java classes that make up the program and very briefly describes what each represents. It’s totally fine to put this section in list format and not to use full sentences.]


Player.java  - represents the player. This class will lay the framework for the players behavior, and will contain the movement and shooting functions


Court.java - Represent the court as a whole and contains the Graphical User Interface. Also contains the hoops


Ball.java - Represents the basketball which players can dribble and shoot with. Tracks the location of the ball, and contains the physics of how the ball moves


Scoreboard.java - Represents the scoreboard which monitors the score in real time


Stats.java - This class will hold all the statistics for each player. These stats include shots attempted, shots made, and points


Game.java - This class will contain the main method and actually run (start) the program




Credits:
[Gives credit for project components. This includes both internal credit (your group members) and external credit (other people, websites, libraries). To do this:


Anirudh - Network and online capabilities and GUI
Aditya  -  Player class implement power ups, and GUI
Josh - Ball physics, code the powerups, and GUI
All three - help put together the whole game and work on game operations
[a]I'm having trouble envisioning the game based on this. How do you steal the ball from the other player? How does "shooting" work? Can you change the angle of the shot, or how hard you throw? I need more information about user interactions with each other.
[b]I'm having trouble envisioning the game based on this. How do you steal the ball from the other player? How does "shooting" work? Can you change the angle of the shot, or how hard you throw? I need more information about user interactions with each other.
[c]I'm having trouble envisioning the game based on this. How do you steal the ball from the other player? How does "shooting" work? Can you change the angle of the shot, or how hard you throw? I need more information about user interactions with each other.
[d]What is the purpose of a pause screen? Is it going to be there everytime a user clicks a key, or is it just there for the user to press whenever they feel like they need a break?
[e]How far away can the player be when they use the dash to steal the ball, do they have to be super close or can they be a bit distanced?
[f]What if both players are trying to dash past each other, do they still only use the left shift key or does player 1 use the left shift key and player 2 use the right shift key or are they only using the 1 single left shift key?
[g]this is an online game
[h]_Marked as resolved_
[i]_Re-opened_
[j]Is the arc/shooting of the ball pre-programmed or are players going to be able to decide at what angle they want to shoot at?
[k]_Marked as resolved_
[l]_Re-opened_
[m]It says "require 4 energy bars...3 energy bars...1 energy bar". Since it's required, it sounds like they need to somehow need to get the energy bars. Are there coins they collect which increases their energy or is it not required and they already get the energy by moving into the range?
[n]they will start with a set amount of energy, as they use the energy and the game progresses, the energy will gradually regenerate.
[o]_Marked as resolved_
[p]_Re-opened_
[q]Is it 3D or 2D based?
[r]_Marked as resolved_
[s]_Re-opened_
[t]Where would the names be displayed?
[u]_Marked as resolved_
[v]_Re-opened_
[w]can you elaborate on this. What more advanced graphical user interface would be used and how different will it be from processing?
[x]The pause button was mentioned earlier, so it seems like it was going to be a must-have feature. You may want to clarify which one it is.
[y]Shouldn't this be a must have feature, as in Basketball you need to know if the ball is in the court or is outside.
[z]How will the fouls be called? Can the characters move their hands to make a foul? If they can't foul other players, then that should be edited so the ref can call it.
[aa]There could be a feature where the player could choose whether they want to play 1v1 or 5v5.
[ab]_Marked as resolved_
[ac]_Re-opened_