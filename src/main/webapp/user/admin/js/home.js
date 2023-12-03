function showDescription(bookId) {
    const hidden = document.getElementById("additionalInfo"+bookId).hidden;
    if (!hidden) {
        document.getElementById("additionalInfo"+bookId).hidden = true;
        document.getElementById("showLessMoreButton"+bookId).textContent = "Show more";
    } else {

        document.getElementById("additionalInfo"+bookId).hidden = false;
        document.getElementById("showLessMoreButton"+bookId).textContent = "Show less";
    }
}
