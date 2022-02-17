function handleSubmit(event) {
    event.preventDefault();

    let text = document.querySelector('#reviewText');
    let bookId = text.getAttribute("data-bookid");
    let data = JSON.stringify({ "bookId": bookId, "text": text.value });

    let xhr = new XMLHttpRequest();
    // let url = window.location.origin + "/api/book-review";
    let url = "/api/book-review";
    xhr.open("POST", url, true);
    xhr.setRequestHeader("Content-Type", "application/json");
    xhr.send(data);
    location.reload();
}

const form = document.querySelector('#userReviewForm');
form.addEventListener('submit', handleSubmit);