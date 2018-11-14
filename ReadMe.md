# Klaus the Chess Bot
Klaus is an autonomous chess playing arm. It calculates it's moves using Stockfish 9 running on a Raspberry Pi 3. It's ELO Rating is about 2000, so it sould beat most casual players.

## Procedure

### Move detection
A human move is detected with Reed Switches, which are located under every field of the chess board. When the human lifts up a piece, the contact breaks. That way we know a piece is lifted up. When the Player places the piece on a new field, a contact is created and so we know the piece has been placed. 

The Reed Switches are shifted in via shift registers in order save contacts on the Arduino. 

With the magnets, we can't differentiate between the different Kinds of pieces, so in order to know which piece has been moved, positions of all pieces on the board are tracked from the starting position. 

### Move calculation
When a legal human move is detected, the chessboard Arduino sends the move (eg: Nf3 or e4) to the raspberry, where a new FEN String is made and passed to Stockfish. Stockfish then calculates move, which is then sent to the mechanical arm arduino. (e7e5 or e8g8). The Arm executes the move. Now the bot is waiting for the next move.

## Team
The Project was realized by Florian Rettenbacher, Johannes Rehrl, Felix Kristandl and Thomas Lienbacher and supervised by Professor Paul Fleischmann during the Laboratory Class in Fall 2018.

## License

MIT License
