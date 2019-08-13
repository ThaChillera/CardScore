# Card Score App

This app is used to track the score in the game 'Boerenbridge'.

##Game rules:
This game is played with a full 52 card french-suited deck.
Players are at all times allowed to see their own cards, but may hide their cards from their opponents.

One full game is sub-divided into multiple rounds, and each round is divided into one or more play-rounds.

At the start of each round players get dealt as many cards as the round count. One card gets shown from the remaining cards pile to randomly assign a suite as the trump suite.

### Start of the round

At the start of each round, each player predicts how many play-rounds they will win.

Each play-round every player plays one card, and the first card played by the starting player dictates which suite is 'requested'. If the other players have a card of that suite in their hands, they are required to play that suite. The player who played the highest value card wins the round. The winning player starts the next play-round.

### Card value chart
Within a suite, Ace is valued the highest, followed by King, Queen, Jack, 10, 9, etc.

Between suites, the requested suite always wins 
Example: first player plays 2 of hearts, following player plays Ace of spades. Since hearts was requested, the first player wins the round.
    
The only exception to this rule is trump cards, trump cards 'trump' the requested card. So if the first player plays an Ace of spades, hearts is the trump suite, and the second player plays a 2 of hearts, the second player wins the round.

### Score
At the end of each round the predicted score of each player is compared to the actual amount of play-rounds won. 
If they were correct, they get 10 points + 2 points per round won.
If they were incorrect, the lose the difference between the prediction and score \* 2 points.

Example: the first round, both players get dealt 1 card. They both predict they will win one play-round. One player wins a play-round, the other wins no play-rounds. The winning player would get 10 + 1 \* 2 = 12 points. The losing player would lose (1 - 0) \* 2 = 2 points.
