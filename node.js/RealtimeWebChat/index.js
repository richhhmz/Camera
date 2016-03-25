var express = require("express");
var app = express();
var port = 3700;

app.set('views', __dirname + '/tpl');
app.set('view engine', "jade");
app.engine('jade', require('jade').__express);
app.use(express.static(__dirname + '/public')); 
var io = require('socket.io').listen(app.listen(port));

io.sockets.on('connection', function (socket) {
	console.log("connection!");
    socket.emit('message', { message: 'welcome to the chat' });
    socket.on('send', function (data) {
		console.log("send!");
        io.sockets.emit('message', data);
    });
});

app.get("/", function(req, res){
    res.render("page");
});

console.log("Listening on port " + port);
