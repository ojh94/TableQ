$(document).ready(function() {
    // review 탭으로 이동
    document.getElementById("goToReview").addEventListener("click", function(event) {
        const reviewTab = new bootstrap.Tab(document.getElementById("review-tab"));
        reviewTab.show();
    });
});