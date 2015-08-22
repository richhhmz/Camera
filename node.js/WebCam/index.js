var express = require("express");
var fs = require('fs');
var bodyParser = require('body-parser');
var app = express();
var port = 3700;
var imageData;

app.set('views', __dirname + '/tpl');
app.set('view engine', "jade");
app.engine('jade', require('jade').__express);
app.use(express.static(__dirname + '/public')); 
app.use(bodyParser.urlencoded({
	extended: true
}));
var io = require('socket.io').listen(app.listen(port));

io.sockets.on('connection', function (socket) {
    console.log("connection!");
    socket.on('send', function (data) {
//        console.log("send!");
        io.sockets.emit('message', data);
    });
});

app.get("/", function(req, res){
    res.render("page");
});

app.post('/', function(req, res){
//    console.log("Received post");
    imageData = req.body.image;
    io.sockets.emit("image",{ message: imageData });
    
    res.writeHead(200, {'Content-Type': 'text/plain'});
    res.end("OK");
});

console.log("Listening on port " + port);
