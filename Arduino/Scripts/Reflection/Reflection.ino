void setup() {
	Serial.begin(9600);
}

void loop() {

	if (Serial.available() > 0) {
		int chr = Serial.read();
		Serial.print((char)chr);
		Serial.print("[");
		Serial.print(chr, HEX);
		Serial.print("]");
	}
}

