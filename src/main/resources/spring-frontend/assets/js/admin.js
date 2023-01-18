$(document).ready(function () {

    // function to validate the Book form and send the data on success
    $('#createBookSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateBookToCreate()) {
            sendCreateBookRequest()
        }
    });

    // function to upload Book data to edit further
    $('#uploadBookDataSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateSlug($("#uploadBookDataToEdit"))) {
            sendGetBookRequest()
        }
    });

    // function to validate Slug input and send the data on success
    $('#editBookSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateBookToEdit()) {
            sendEditBookRequest()
        }
    });

    // function to validate Slug input and send the data on success
    $('#editAuthorSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateAuthorToEdit()) {
            sendEditAuthorRequest()
        }
    });

    // function to validate Slug input and send the data on success
    $('#deleteBookSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateSlug($("#deleteBookForm"))) {
            sendDeleteBookRequest()
        }
    });

    // function to validate Slug input and upload current book cover to change it further
    $('#uploadBookCoverSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateUploadBookCoverForm()) {
            sendUploadBookCoverRequest()
        }
    });

    // function to upload Author data to edit further
    $('#uploadAuthorDataSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateSlug($("#uploadAuthorDataToEdit"))) {
            sendGetAuthorRequest()
        }
    });

    // function to create bookToUser entity (made book paid, archived, etc. by user)
    $('#createBookToUserSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateBookToUserCreate()) {
            sendCreateBookToUserRequest()
        }
    });

    // function to upload Reviews data to moderate by Admin role further
    $('#uploadUserBooksDataSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateUserId()) {
            sendGetBooksByUserRequest()
        }
    });

    // function to change BookToUserType (the status of the User's book)
    $(document).on('click', '.btn_book_to_user', function (e) {
        e.preventDefault();
        sendPatchBookToUserRequest($(this), $(this).attr("data-bookToUserId"))
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
            case "book_edit":
                $("#editBookForm").closest('.Tabs-block').show();
                break;
            case "book_remove":
                $("#deleteBookForm").closest('.Tabs-block').show();
                break;
            case "book_cover":
                $("#uploadBookCoverForm").closest('.Tabs-block').show();
            default:
                $(".Topup-wrap").closest('.Tabs-block').show();
        }
    });

});

function sendCreateBookRequest() {
    let $form = $('#createBookForm');
    let data = getFormData($form);
    let parentElement = $('#createBookSubmit').parent();

    $.post({
        url: '/api/books',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        success: function (validatedResponseDto) {
            processValidatedResponseDto(validatedResponseDto, parentElement, "Book successfully saved")
        }
    })
}

function sendEditBookRequest() {
    let slug = $('#slugEdit').val();
    let parentElement = $('#editBookSubmit').parent();

    let data = {
        "title": $('#bookTitle').val(),
        "author": $('#bookAuthor').val(),
        "description": $('#bookDescription').val(),
        "pubDate": $('#bookPubDate').val(),
        "price": $('#bookPrice').val(),
        "discount": $('#bookDiscount').val(),
        "isBestseller": $('#isBookBestseller').val()
    };

    $.ajax({
        url: '/api/books/' + slug,
        type: 'PATCH',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        success: function (validatedResponseDto) {
            processValidatedResponseDto(validatedResponseDto, parentElement, "Book successfully edited")
        },
        error: function (errorResponse) {
            let error = errorResponse.responseJSON;
            createErrorElement(parentElement, error.message)

        }
    })
}

function sendEditAuthorRequest() {
    let slug = $('#slugAuthor').val();
    let parentElement = $('#editAuthorSubmit').parent();

    let data = {
        "lastName": $('#lastName').val(),
        "firstName": $('#firstName').val(),
        "description": $('#authorDescription').val(),
    };

    $.ajax({
        url: '/api/authors/' + slug,
        type: 'PATCH',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        success: function (validatedResponseDto) {
            processValidatedResponseDto(validatedResponseDto, parentElement, "Author successfully edited")
        },
        error: function (errorResponse) {
            let error = errorResponse.responseJSON;
            createErrorElement(parentElement, error.message)
        }
    })
}

function sendDeleteBookRequest() {
    let slug = $('#slugDelete').val();
    let parentElement = $('#deleteBookSubmit').parent();

    $.ajax({
        url: '/api/books/' + slug,
        type: 'DELETE',
        success: function (validatedResponseDto) {
            processValidatedResponseDto(validatedResponseDto, parentElement, "Book successfully deleted")
        }
    })
}

function sendUploadBookCoverRequest() {
    let slug = $('#slugUploadCover').val();
    let parentElement = $('#uploadBookCoverForm').parent();

    // From the form we only need to send the file (slug is used to specify the path variable)
    let data = new FormData();
    data.append('file', $('#fileUploadCover')[0].files[0]);

    $.post({
        url: '/api/books/' + slug + '/img/save',
        data: data,
        dataType: 'json',
        enctype: 'multipart/form-data',
        processData: false,
        contentType: false,
        cache: false,
        success: function (validatedResponseDto) {
            processValidatedResponseDto(validatedResponseDto, parentElement, "Book successfully deleted")
        },
        error: function (errorResponse) {
            let error = errorResponse.responseJSON;
            createErrorElement(parentElement, error.message)
        }
    })
}

function sendGetBookRequest() {
    let slug = $('#slugEdit').val();
    let parentElement = $('#uploadBookDataToEdit').parent();
    $.ajax({
        url: '/api/books/' + slug,
        type: 'GET',
        dataType: 'json',
        success: function (result) {

            // in case of business error we have 200 HTTP Status
            // and info about error in ValidatedResponseDto object
            if (!result.validated) {
                $.each(result.errorMessages, function (key, value) {
                    createErrorElement(parentElement, value)
                });
            }

            // check that the book is present in response
            if (result.id != null) {
                let authorName = result.author.firstName + ', ' + result.author.lastName
                $("#editBookForm").show("fast");
                $("#bookTitle").val(result.title);
                $("#bookTitle").prop("disabled", false);
                $("#bookAuthor").val(authorName);
                $("#bookAuthor").prop("disabled", false);
                $("#bookDescription").val(result.description);
                $("#bookDescription").prop("disabled", false);
                $("#bookPubDate").val(result.pubDate);
                $("#bookPubDate").prop("disabled", false);
                $("#bookPrice").val(result.price);
                $("#bookPrice").prop("disabled", false);
                $("#bookDiscount").val(result.discount);
                $("#bookDiscount").prop("disabled", false);
                $("#isBookBestseller").val(result.isBestseller);
                $("#isBookBestseller").prop("disabled", false);
            }
        },
        // in case of technical and unexpected error
        error: function (errorResponse) {
            let error = errorResponse.responseJSON;
            createErrorElement(parentElement, error.message)
        }
    })
}

function sendGetAuthorRequest() {
    let slug = $('#slugAuthor').val();
    let parentElement = $('#uploadAuthorDataToEdit').parent();
    $.ajax({
        url: '/api/authors/' + slug,
        type: 'GET',
        dataType: 'json',
        success: function (result) {

            // in case of business error we have 200 HTTP Status
            // and info about error in ValidatedResponseDto object
            if (!result.validated) {
                $.each(result.errorMessages, function (key, value) {
                    createErrorElement(parentElement, value)
                });
            }

            // check that the book is present in response
            if (result.id != null) {
                $("#editAuthorForm").show("fast");
                $("#lastName").val(result.lastName);
                $("#lastName").prop("disabled", false);
                $("#firstName").val(result.firstName);
                $("#firstName").prop("disabled", false);
                $("#authorDescription").val(result.description);
                $("#authorDescription").prop("disabled", false);
            }
        },
        // in case of technical and unexpected error
        error: function (errorResponse) {
            let error = errorResponse.responseJSON;
            createErrorElement(parentElement, error.message)
        }
    })
}

function sendCreateBookToUserRequest() {
    let $form = $('#createBookToUserForm');
    let data = getFormData($form);
    let parentElement = $('#createBookToUserSubmit').parent();

    $.post({
        url: '/api/book-to-user',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        success: function (validatedResponseDto) {
            processValidatedResponseDto(validatedResponseDto, parentElement, "Book successfully saved")
        }
    })
}

function sendGetBooksByUserRequest() {
    let userId = $('#userId').val();
    let parentElement = $('#uploadUserBooksData').parent();
    $.ajax({
        url: '/api/book-to-user?userId=' + userId,
        type: 'GET',
        dataType: 'json',
        success: function (response) {

            // in case of business error we have 200 HTTP Status
            // and info about error in ValidatedResponseDto object
            if (!response.validated) {
                $.each(response.errorMessages, function (key, value) {
                    createErrorElement(parentElement, value)
                });
            }

            createBookToUserBlock(parentElement, response)

        },
        // in case of technical and unexpected error
        // we have 4**, 5** status and other error handling
        error: function (errorResponse) {
            let error = errorResponse.responseJSON;
            createErrorElement(parentElement, error.message)
        }
    })
}

function sendPatchBookToUserRequest(changeBookStatusButton, id) {
    let parentElement = changeBookStatusButton.closest('.Admin-btn');
    let data = getFormData($(`form[data-bookToUserId="${id}"]`).first());

    $.ajax({
        url: '/api/book-to-user/' + id,
        type: 'PATCH',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        success: function (validatedResponseDto) {

            // in case of business error we have 200 HTTP Status
            // and info about error in ValidatedResponseDto object
            if (!validatedResponseDto.validated) {
                $.each(response.errorMessages, function (key, value) {
                    createErrorElement(parentElement, value)
                });
            }

            processValidatedResponseDto(validatedResponseDto, parentElement, "Status of the book successfully updated")

        },
        // in case of technical and unexpected error
        error: function (errorResponse) {
            let error = errorResponse.responseJSON;
            createErrorElement(parentElement, error.message)
        }
    })
}

function processValidatedResponseDto(validatedResponseDto, parent, message) {
    if (validatedResponseDto.validated) {
        $('<div class="Profile-success">' + message + '</div>')
            .insertAfter(parent);
    } else {
        $.each(validatedResponseDto.errorMessages, function (key, value) {
            createErrorElement(parent, value)
        });
    }
}

function createErrorElement(parent, message) {
    $('<div class="error"><span>' + message + '</span></div>')
        .insertAfter(parent);
}

function createBookToUserBlock(parent, response) {
    let formBlock = $('<div class="Comments"></div>').insertAfter(parent);
    formBlock.insertAfter(parent)

    response.forEach(bookToUserDto => {

        let id = bookToUserDto.bookToUserId;

        $('<div class="Profile">' +
            '    <form class="form Profile-form" type="submit" data-bookToUserId="' + id + '">' +
            '        <div class="row">' +
            '            <div class="row-block">' +
            '            <div className="form-group form-group_row">' +
            '                <div class="form-group">' +
            '                    <label class="form-label" for="bookToUserId">BookToUser ID</label>' +
            '                    <input class="form-input" name="bookToUserId" type="text" value="' + id + '" disabled/>' +
            '                </div>' +
            '                <div class="form-group">' +
            '                    <label class="form-label" for="userName">User name</label>' +
            '                    <input class="form-input" name="userName" type="text" value="' + bookToUserDto.username + '" disabled/>' +
            '                </div>' +
            '             </div>' +
            '                <div class="form-group">' +
            '                    <label class="form-label" for="bookTitle">Title</label>' +
            '                    <input class="form-input" name="bookTitle" type="text" value="' + bookToUserDto.bookTitle + '" disabled/>' +
            '                </div>' +
            '                <div class="form-group">' +
            '                    <label class="form-label" for="bookSlug">Slug</label>' +
            '                    <input class="form-input" name="bookSlug" type="text" value="' + bookToUserDto.bookSlug + '" disabled/>' +
            '                </div>' +
            '                <div class="form-group">' +
            '                    <label class="form-label" for="bookStatus">Type</label>' +
            '                    <select class="form-input" name="bookStatus" data-bookToUserId="' + id + '">' +
            '                        <option value="KEPT">KEPT</option>' +
            '                        <option value="CART">CART</option>' +
            '                        <option value="PAID">PAID</option>' +
            '                        <option value="ARCHIVED">ARCHIVED</option>' +
            '                        <option value="VIEWED">VIEWED</option>' +
            '                    </select>' +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '        <div class="form-group">' +
            '            <div class="Admin-btn">' +
            '                <button class="btn btn_admin btn_book_to_user" type="submit" data-bookToUserId="' + id + '">Change BookToUser type</button>' +
            '            </div>' +
            '        </div>' +
            '    </form>' +
            '</div>').appendTo(formBlock);

        let select = $(`select[data-bookToUserId="${id}"]`).first();
        select.val(bookToUserDto.bookStatus);
    });
}

function validateBookToCreate() {
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

function validateBookToUserCreate() {
    let validator = $("#createBookToUserForm").validate({
        rules: {
            username: {
                required: true,
                minlength: 3,
            },
            bookSlug: {
                required: true,
                minlength: 3,
                maxlength: 12
            },
            bookStatus: "required"
        }
    });
    return !!validator.form();
}

function validateBookToEdit() {
    let validator = $("#editBookForm").validate({
        rules: {
            bookTitle: "required",
            bookAuthor: "required",
            bookDescription: "required",
            bookPubDate: "required",
            bookPrice: {
                required: true,
                digits: true,
                min: 0
            },
            bookDiscount: {
                required: true,
                digits: true,
                min: 0,
                max: 100
            }
        }
    });
    return !!validator.form();
}

function validateAuthorToEdit() {
    let validator = $("#editAuthorForm").validate({
        rules: {
            firsName: "required",
            lastName: "required",
            authorDescription: "required"
        }
    });
    return !!validator.form();
}

function validateSlug(form) {
    let validator = form.validate({
        // can be improved by using validation using classes rather than id
        // rule is the same for both
        rules: {
            slugDelete: {
                required: true,
                minlength: 3,
                maxlength: 12
            },
            slugEdit: {
                required: true,
                minlength: 3,
                maxlength: 12
            },
            slugAuthor: {
                required: true,
                minlength: 3,
                maxlength: 12
            }
        }
    });
    return !!validator.form();
}

function validateUploadBookCoverForm() {
    let validator = $("#uploadBookCoverForm").validate({
        rules: {
            slugUploadCover: {
                required: true,
                minlength: 3,
                maxlength: 12
            },
            fileUploadCover: {
                required: true
            }
        }
    });
    return !!validator.form();
}

function validateUserId() {
    let validator = $("#uploadUserBooksData").validate({
        rules: {
            userId: {
                required: true,
                digits: true,
                minlength: 1,
                maxlength: 5
            }
        }
    });
    return !!validator.form();
}

// Util Function to convert form data to Json format
function getFormData($form) {
    let disabled = $form.find(':input:disabled').removeAttr('disabled');
    let unindexed_array = $form.serializeArray();
    let indexed_array = {};

    $.map(unindexed_array, function (n) {
        indexed_array[n['name']] = n['value'];
    });

    disabled.attr('disabled', 'disabled');
    return indexed_array;
}
