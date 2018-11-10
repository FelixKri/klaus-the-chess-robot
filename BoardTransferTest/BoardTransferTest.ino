int board1[8][8] = {{0,0,0,0,0,0,0,0},
					{0,1,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0}};
					
int board2[8][8] = {{0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0},
					{0,1,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0},
					{0,0,0,0,0,0,0,0}};

void setup() {
	Serial.begin(38400);
}

char i = 0;

void loop() {
	i ^= 1;
	
	for (int x = 0; x < 8; x++) {
		for (int y = 0; y < 8; y++) {
			if(i) {
				Serial.print(board1[x][y]);
			}
			else {
				Serial.print(board2[x][y]);
			}
		}
	}
	
	Serial.print("\n");

	delay(800);	
}
