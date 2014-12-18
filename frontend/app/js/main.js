/**
 * Patrick
 * Klasse hinzufuegen wenn app im fullscreenmodus
 */
if ( ( window.navigator.standalone !== false ) && (navigator.userAgent.match(/(iPad|iPhone|iPod)/g) !== null ) ) {
    var elm_html = document.documentElement;
    elm_html.classList.add("is__ios-fullscreen");
}

/**
 * Sidebar - Simon
 */ 
$(document).ready(function () {
    $("#radius").ionRangeSlider({
        grid: false,
        min: 0.1,
        max: 10,
        step: 0.1,
        from: 1,
        hide_min_max: true
    });
    $("#time").ionRangeSlider({
        type: "double",
        grid: false,
        values: ["12:00", "13:00", "14:00", "15:00", "16:00", "17:00",
                 "18:00","19:00","20:00","21:00","22:00","23:00","24:00",
                 "01:00","02:00","03:00","04:00","05:00","06:00","07:00",
                 "08:00","09:00","10:00","11:00","12:00"],
        from: 8,
        to: 15,
        hide_min_max: true
    });
    $("#resttime").ionRangeSlider({
        grid: false,
        min: 0,
        max: 6,
        step: 0.5,
        from: 1,
        hide_min_max: true
    });
});