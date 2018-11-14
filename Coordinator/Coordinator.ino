#include <Stepper.h>
#include <Servo.h>


Servo bottom_servo; //Main Servo at the bottom of the mechanical arm. Model: BMS 410-C from Conrad Electronics. 
Servo top_servo;    //Secondary Servo on top of the mechanical arm. Model: 9g Standard Servo for Arduino.
Stepper stepper(200, 10,11,12,13);  //A stepper Motor we found in the storage room. It's bipolar, thats all we know. 

int stepper_1 = 5, stepper_2 = 4, stepper_3 = 3, stepper_4 = 2;
int bottom_servo_pin = 10;
int top_servo_pin = 11;

void setup() {

  Serial.begin(9600);
  
  pinMode(stepper_1, OUTPUT);
  pinMode(stepper_2, OUTPUT);
  pinMode(stepper_3, OUTPUT);
  pinMode(stepper_4, OUTPUT); 
  
  stepper.setSpeed(100);

  bottom_servo.attach(bottom_servo_pin);
  top_servo.attach(top_servo_pin);

  pinMode(13, INPUT); //Poti stepper
  pinMode(12, OUTPUT); //Poti bottom_servo
  pinMode(11, OUTPUT); //Poti top_servo

}

void loop() {
  int topVal = map(digitalRead(13), 0, 1024, 0, 120);
  int bottomVal = map(digitalRead(12), 0, 1024, 0, 120);
  int stepperVal = map(digitalRead(11), 0, 1024, 0, 200);

  stepper.step(stepperVal);
  delay(10);
  top_servo.write(topVal);
  bottom_servo.write(bottomVal);
  delay(100);

  Serial.println("+++++++++++++++++++++++++++++++++++++");
  Serial.print("Stepper Value: "); Serial.println(stepperVal);
  Serial.print("Bottom Servo Value: "); Serial.println(bottomVal);
  Serial.print("Top Servo Value: "); Serial.println(topVal);
  Serial.println("++++++++++++++++++++++++++++++++++++++++");
  Serial.println("");
}
