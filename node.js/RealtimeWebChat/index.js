var express = require("express");
var fs = require('fs');
var base64 = require("./base64.js");
var bodyParser = require('body-parser');
var app = express();
var port = 3700;
var ImageA = "nopic.b64";
var image = ImageA;
var imageData;

app.set('views', __dirname + '/tpl');
app.set('view engine', "jade");
app.engine('jade', require('jade').__express);
app.use(express.static(__dirname + '/public')); 
app.use(bodyParser.urlencoded({
	extended: true
}));
var io = require('socket.io').listen(app.listen(port));

function sendImage(){
    //image = (image == ImageA?ImageB:ImageA);
    
    fs.readFile(image, function (err, data) {
	io.sockets.emit("image",{ message: String.fromCharCode.apply(null, new Uint16Array(data)) });
    });

}

function displayImage(){
    clearTimeout();

    io.sockets.emit("image",{ message: imageData });
    setTimeout(function () {displayImage()}, 1000);
    
}

io.sockets.on('connection', function (socket) {
    console.log("connection!");
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

    imageData = req.body.image;
    //console.log(imageData);
    displayImage();
    
    res.writeHead(200, {'Content-Type': 'text/plain'});
    res.end("OK");
});

//function decodeBase64Image(dataString) {
//    var matches = dataString.match(/^data:([A-Za-z-+\/]+);base64,(.+)$/),
//        response = {};
//
//    if (matches.length !== 3) {
//        return new Error('Invalid input string');
//    }
//
//    response.type = matches[1];
//    response.data = new Buffer(matches[2], 'base64');
//
//    return response;
//}

console.log("Listening on port " + port);
//sendImage();
