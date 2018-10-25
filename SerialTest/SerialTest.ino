void setup() {
	Serial.begin(9600);//must be 9600
}

int i = 0;

void loop() {
	i++;
	
	char buf[64];
	snprintf(buf, sizeof(buf) - 1, "%s -> %d\n", "command: ", i);
	Serial.write(buf, strlen(buf));
	
	delay(100);
}
