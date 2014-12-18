/**
 * Diese Datei dient nur zur Demostration einiger Funktionen
 */ 
$(document).ready(function() {	
	$('#theatre-recentroute').find('.e__list-big-element').on('click',function() {
		$(this).toggleClass('is__expanded');
	});
});