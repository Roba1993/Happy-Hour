/**
 * Klasse hinzufuegen wenn app im fullscreenmodus
 */
if ( ( window.navigator.standalone !== false ) && (navigator.userAgent.match(/(iPad|iPhone|iPod)/g) !== null ) ) {
    var elm_html = document.documentElement;
    elm_html.classList.add("is__ios-fullscreen");
}