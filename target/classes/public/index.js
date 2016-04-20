var canvasEl = document.getElementById("world-map");
var ctx = canvasEl.getContext("2d");

canvasEl.height = window.innerHeight;
canvasEl.width = window.innerWidth;
var background = new Image();
background.src = "map_3mb.jpg";

var redDot = new Image();
redDot.src = "dot_red.gif";

// Make sure the image is loaded first otherwise nothing will draw.
background.onload = function(){
  ctx.drawImage(background,0,0,canvasEl.width, canvasEl.height);
}

var equator = canvasEl.height/2;
var primeMeridian = canvasEl.width/2;

function calculateX (lat) {
  floatLat = parseFloat(lat);
  // South of equator is considered negative -__-
  x = equator - floatLat/90 * equator;
  return x;
}

function calculateY (lon) {
  floatLon = parseFloat(lon);
  y = primeMeridian + floatLon/180 * primeMeridian;
  return y;
}

Pusher.log = function(message) {
  if (window.console && window.console.log) {
    window.console.log(message);
  }
};

var pusher = new Pusher('d666fb92d6623055b4a1', {
  encrypted: true
});

var channel = pusher.subscribe('client-data');

channel.bind('rsvp', function(data) {
  var obj = JSON.parse(data);
  var x = calculateX(obj.lat);
  var y = calculateY(obj.lon);
  ctx.drawImage(redDot, x, y);
  console.log(obj.city);
});