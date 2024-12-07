$(document).ready(function() {
    requestOwnerNavApi();
});

// 점주 별 레스토랑 정보 조회
function requestOwnerNavApi() {
    /*const userId = $('#userId').val();*/
    const userId = 3;

    $.ajax({
        url: `/api/restaurant/owner/my-restaurants/${userId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            response.data.forEach((restaurant) => {
                let restaurantHtml =
                    `
                    <li><a class="dropdown-item" href="/restaurant/modify/${restaurant.id}">${restaurant.name}</a></li>
                    `;

                // information 요소(내부) 끝에 추가
                $('.dropdown-menu').prepend(restaurantHtml);
            });
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('점주 레스토랑 불러오기 실패:', error);
        alert('점주 레스토랑 불러오기 중 오류가 발생했습니다.');
        }
    });
}