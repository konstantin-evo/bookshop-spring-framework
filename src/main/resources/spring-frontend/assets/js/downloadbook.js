$(document).ready(function () {
    $('a.btn').click(function (event) {
        event.preventDefault();
        $('#download-popup-overlay').fadeIn(297,function () {
            $('#download-popup').css('display', 'block').animate({opacity:1}, 198);
        });
    });

    $('#download-popup-close').click(function () {
        $('#download-popup').animate({opacity: 1}, 198, function () {
            $(this).css('display', 'none');
            $('#download-popup-overlay').fadeOut(297);
        });
    });

    $('#download-popup-overlay').click(function () {
        $('#download-popup').animate({opacity: 1}, 198, function () {
            $(this).css('display', 'none');
            $('#download-popup-overlay').fadeOut(297);
        });
    });

});