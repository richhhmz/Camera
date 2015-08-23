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
		    image.src = "data:image/jpeg;base64," + data.message.image;
		    timestamp.innerHTML = data.message.timestamp;
        } else {
            console.log("There is a problem:", data);
        }
    });
    
    key.onkeydown = function(event) {
    	var charCode = (event.which) ? event.which : event.keyCode
    	this.value = "";
    	if (charCode == 37) this.value=("left 5 degrees"); // left
    	if (charCode == 38) this.value=("up 5 degrees"); // up
    	if (charCode == 39) this.value=("right 5 degrees"); // right
    	if (charCode == 40) this.value=("down 5 degrees"); // down
    	if (charCode == 73) this.value=("up 30 degrees"); // i
    	if (charCode == 74) this.value=("left 30 degrees"); // j
    	if (charCode == 75) this.value=("down 30 degrees"); // k
    	if (charCode == 76) this.value=("right 30 degrees"); // l
    	return false;
    };

    sendButton.onclick = function() {
        var text = field.value;
        socket.emit('send', { message: text });
    };
 
    
}
