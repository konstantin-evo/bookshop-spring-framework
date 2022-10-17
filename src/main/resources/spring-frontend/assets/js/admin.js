$(document).ready(function () {

    // function to validate the Book form and send the data on success
    $('#createBookSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateBook()) {
            sendBookData()
        }
    });

    // function for interactive display of information on the "Admin" page
    $(".Tabs-link").click(function () {
        $(".Tabs-link").removeClass("Tabs-link_ACTIVE");
        $(this).addClass("Tabs-link_ACTIVE");
        $(".Tabs-block").hide();

        let id = $(this).attr('id');

        switch (id) {
            case "book_create":
                $("#createBookForm").closest('.Tabs-block').show();
                break;
            default:
                $(".Topup-wrap").closest('.Tabs-block').show();
        }
    });

});

function sendBookData() {
    let $form = $('#createBookForm');
    let data = getFormData($form);

    $.post({
        url: '/api/books',
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
