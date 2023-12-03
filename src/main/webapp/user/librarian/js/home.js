function showDescription(orderId) {
    const hidden = document.getElementById("additionalInfo"+orderId).hidden;
    if (!hidden) {
        document.getElementById("additionalInfo"+orderId).hidden = true;
        document.getElementById("showLessMoreButton"+orderId).textContent = "Show more";
    } else {

        document.getElementById("additionalInfo"+orderId).hidden = false;
        document.getElementById("showLessMoreButton"+orderId).textContent = "Show less";
    }
}

function showOrders(userId) {
    const hidden = document.getElementById("userOrders"+userId).hidden;
    if (!hidden) {
        document.getElementById("userOrders"+userId).hidden = true;
        document.getElementById("showLessMoreButtonAbon"+userId).textContent = "Show more";
    } else {

        document.getElementById("userOrders"+userId).hidden = false;
        document.getElementById("showLessMoreButtonAbon"+userId).textContent = "Show less";
    }
}