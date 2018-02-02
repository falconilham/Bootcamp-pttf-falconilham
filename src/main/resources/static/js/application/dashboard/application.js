$(document).ready(function() {	
	
	var date = new Date();
	var calbeginDay = new Date(date.getFullYear(), date.getMonth(), 1);
	
    // page is now ready, initialize the calendar...
    var fullcalendar = $('#calendar').fullCalendar({
		
    	disableResizing : false,
		height: "auto",

        select: function (start, end, allDay) {
            var title = prompt('Event Title:');
            if (title) {
                calendar.fullCalendar('renderEvent', {
                        title: title,
                        start: start,
                        end: end,
                        allDay: allDay
                    }, true // make the event "stick"
                );
            }
            calendar.fullCalendar('unselect');
        },
        
        events: function(start, end, timezone, callback) {
            $.ajax({
                url: '/dashboard/calendar',
                dataType: 'json',
                data: {
                    // our hypothetical feed requires UNIX timestamps
                    start: start.unix(),
                    end: end.unix()
                },
                success: function(data) {
                	var events = [];
                    data.forEach(function(event) {
                    	console.log(event);
                        events.push({
                        	title : event.title,
							start : new Date(event.start.year, event.start.monthValue-1, event.start.dayOfMonth),
							end : new Date(event.end.year, event.end.monthValue-1, event.end.dayOfMonth),
							description : event.description,
							allDay: true,
							className : event.classNames
                        });
                    });
                    callback(events);
                }
            });
        },

        eventRender: function (event, element, icon) {
            if (!event.description == "") {
                element.find('.fc-title').append('<br/><span class= "ultra-light" >' + event.description + "</span>");
            }
        },
        
        firstDay:calbeginDay.getDay(),

        windowResize: function (event, ui) {
            $('#calendar').fullCalendar('render');
        }

    });
    
    Highcharts.chart('container', {
    	title: {
            text: null
        },

        yAxis: {
        },

        xAxis: {
            categories: dataX
        },
        
        series: [{
        	name: null,
            data: dataY
        }]
    });
    
$('td.fc-other-month').text('');
    
    $('.fc-next-button').click(function (){
    	$('td.fc-other-month').text('');
    });
    
    $('.fc-prev-button').click(function (){
    	$('td.fc-other-month').text('');
    });

});