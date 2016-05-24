var express = require('express'),
    request = require('request'),
    errorHandler = require('errorhandler'),
    app = express(),
	proxy = require('express-http-proxy');
var HOSTNAME = 'localhost',
    PORT = 8080,
    PUBLIC_DIR = __dirname + '/public_html';

var request_counter = 0;

app.use(function (req, res, next) {
	var request_time = new Date();
	console.log('Time: ', request_time.toString());
	console.log("Requests count: ", ++request_counter);
	next();
});
app
	.use('/', express.static(PUBLIC_DIR))
	.use(errorHandler());

app.listen(PORT,HOSTNAME,function () {
	console.log("Simple static server showing %s listening at http://%s:%s", PUBLIC_DIR, HOSTNAME, PORT);
});
app.use(
	'/api',
	proxy(
		'http://localhost', {
			port: 8090,
			forwardPath: function (req, res) {
				console.log("proxy: [%s %s %s]", req.method, req.originalUrl);
				return '/api' + require('url').parse(req.url).path;
			}
		}
	)
);


