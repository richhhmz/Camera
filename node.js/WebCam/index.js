var express = require("express");
var fs = require('fs');
var bodyParser = require('body-parser');
var app = express();
var port = 3700;
var imageData;

var pan = 90;
var tilt = 90;
var zoom = 0;
var panMin = 0;
var panMax = 180;
var tiltMin = 0;
var tiltMax = 180;
var zoomMin = 0;
var zoomMax = 15;

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
    socket.on('charCode', function(data) {
    	processCharCode(data);
    });
});

function processCharCode(charCode){
	console.log("enter processCharCode, char="+charCode);
	switch(parseInt(charCode)){
		case 87: // w
			tilt += 1;
			break;
		case 65: // a
			pan -= 1;
			break;
		case 66: // s
			tilt -= 1;
			break;
		case 67: // d
			pan += 1;
			break;
		case 37: // arrow left
			pan -= 5;
			break;
		case 38: // arrow up
			tilt += 5;
			break;
		case 39: // arrow right
			pan += 5;
			break;
		case 40: // arrow down
			tilt -= 5;
			break;
		case 73: // i
			tilt += 30;
			break;
		case 74: // j
			pan -= 30;
			break;
		case 75: // k
			tilt -= 30;
			break;
		case 76: // l
			pan += 30;
			break;
		case 33: // page up
			zoom += 1;
			break;
		case 34: // page down
			zoom -= 1;
			break;
	}
	if(pan < panMin) pan = panMin;
	if(pan > panMax) pan = panMax;
	if(tilt < tiltMin) tilt = tiltMin;
	if(tilt > tiltMax) tilt = tiltMax;
	if(zoom < zoomMin) zoom = zoomMin;
	if(zoom > zoomMax) zoom = zoomMax;
	console.log("code="+charCode+", pan="+pan+", tilt="+tilt+", zoom="+zoom);
}

app.get("/", function(req, res){
    res.render("page");
});

app.post('/', function(req, res){
//    console.log("Received post");
    
	console.log(req.body.message);
	
	var obj = JSON.parse(req.body.message);
	
//	pan=parseInt(obj.message.settings.pan);
//	tilt=parseInt(obj.message.settings.tilt);
//	zoom=parseInt(obj.message.settings.zoom);
	panMin = parseInt(obj.message.ranges.panMin);
	panMax = parseInt(obj.message.ranges.panMax);
	tiltMin = parseInt(obj.message.ranges.tiltMin);
	tiltMax = parseInt(obj.message.ranges.tiltMax);
	zoomMin = parseInt(obj.message.ranges.zoomMin);
	zoomMax = parseInt(obj.message.ranges.zoomMax);
	
    io.sockets.emit("message", JSON.parse(req.body.message));
    
    res.writeHead(200, {'Content-Type': 'application/json'});
    res.end(JSON.stringify(
    	{response: {
	    	status: "OK", 
	    	settings: 
	    		{
	    			pan: "" + pan, 
	    			tilt: "" + tilt, 
	    			zoom: "" + zoom
	    		}
    	}}));
});

console.log("Listening on port " + port);
