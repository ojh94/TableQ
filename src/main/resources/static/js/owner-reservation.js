$(document).ready(function() {
    requestOwnerNavApi();
    requestReservationRestaurantApi();

    // 이전 페이지로 이동
    document.querySelector('.cancel').addEventListener('click', function() {
        window.history.back();
    });
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

// 레스토랑 별 예약 목록 조회
function requestReservationRestaurantApi() {

    const restaurantId = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/reservation/restaurant/${restaurantId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        data: {
               "page" : 0,
               "size" : 10
              },
        success: function(response) {
            // 요청 성공 시 동작
            const nullReservation = response.data.filter(item => item.isEntered == null);

            if (nullReservation.length === 0) {
                return;
            }

            $('#reservation-empty').remove();

            nullReservation.forEach((reservation) => {
                let reservationHtml =
                    `
                    <div class="card mb-2">
                        <div class="card-body">
                            <p>대기번호 : ${reservation.reservationNumber}번</p>
                            <p>인원 : ${reservation.people}명</p>
                            <p>날짜 : ${formatDate(reservation.createdAt)}</p>
                            <button class="reservation-link" data-reservation-id="${reservation.id}">입장완료</button>
                        </div>
                    </div>
                    `;

                // expected 요소(내부) 시작 부분에 추가
                $('#reservation-inventory').prepend(reservationHtml);
            });

            $('#reservation-page').show();

            /*$(document).on('click', '.reservation-link', function() {
                const reservationId = $(this).data('reservation-id');
                if (reservationId) {

                }
            });*/

        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('점포 예약 목록 set 실패:', error);
        alert('점포 예약 목록 set 중 오류가 발생했습니다.');
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