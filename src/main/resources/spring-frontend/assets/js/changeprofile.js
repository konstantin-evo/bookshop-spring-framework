$(document).ready(function () {

    // function to validate the Profile form and submit the data on success
    $('#profileSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateProfile()) {
            sendProfileData()
        }
    });

    // function to validate the amount by which the user's balance will be replenished
    $('#topUpSubmit').click(function (e) {
        e.preventDefault();
        clearPreviousResponseInfo()
        if (validateTopUp()) {
            sendTopUpData()
        }
    });

    // function to load next page of transactions
    $('#transactionMore').click(function (e) {
        e.preventDefault();
        let $this = $(this);
        let limit = $this.data('transactionlimit');
        let offset = $this.data('transactionoffset');
        getTransitionPage(offset, limit);
        $this.attr("data-transactionoffset", offset + limit);
    });

    // function for interactive display of information on the "Profile" page
    $(".Tabs-link").click(function () {
        $(".Tabs-link").removeClass("Tabs-link_ACTIVE");
        $(this).addClass("Tabs-link_ACTIVE");
        $(".Tabs-block").hide();

        let id = $(this).attr('id');

        switch (id) {
            case "basic_button":
                $(".Profile").closest('.Tabs-block').show();
                break;
            case "transactions_button":
                $(".Transactions").closest('.Tabs-block').show();
                break;
            default:
                $(".Topup-wrap").closest('.Tabs-block').show();
        }
    });
});

function sendProfileData() {
    let $form = $('#profileForm');
    let data = getFormData($form);

    $.post({
        url: '/profile',
        dataType: 'json',
        contentType: 'application/json; charset=utf-8',
        data: JSON.stringify(data),
        success: function (result) {

            $('#passwordReply').val('');
            $('#password').val('');

            let parentDiv = $('#profileSubmit').parent();

            if (result.validated) {
                $('<div class="Profile-success">Profile data changed successfully</div>')
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

function sendTopUpData() {
    $.post({
        url: '/profile',
        dataType: 'text',
        contentType: 'application/x-www-form-urlencoded',
        data: $('#topUpForm').serialize(),
        success: function () {
            $('#sum').val('');
            $('<div class="Profile-success">Your account has been successfully funded</div>')
                .insertBefore('.Topup-btn').first();
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

function getTransitionPage(offset, limit) {
    const newOffset = offset + 1;
    $.get({
        url: '/transactions?offset=' + newOffset + '&limit=' + limit,
        success: function (result) {
            if (result.count > 0) {
                let table = document.getElementById("transactionsTable");
                result.transactions.forEach(function (transaction) {
                    let row = table.insertRow(-1);
                    let cellDate = row.insertCell();
                    let cellAmount = row.insertCell();
                    let cellDescription = row.insertCell();
                    cellDate.innerHTML = transaction.time;
                    cellAmount.innerHTML = transaction.value;
                    cellDescription.innerHTML = transaction.description;
                });
                if (result.count < limit) {
                    $('#transactionMore').hide();
                }
            }
        },
        error: function (jqXhr, textStatus, errorThrown) {
            console.log(errorThrown);
        }
    });
}

function validateProfile() {
    let validator = $("#profileForm").validate({
        rules: {
            password: "required",
            passwordReply: {
                equalTo: "#password"
            }
        },
        messages: {
            password: "Enter Password",
            passwordReply: "Passwords must match"
        },
    });
    return !!validator.form();
}

function validateTopUp() {
    let validator = $("#topUpForm").validate({
        rules: {
            sum: {
                required: true,
                digits: true,
                min: 0.01,
                max: 99999
            }
        },
        messages: {
            sum: {
                required: "Please enter the amount to top up your account",
                digits: "Invalid characters",
                min: "Minimum deposit amount is 0.01",
                max: "Minimum deposit amount is 99999"
            }
        },
        errorElement: 'div',
        errorPlacement: function (error) {
            error.insertBefore("#topUpSubmit");
            error.wrap("<p>")
        }
    });
    return !!validator.form();
}

function clearPreviousResponseInfo() {
    $('.Profile-success').empty();
    $('.error').empty();
}

// Util Function to convert form data to Json format
function getFormData($form) {
    let unindexed_array = $form.serializeArray();
    let indexed_array = {};

    $.map(unindexed_array, function (n) {
        indexed_array[n['name']] = n['value'];
    });
    return indexed_array;
}