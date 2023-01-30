$(document).ready(function () {

    $('#orderBook').click(function (e) {
        e.preventDefault();
        $.post({
            url: '/books/order',
            success: function (result) {
                if (result.validated) {
                    $('.Cart-product').remove();
                    $('.Cart-total').remove();
                    $('<div class="Section"><div class="Section-header"><h1 class="Section-title">Books purchased - enjoy the reading!</h1></div></div>')
                        .insertAfter('.Cart').first();
                } else {
                    $.each(result.errorMessages, function (key, value) {
                        $('<div class="Cart-block Cart-block_total"><strong class="Cart-title">' + value + '</strong></div>')
                            .insertAfter('.Cart').first();
                    });
                }
            },
            error: function (jqXhr, textStatus, errorThrown) {
                console.log(errorThrown);
            }
        });
    });
})