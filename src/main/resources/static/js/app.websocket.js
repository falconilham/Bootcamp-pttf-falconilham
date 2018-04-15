var stompClient = null;

function setConnected(connected) {
	if (connected)
		$("#activity").addClass("txt-color-green");
	else
		$("#activity").addClass("txt-color-grey");
    if (connected) {
        $("#conversation").show();
    }
    else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    var socket = new SockJS('/notifications');
    stompClient = Stomp.over(socket);
    stompClient.connect({}, function (frame) {
        setConnected(true);
        stompClient.subscribe('/user/queue/notifications/unread', function (numberOfUnreadMessage) {
        	if (parseInt(numberOfUnreadMessage.body) > 9)
        		$('#activity b').text('9+');
        	else
        		$('#activity b').text(numberOfUnreadMessage.body);
        	
        	var audio = new Audio('/sound/smallbox.mp3');
        	audio.play();
        });
        
    });
}


function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}


$(function () {
	setConnected(false);
	connect();
});