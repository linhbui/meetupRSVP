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