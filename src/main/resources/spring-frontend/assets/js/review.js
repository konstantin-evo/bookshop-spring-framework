$(document).ready(function () {

    // function to create new book review by user
    $('#userReviewFormSubmit').click(function (e) {
        e.preventDefault();
        sendCreateReviewRequest()
    });

    // function to upload Reviews data to moderate by Admin role further
    $('#uploadUserReviewDataSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateUserId()) {
            sendGetReviewsRequest()
        }
    });

    // function to delete Review by ID
    // $('.review_hide').click(function (e) {
    $(document).on('click', '.review_hide', function (e) {
        e.preventDefault();
        sendDeleteReviewRequest($(this))
    });

});

function sendCreateReviewRequest() {
    let text = document.querySelector('#reviewText');
    let slug = text.getAttribute("data-bookid");
    let bookReview = JSON.stringify({"slug": slug, "text": text.value});

    $.post({
        url: '/api/reviews',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: bookReview
    })
}

function sendGetReviewsRequest() {
    let userId = $('#userId').val();
    let parentElement = $('#uploadUserReviewData').parent();
    $.ajax({
        url: '/api/reviews/?userId=' + userId,
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

            createReviewsBlock(parentElement, response)

        },
        // in case of technical and unexpected error
        // we have 4**, 5** status and other error handling
        error: function (errorResponse) {
            let error = errorResponse.responseJSON;
            createErrorElement(parentElement, error.message)
        }
    })
}

function sendDeleteReviewRequest(deleteReviewButton) {
    let reviewId = deleteReviewButton.attr("data-reviewId");
    let parentElement = deleteReviewButton.closest('.AdminReview-btn');
    $.ajax({
        url: '/api/reviews/' + reviewId,
        type: 'DELETE',
        success: function (validatedResponseDto) {

            // in case of business error we have 200 HTTP Status
            // and info about error in ValidatedResponseDto object
            if (!validatedResponseDto.validated) {
                $.each(response.errorMessages, function (key, value) {
                    createErrorElement(parentElement, value)
                });
            }

            processValidatedResponseDto(validatedResponseDto, parentElement, "Review successfully deleted")

        },
        // in case of technical and unexpected error
        error: function (errorResponse) {
            let error = errorResponse.responseJSON;
            createErrorElement(parentElement, error.message)
        }
    })
}

function validateUserId() {
    let validator = $("#uploadUserReviewData").validate({
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

function createReviewsBlock(parent, response) {
    let reviewsBlock = $('<div class="Comments"></div>').insertAfter(parent);
    reviewsBlock.insertAfter(parent)

    response.forEach(reviewDto => {
        $('<div class="Comment">' +
            '<div class="Comment-column Comment-column_pict">' +
            '   <div class="Comment-avatar"></div>' +
            '</div>' +
            '<div class="Comment-column">' +
            '   <header class="Comment-header">' +
            '   <div>' +
            '       <strong class="Comment-title">' +
            '           <span>Book: ' + reviewDto.bookTitle + ' (' + reviewDto.bookSlug + ')<br>User: ' + reviewDto.userName + '<br>Review ID: ' + reviewDto.id + '</span>' +
            '       </strong>' +
            '       <span class="Comment-date">' + reviewDto.pubDate + '</span>' +
            '   </div>' +
            '   </header>' +
            '   <div class="Comment-content">' +
            '       <p>' + reviewDto.text + '</p>' +
            '   </div>' +
            '</div>' +
            '</div>' +
            '<div class="AdminReview-btn">' +
            '   <div class="AdminReview-btn-element">' +
            '       <button class="btn btn_primary btn_outline review_hide" type="button" data-reviewId="' + reviewDto.id + '">Hide Review</button>' +
            '   </div>' +
            '   <div class="AdminReview-btn-element">' +
            '       <button class="btn btn_primary btn_outline block_user" type="button" data-userName="' + reviewDto.userName + '">Block User</button>' +
            '   </div>' +
            '</div>'
        ).appendTo(reviewsBlock);

    });
}

function processValidatedResponseDto(validatedResponseDto, parent, message) {
    if (validatedResponseDto.validated) {
        $('<div class="Review-success">' + message + '</div>')
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

function clearPreviousResponseInfo() {
    $('.Comments').empty();
    $('.error').empty();
}
