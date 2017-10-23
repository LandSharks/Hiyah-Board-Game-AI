# Hiyah-Board-Game-AI
Hi-Ya! is a board game designed by Scott Gordon for the Fall 2017 CSC 180 course. It is a martial arts theme game where the pieces fight
rather than capture eachother. 

### The Game

The following instructions/rules are taken directly from Scott Gordons site: http://athena.ecs.csus.edu/~gordonvs/180

"Hi-YA!" is a chess-like game in which each player takes turns moving one of
his/her pieces, and tries to knock out the opponent's King.  It is played on
a 7x8 board, and each side starts with a set of pieces consisting of 1 King,
3 Ninjas, 3 Samurai, 3 mini-Ninjas, and 3 mini-Samurai.  The different pieces
have rules about how they can move, which is described later.

Players alternate moves, moving one of their own pieces each turn.
When it is your turn, you MUST make a move -- you cannot pass.
If it is your turn and you have no legal moves, you lose.

A player wins either when he/she knocks out the opponents' King, or if it is
the opponent's turn but the opponent has no legal moves.

The initial board state is with some slight modifications:

![initial positions](http://athena.ecs.csus.edu/~gordonvs/180/board1.JPG)

Take note that the image provided by Scott Gordon uses J and S to define Ninja's and Samurai respectively.

However due to project time and limitiations of Eclipses Console, I used the following letters.

Computer:
King - K, I - Ninja, i - Mini Ninja, A - Samurai, a - Samurai

Player:
King - K, N - Ninja, n - Mini Ninja, S - Samurai, s - Mini Samurai

Unlike regular chess, the pieces in "Hi-YA!" may ONLY move onto empty squares.
They may NEVER move onto any square occupied by another piece.
Rather than "capturing" (as in regular chess), pieces in "Hi-YA!" attack each
other by moving onto the square directly IN FRONT OF an opposing piece.
For example, in the above picture, the Human could move his Ninja at E2 to B5,
and thus attack (or "fight") the computer's mini-Samurai at B6.  It is customary
in this game to announce "HiYa!!" when attacking an opponent's piece, and your
program is expected to do that when appropriate.

When a piece attacks an opposing piece, the opposing piece is either demoted
(becomes a weaker piece), or is removed from the board, depending on what type
of piece it is.  In particular:

- if attacked, a Ninja becomes a mini-Ninja.
- if attacked, a Samurai becomes a mini-Samurai.
- if attacked, a mini-Ninja is removed.
- if attacked, a mini-Samurai is removed.
- if attacked, a King is removed and that player loses.

The different pieces move as follows:

KING:

-- cannot move at all, ever.

-- whoever attacks the king wins!

NINJA:

-- moves roughly the same as the "bishop" in regular chess. That is, in a diagonal line any number of squares.
    
-- CANNOT jump over other pieces. Once it bumps into a piece (or the side of the board) that is as far as it can go.
    
-- a ninja can move in the forward diagonal direction. It can only move in a backwards diagonal direction if that move is an attack.
    
-- note that a ninja moves diagonally, but attacks by landing on the square directly below [not diagonal] (or above, in the case of the computer moving) an opposing piece.

SAMURAI:

-- moves roughly the same as a "rook" in regular chess. That is, in a horizontal or forward direction any number of squares.
    
-- never moves backwards.

-- CANNOT jump over other pieces. Once it bumps into a piece (or the side of the board) that is as far as it can go.
    
-- a samurai can move or attack in the forward direction. It can only move in the sideways direction if that move is a attack.

MINI-NINJA

-- moves the same as a NINJA, except that it can only move a distance of one square. That is, 1 square diagonally forward, or 1 square diagonally backwards if that move is an attack.
    
-- note that a mini-ninja moves diagonally, but attacks by landing on the square directly below [not diagonal] (or above, in the case of the computer moving) an opposing piece.

MINI-SAMURAI

-- moves the same as a SAMURAI , except that it can only move a distance of one square. That is, 1 square directly forward, or 1 square sideways if that move is an attack.
    
OTHER DETAILS - 

-- moving is compulsory.  That is, a player cannot "pass".

-- if you move in front of one of your own pieces, you are not attacking it.

-- unlike chess, there is no such thing as "check" or "checkmate".  Winning is by actually attacking the KING (or the opponent having no move).
-- unlike chess, there are no pawns, knights, or queens.

-- unlike chess, inability to move isn't a stalemate draw - it is a LOSS.

-- unlike Go (Weiqi), players don't place pieces on the board. The pieces are on the board at the beginning, and are moved.
    
-- NINJAs and SAMURAIs are captured by first being attacked and demoted to mini-NINJAs and mini-SAMURAIs, and then later removed in a future attack.
    
-- an attack happens only when someone MOVES a piece in front of another piece.

For example, if you move a SAMURAI to attack your opponent's NINJA,
your opponent's NINJA is demoted to a mini-NINJA and it is then your
opponent's turn.  Your opponent's mini-NINJA is NOT then attacking your
SAMURAI just because it is in front of it.  Attacking only happens when
someone MOVES a piece in front of another piece.

### Details about Alice

Alice uses Minimax algorithm and Alpha-beta pruning for decision making. The Hueristic takes into account potential board states by counting the total value of all the pieces on the board and makes moves based on gaining "positive" value on the board.

Alice uses an experimental version of Iterative Deepening in order for it to still be fast, but challenging as the game goes on. Alice is very good against begineer players and is able to close out games sometimes in 5 turns. However, as the game progresses Alice tends to leave its King open for attack and I am looking for ways to prevent this.

Additionally, I am looking into alternative hueristics to hopefully provide Alice a stronger edge.
