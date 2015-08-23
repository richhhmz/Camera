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

     sendButton.onclick = function() {
        var text = field.value;
        socket.emit('send', { message: text });
    };
 
}
