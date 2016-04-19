Pusher.log = function(message) {
  if (window.console && window.console.log) {
    window.console.log(message);
  }
};

var pusher = new Pusher('d666fb92d6623055b4a1', {
  encrypted: true
});

var channel = pusher.subscribe('test_channel');

channel.bind('my_event', function(data) {
  alert(data.message);
});