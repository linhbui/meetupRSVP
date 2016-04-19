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
  console.dir(data);
});