$(document).ready(function () {
    $("#locales").value = window.navigator.language;
    $("#locales").change(function () {
        let selectedOption = $("#locales").val();
        if (selectedOption !== '') {
            window.location.replace('?lang=' + selectedOption);
        }
    });
});