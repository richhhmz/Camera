window.onload = function() {
 
    var messages = [];
    var socket = io.connect(Document.URL);
    var field = document.getElementById("field");
	
    socket.on('image', function (data) {
//		alert("image");
        if(data.message) {
            //alert("image message);
	    document.getElementById("placeholder").src = "data:image/jpeg;base64," + data.message;
        } else {
            console.log("There is a problem:", data);
        }
    });

     sendButton.onclick = function() {
        var text = field.value;
        socket.emit('send', { message: text });
    };
 
}
