$(document).ready(function() {
    // review 탭으로 이동
    document.getElementById("goToReview").addEventListener("click", function(event) {
        const reviewTab = new bootstrap.Tab(document.getElementById("review-tab"));
        reviewTab.show();
    });

    requestRestaurantApi();
});

function requestRestaurantApi() {

    const id = 1; // 추후 수정 필요

    $.ajax({
        url: `/api/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            const rName = $('body > div > div.container.mt-5 > div > div > article:nth-child(1) > header > h1');

            console.log(response);

            rName[0].textContent = response.data.name;

            console.log('가게 네임 셋 완료');

        },
        error: function(xhr, status, error) {
            // 요청 실패 시 동작
            console.error('수정 실패:', error);
            alert('수정 중 오류가 발생했습니다.');
        }
    });
}