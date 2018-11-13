#include <Stepper.h>
#include <Servo.h>


Servo bottom_servo; //Main Servo at the bottom of the mechanical arm. Model: BMS 410-C from Conrad Electronics. 
Servo top_servo;    //Secondary Servo on top of the mechanical arm. Model: 9g Standard Servo for Arduino.
Stepper stepper(200, 10,11,12,13);  //A stepper Motor we found in the storage room. It's bipolar, thats all we know. 

int stepper_1 = 13, stepper_2 = 12, stepper_3 = 11, stepper_4 = 10;
int bottom_servo_pin = 9;
int top_servo_pin = 6;

int coordinates[64][3] = {
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                          {0,0,0},
                      };
void setup(){
  pinMode(stepper_1, OUTPUT);
  pinMode(stepper_2, OUTPUT);
  pinMode(stepper_3, OUTPUT);
  pinMode(stepper_4, OUTPUT); 
  
  stepper.setSpeed(100);

  bottom_servo.attach(bottom_servo_pin);
  top_servo.attach(top_servo_pin);
}

void loop(){

   /* Tests if servos are working */
   stepper.step(200);
   delay(10);

   bottom_servo.write(30);
   top_servo.write(30);
   delay(1000);
   bottom_servo.write(120);
   top_servo.write(120);
   delay(1000);
}


