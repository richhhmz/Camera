window.onload = function() {
 
    var messages = [];
    var socket = io.connect(Document.URL);
    var field = document.getElementById("field");
	
    socket.on('message', function (data) {
//		alert("image");
        if(data.message) {
            //alert("image message);
	        var image = document.getElementById("image");
	        var timestamp = document.getElementById("timestamp");
	        var pan = document.getElementById("pan");
	        var tilt = document.getElementById("tilt");
	        var zoom = document.getElementById("zoom");
	        var key = document.getElementById("key");
		    image.src = "data:image/jpeg;base64," + data.message.image;
		    timestamp.innerHTML = data.message.timestamp;
		    pan.innerHTML = data.message.settings.pan;
		    tilt.innerHTML = data.message.settings.tilt;
		    zoom.innerHTML = data.message.settings.zoom;
		    key.value = "";
        } else {
            console.log("There is a problem:", data);
        }
    });
    
    key.onkeydown = function(event) {
    	var charCode = (event.which) ? event.which : event.keyCode
    	this.value = "";
    	if (charCode == 87) this.value=("left 1 degree"); // w
    	if (charCode == 65) this.value=("up 1 degree"); // a
    	if (charCode == 66) this.value=("right 1 degree"); // s
    	if (charCode == 67) this.value=("down 1 degree"); // d
    	if (charCode == 37) this.value=("left 5 degree"); // left
    	if (charCode == 38) this.value=("up 5 degrees"); // up
    	if (charCode == 39) this.value=("right 5 degrees"); // right
    	if (charCode == 40) this.value=("down 5 degrees"); // down
    	if (charCode == 73) this.value=("up 30 degrees"); // i
    	if (charCode == 74) this.value=("left 30 degrees"); // j
    	if (charCode == 75) this.value=("down 30 degrees"); // k
    	if (charCode == 76) this.value=("right 30 degrees"); // l
    	if (charCode == 33) this.value=("zoom in"); // page up
    	if (charCode == 34) this.value=("zoom out"); // page down
    	socket.emit('charCode', charCode.toString());
    	return false;
    };

    sendButton.onclick = function() {
        var text = field.value;
        socket.emit('send', { message: text });
    };
 
    
}
