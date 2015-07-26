var express = require("express");
var fs = require('fs');
var base64 = require("./base64.js");
var app = express();
var port = 3700;
var dotsImage = "dots.b64"
var squigImage = "squig.b64"
var image = dotsImage;

app.set('views', __dirname + '/tpl');
app.set('view engine', "jade");
app.engine('jade', require('jade').__express);
app.use(express.static(__dirname + '/public')); 
var io = require('socket.io').listen(app.listen(port));

function sendImage(){
	image = (image == dotsImage?squigImage:dotsImage);
	fs.readFile(image, function (err, data) {
		clearTimeout();
		if (err) throw err;
//		console.log("data="+data);
		io.sockets.emit('image',{ message: String.fromCharCode.apply(null, new Uint16Array(data)) });
		setTimeout(function () {sendImage()}, 1000);
	});
}

io.sockets.on('connection', function (socket) {
	console.log("connection!");
    socket.emit('message', { message: 'welcome to the chat' });
	sendImage();
    socket.on('send', function (data) {
		console.log("send!");
        io.sockets.emit('message', data);
    });
});

app.get("/", function(req, res){
    res.render("page");
});

console.log("Listening on port " + port);
//sendImage();
