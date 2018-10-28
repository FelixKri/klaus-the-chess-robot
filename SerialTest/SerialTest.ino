//#define READ_TEST
#define WRITE_TEST

void setup() {
	Serial.begin(38400);
	pinMode(LED_BUILTIN, OUTPUT);
}

int i = 0;

void loop() {
	
#ifdef READ_TEST
	i++;
	
	char buf[64];
	snprintf(buf, sizeof(buf) - 1, "%s -> %d\n", "command: ", i);
	Serial.write(buf, strlen(buf));
	
	delay(100);
#endif

#ifdef WRITE_TEST
	int k = 0;
	
	if (Serial.available() > 0) {
		String str = Serial.readString();
		Serial.print(str);
		Serial.flush();
		k = 1;
	}

	digitalWrite(LED_BUILTIN, k);
	
	delay(1);
#endif
}
