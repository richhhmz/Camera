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
    
//    console.log("pan= " + req.body.pan +
//    		    ", tilt= " + req.body.tilt +
//    		    ", zoom=", + req.body.zoom);
//    console.log("timestamp="+req.body.timestamp);

//    io.sockets.emit("image",{ message: imageData });
    io.sockets.emit("message",{ message: {image: imageData, timestamp: req.body.timestamp }});
    
    res.writeHead(200, {'Content-Type': 'application/json'});
    res.end(JSON.stringify(
    	{response: {
	    	status: "OK", 
	    	settings: 
	    		{
	    			pan: req.body.pan, 
	    			tilt: req.body.tilt, 
	    			zoom: req.body.zoom
	    		}
    	}}));
});

console.log("Listening on port " + port);
