var express = require("express");
var fs = require('fs');
var base64 = require("./base64.js");
var bodyParser = require('body-parser');
var app = express();
var port = 3700;
var ImageA = "image_a.b64";
var ImageB = "image_b.b64";
var image = ImageA;

app.set('views', __dirname + '/tpl');
app.set('view engine', "jade");
app.engine('jade', require('jade').__express);
app.use(express.static(__dirname + '/public')); 
app.use(bodyParser.urlencoded({
	extended: true
}));
var io = require('socket.io').listen(app.listen(port));

/* These prove a string can be encoded/decoded, but it looks like the Base64.js won't be necessary.
var hw = "Hello, World!";
var hw64 = base64.encode(hw);
var hw64dec = base64.decode(hw64);
*/
function sendImage(){
    image = (image == ImageA?ImageB:ImageA);
    fs.readFile(image, function (err, data) {
        clearTimeout();
	//if (err) throw err;
//		console.log("data="+data);
	io.sockets.emit('image',{ message: String.fromCharCode.apply(null, new Uint16Array(data)) });
	setTimeout(function () {sendImage()}, 2000);
    });
}

io.sockets.on('connection', function (socket) {
        console.log("connection!");
        //socket.emit('message', { message: 'welcome to the chat' });
        //socket.emit('message', { message: hw64dec });
        sendImage();
        socket.on('send', function (data) {
        console.log("send!");
        io.sockets.emit('message', data);
    });
});

app.get("/", function(req, res){
    res.render("page");
});

app.post('/', function(req, res){
	console.log("Received post");
	
	// Replace with code to download the image to the browser.
	console.log(req.body.image);
	
	res.writeHead(200, {'Content-Type': 'text/plain'});
	res.end("OK");
});

console.log("Listening on port " + port);
//sendImage();
