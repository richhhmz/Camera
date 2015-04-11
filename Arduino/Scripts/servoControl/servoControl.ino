#include <Servo.h> 
 
Servo myservo;
int pos = 0;

void setup() {
  Serial.begin(9600);
  myservo.attach(9);  
}

void loop() {
  if (Serial.available() > 0) {
    pos = Serial.read();
    
    //If the servo receives less than 3, it strains to rotate more than it can.
    //The actual range is 0 to 177 degrees.
    myservo.write(pos+3);
  }
}
