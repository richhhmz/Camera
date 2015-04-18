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
    
    //If the servo receives less than 3, it strains to rotate more than it can.
    //The actual range is 0 to 177 degrees.
    //Also, received numbers 0 through 127, 128 through 255 
    //are converted to 0 through 180.
    if (pos > 127)
      servoTilt.write(round((pos-1)*CONVERSION)-177);
    else
      servoPan.write(round(pos*CONVERSION)+3);
  }
}
