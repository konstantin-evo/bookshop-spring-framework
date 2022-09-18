$(document).ready(function () {

    // function to validate the Book form and send the data on success
    $('#createBookSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateBook()) {
            let data = getFormData($form);
            sendBookData()
        }
    });

});

function sendBookData() {
    let $form = $('#createBookForm');
    let data = getFormData($form);

    $.post({
        url: '/book',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        success: function (result) {

            let parentDiv = $('#createBookSubmit').parent();

            if (result.validated) {
                $('<div class="Profile-success">Book successfully saved</div>')
                    .insertAfter(parentDiv);
            } else {
                $.each(result.errorMessages, function (key, value) {
                    $('<div class="error"><span>' + value + '</span></div>')
                        .insertAfter(parentDiv);
                });
            }
        }
    })
}

function validateBook() {
    let validator = $("#createBookForm").validate({
        rules: {
            title: "required",
            author: "required",
            description: "required",
            pubDate: "required",
            genre: "required",
            price: {
                required: true,
                digits: true,
                min: 0
            },
            discount: {
                required: true,
                digits: true,
                min: 0,
                max: 100
            }
        }
    });
    return !!validator.form();
}
