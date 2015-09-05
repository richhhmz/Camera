#include <Servo.h> 
#define CONVERSION 1.41732283464567
 
Servo servoPan;
Servo servoTilt;
int pos = 0;

void setup() {
  Serial.begin(9600);
  servoTilt.attach(9);
  servoPan.attach(10);
}

void loop() {
  if (Serial.available() > 0) {
    pos = Serial.read();
    
    if (pos > 127)
      servoTilt.write(round((pos-1)*CONVERSION)-180);
    else
      servoPan.write(round(pos*CONVERSION));
  }
}
