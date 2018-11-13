
//These Pins control the LED Lights
int latchPinLed = 2;
int clockPinLed = 12;
int dataPinLed = 11;

//These Pins control the Reed Switchs
int latchPinReed = 8;
int dataPinReed = 9;
int clockPinReed = 7;

int registerValue_rank1 = 0;
int RegisterValue_rank2 = 0;
int RegisterValue_rank3 = 0;
int RegisterValue_rank4 = 0;
int RegisterValue_rank5 = 0;
int RegisterValue_rank6 = 0;
int RegisterValue_rank7 = 0;
int RegisterValue_rank8 = 0;

void setup() {
 Serial.begin(9600);
 
 //set pins to output so you can control the shift register
 pinMode(latchPin, OUTPUT);
 pinMode(clockPin, OUTPUT);
 pinMode(dataPin, OUTPUT);

 pinMode(latchPinReed, OUTPUT);
 pinMode(clockPinReed, OUTPUT); 
 pinMode(dataPinReed, INPUT);
 
}

void loop() {
 
 scanBoard();
 
}

void scanBoard(){
 digitalWrite(latchPinReed, 1);
 digitalWrite(clockPinReed, 1);
 delayMicroseconds(20);
 digitalWrite(latchPinReed, 0);

 
 
 registerValue_rank1 = shiftIn(dataPinReed, clockPinReed, MSBFIRST);
 registerValue_rank2 = shiftIn(dataPinReed, clockPinReed, MSBFIRST);
 registerValue_rank3 = shiftIn(dataPinReed, clockPinReed, MSBFIRST);
 registerValue_rank4 = shiftIn(dataPinReed, clockPinReed, MSBFIRST);
 registerValue_rank5 = shiftIn(dataPinReed, clockPinReed, MSBFIRST);
 registerValue_rank6 = shiftIn(dataPinReed, clockPinReed, MSBFIRST);
 registerValue_rank7 = shiftIn(dataPinReed, clockPinReed, MSBFIRST);
 registerValue_rank8 = shiftIn(dataPinReed, clockPinReed, MSBFIRST);
 /*Serial.print("Erster Shift:");
 Serial.println(RegisterValue, BIN);
 Serial.print("Zweiter Shift");
 Serial.println(RegisterValue2, BIN);*/

 //board gets printed to console here
 
 String row1 = String(registerValue_rank1, BIN);
 String row2 = String(registerValue_rank2, BIN);
 String row3 = String(registerValue_rank3, BIN);
 String row4 = String(registerValue_rank4, BIN);
 String row5 = String(registerValue_rank5, BIN);
 String row6 = String(registerValue_rank6, BIN);
 String row7 = String(registerValue_rank7, BIN);
 String row8 = String(registerValue_rank8, BIN);
 
 Serial.println();
 Serial.println(transformRowBin(row8));
 Serial.println(transformRowBin(row7));
 Serial.println(transformRowBin(row6));
 Serial.println(transformRowBin(row5));
 Serial.println(transformRowBin(row4));
 Serial.println(transformRowBin(row3));
 Serial.println(transformRowBin(row2));
 Serial.println(transformRowBin(row1));
 Serial.println();

 //
 
 
 /*Serial.println(RegisterValue, BIN);
 Serial.println(RegisterValue);
 Serial.println("-------------------");*/
 
 digitalWrite(latchPin, LOW);
 
 shiftOut(dataPin, clockPin, MSBFIRST, RegisterValue);
 shiftOut(dataPin, clockPin, MSBFIRST, RegisterValue2);
 digitalWrite(latchPin, HIGH);
 delay(500);
}

String transformRowBin(String row){
 if(row.length() == 1){
   row = "0000000"+row;
 }
 else if(row.length() == 2){
   row = "000000"+row;
 }
 else if(row.length() == 3){
   row = "00000"+row;
 }
 else if(row.length() == 4){
   row = "0000"+row;
 }
 else if(row.length() == 5){
   row = "000"+row;
 }
 else if(row.length() == 6){
   row = "00"+row;
 }
 else if(row.length() == 7){
   row = "0"+row;
 }
 return row;
}
