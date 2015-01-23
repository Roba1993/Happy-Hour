/**
 * Diese Datei dient nur zur Demostration einiger Funktionen
 */ 
$(document).ready(function() {	
	$('#theatre-recentroute').find('.e__list-big-element-frontside').on('click',function() {
		$(this).parents('.e__list-big-element').toggleClass('is__expanded');
	});
	$('#trigger-sidebar').on('click', function() {
		$('#main').toggleClass('sidebar-is-open');
	});
});