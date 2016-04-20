var canvasEl = document.getElementById("world-map");
var ctx = canvasEl.getContext("2d");

canvasEl.height = window.innerHeight;
canvasEl.width = window.innerWidth;

var background = new Image();
background.src = "map_3mb.jpg";

// Make sure the image is loaded first otherwise nothing will draw.
background.onload = function(){
  ctx.drawImage(background,0,0,canvasEl.width, canvasEl.height);
}

var redDot = new Image();
redDot.src = "dot_red.gif";

var equator = canvasEl.height/2;
var primeMeridian = canvasEl.width/2;

function calculateY (lat) {
  floatLat = parseFloat(lat);
  // South of equator is considered negative -__-
  y = equator - floatLat/90 * equator;
  return y;
}

function calculateX (lon) {
  floatLon = parseFloat(lon);
  x = primeMeridian + floatLon/180 * primeMeridian;
  return x;
}

var pusher = new Pusher('d666fb92d6623055b4a1', {
  encrypted: true
});

var channel = pusher.subscribe('client-data');

channel.bind('rsvp', function(data) {
  var obj = JSON.parse(data);
  var x = calculateX(obj.lon);
  var y = calculateY(obj.lat);
  ctx.drawImage(redDot, x, y);
  console.log(obj.city);
});