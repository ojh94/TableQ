$(document).ready(function() {
    requestUserHistoryApi();

    // 이전 페이지로 이동
    document.querySelector('.cancel').addEventListener('click', function() {
        window.history.back();
    });
});

function requestUserHistoryApi() {

    const userId = document.getElementById("userId").value;

    $.ajax({
        url: `/api/reservation/user/${userId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        data: {
               "page" : 0,
               "size" : 3
              },
        success: function(response) {
            // 요청 성공 시 동작
            if (response.data.length === 0) {
                return;

            }

            if (response.data.filter(item => item.isEntered == null).length > 0) {

                $('#expected-empty').remove();

                const expected = response.data.filter(item => item.isEntered == null);
                expected.forEach((plans) => {
                    let reservationHtml =
                        `
                        <div class="card mb-2">
                            <div class="card-body">
                                <p>${formatDate(plans.createdAt)}</p>
                                <p>대기중</p>
                                <h6 class="fw-bold mt-3 restaurant-link" data-restaurant-id="${plans.restaurant.id}">
                                    ${plans.restaurant.name} <i class="bi bi-chevron-right"></i>
                                </h6>
                                <p>대기번호 : ${plans.reservationNumber}번</p>
                                <p>인원 : ${plans.people}명</p>
                            </div>
                        </div>
                        `;

                    // expected 요소(내부) 시작 부분에 추가
                    $('#expected').prepend(reservationHtml);
                });

                $('#expected-page').show();

            }

            if (response.data.filter(item => item.isEntered === true).length > 0) {

                $('#complete-empty').remove();

                const complete = response.data.filter(item => item.isEntered === true);
                complete.forEach((plans) => {
                    let reservationHtml =
                        `
                        <div class="card mb-2">
                            <div class="card-body">
                                <p>${formatDate(plans.createdAt)}</p>
                                <p>완료</p>
                                <h6 class="fw-bold mt-3 restaurant-link" data-restaurant-id="${plans.restaurant.id}">
                                    ${plans.restaurant.name} <i class="bi bi-chevron-right"></i>
                                </h6>
                                <p>대기번호 : ${plans.reservationNumber}번</p>
                                <p>인원 : ${plans.people}명</p>
                            </div>
                        </div>
                        `;

                    // complete 요소(내부) 시작 부분에 추가
                    $('#complete').prepend(reservationHtml);
                });

                $('#complete-page').show();

            }

            if (response.data.filter(item => item.isEntered === false).length > 0) {

                $('#cancellation-empty').remove();

                const cancellation = response.data.filter(item => item.isEntered === false);
                cancellation.forEach((plans) => {
                    let reservationHtml =
                        `
                        <div class="card mb-2">
                            <div class="card-body">
                                <p>${formatDate(plans.createdAt)}</p>
                                <p>취소</p>
                                <h6 class="fw-bold mt-3 restaurant-link" data-restaurant-id="${plans.restaurant.id}">
                                    ${plans.restaurant.name} <i class="bi bi-chevron-right"></i>
                                </h6>
                                <p>대기번호 : ${plans.reservationNumber}번</p>
                                <p>인원 : ${plans.people}명</p>
                            </div>
                        </div>
                        `;

                    // cancellation 요소(내부) 시작 부분에 추가
                    $('#cancellation').prepend(reservationHtml);
                });

                $('#cancellation-page').show();
            }

            $(document).on('click', '.restaurant-link', function() {
                const restaurantId = $(this).data('restaurant-id');
                if (restaurantId) {
                    location.href = '/restaurant/' + restaurantId;
                }
            });

            console.log('이용내역 set 완료');
        },
         error: function(xhr, status, error) {
         // 요청 실패 시 동작
         console.error('이용내역 set 실패:', error);
         alert('이용내역 set 중 오류가 발생했습니다.');
         }
    });
}

// 예약 이용날짜 형식 변경
function formatDate(dateString) {
    // 문자열을 Date 객체로 변환
    const date = new Date(dateString);

    // 요일 배열
    const days = ['일', '월', '화', '수', '목', '금', '토'];

    // 연, 월, 일, 시간, 분 추출
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0'); // 월은 0부터 시작하므로 +1
    const day = String(date.getDate()).padStart(2, '0');
    const weekday = days[date.getDay()]; // 요일 (0: 일요일, 6: 토요일)
    const hours = String(date.getHours()).padStart(2, '0');
    const minutes = String(date.getMinutes()).padStart(2, '0');

    // 원하는 형식으로 조합
    return `${year}.${month}.${day} (${weekday}) ${hours}:${minutes}`;
}