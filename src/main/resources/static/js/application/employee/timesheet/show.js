$(document).ready(function() {
	
    // page is now ready, initialize the calendar...
    $('#calendar').fullCalendar({
		disableResizing : false,
		height: "auto",
        
        events: function(start, end, timezone, callback) {
            $.ajax({
                url: '/calendar/timesheet/'+employeeId,
                dataType: 'json',
                data: {
                    // our hypothetical feed requires UNIX timestamps
                    start: start.unix(),
                    end: end.unix()
                },
                success: function(data) {
                	var events = [];
                	
                    data.forEach(function(event) {
                        events.push({
                        	title : event.title,
                        	description : event.description,
							start : new Date(event.start.year, event.start.monthValue-1, event.start.dayOfMonth),
							end : new Date(event.end.year, event.end.monthValue-1, event.end.dayOfMonth+1),
							rendering: 'background',
							color: event.classNames[1],
							overlap: false,
							allDay: true,
                        });
                    });
                    
                    callback(events);
                }
            });
        },

        windowResize: function (event, ui) {
            $('#calendar').fullCalendar('render');
        }

    });
    
    $(":button.fc-today-button").hide();
    $(":button.fc-corner-left").hide(); 
    $(":button.fc-corner-right").hide();
    
    var m = $.fullCalendar.moment(dateCalendar);
    $('#calendar').fullCalendar( 'gotoDate', m );

});