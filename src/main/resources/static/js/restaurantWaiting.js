$(document).ready(function() {
    let restaurantId = (document.getElementById("restaurant-id")) ? document.getElementById("restaurant-id").value : '';

    if (window.location.pathname
    === '/restaurant/waiting/' + restaurantId) {

        requestWaitingApi();
        requestWaitingNumApi();

        // 성인 및 아동의 버튼 클릭 이벤트
        document.getElementById('adult-plus').addEventListener('click', function() {
            updateCount('adult', 1);
        });

        document.getElementById('adult-minus').addEventListener('click', function() {
            updateCount('adult', -1);
        });

        document.getElementById('child-plus').addEventListener('click', function() {
            updateCount('child', 1);
        });

        document.getElementById('child-minus').addEventListener('click', function() {
            updateCount('child', -1);
        });

        // 신청하기 버튼 클릭 시
        document.getElementById('apply').addEventListener('click', function(event) {
            event.preventDefault();

            const restaurantId = parseInt(document.getElementById("restaurant-id").value);
            const userId = parseInt(document.getElementById("user-id").value);
            const totalCount = document.getElementById('total-count').value;

            if(totalCount == null || totalCount === 0) {
                alert("인원을 입력해주세요.");
                return;
            }

            const formData = {
                "data": {
                    "people" : totalCount,
                    "restaurantId" : restaurantId,
                    "userId" : userId
                }
            };

            // AJAX 요청 보내기
            $.ajax({
                url: `/api/reservation`,
                type: 'POST', // 필요한 HTTP 메서드로 변경 (PUT 또는 PATCH 등도 가능)
                contentType: 'application/json', // JSON 형식으로 데이터 전송
                data: JSON.stringify(formData), // 데이터를 JSON 문자열로 변환
                success: function(response) {
                    // 요청 성공 시 동작
                    alert('원격줄서기 신청이 완료되었습니다.');
                    location.href = '/restaurant/waiting/detail/' + userId;
                },
                error: function(xhr, status, error) {
                    // 요청 실패 시 동작
                    console.error('생성 실패:', error);
                    alert('생성 중 오류가 발생했습니다.');
                }
            });
        });
    }

    if (window.location.pathname
    === '/restaurant/waiting/detail/' + document.getElementById("reservation-id").value) {
        requestReservationDetailApi();
    }

    // 이전 페이지로 이동
    document.querySelector('.cancel').addEventListener('click', function () {
        window.history.back();
    });
});

// 성인, 아동 카운트 업데이트 함수
function updateCount(type, change) {
    const countElement = document.getElementById(type + '-count');
    let currentCount = parseInt(countElement.textContent);

    // 성인과 아동 모두 0보다 적을 수 없도록
    if (currentCount + change >= 0) {
        currentCount += change;
        countElement.textContent = currentCount;
    }

    updateTotalCount();
}

// 총 인원 업데이트 함수
function updateTotalCount() {
    const adultCount = parseInt(document.getElementById('adult-count').textContent);
    const childCount = parseInt(document.getElementById('child-count').textContent);

    const totalCount = adultCount + childCount;
    document.getElementById('total-count').textContent = totalCount + '명';
    document.getElementById('total-count').value = totalCount;
}

// 가게 이름 조회
function requestWaitingApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            $('#restaurant-name')[0].textContent = response.data.name;

            console.log('가게 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('가게 set 실패:', error);
        alert('가게 set 중 오류가 발생했습니다.');
        }
    });
}

// 가게 대기 순서 조회
function requestWaitingNumApi() {

    const restaurantId = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/reservation/restaurant/queue/${restaurantId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            $('#team')[0].textContent = response.data + '팀';

            console.log('대기 순서 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('대기 순서 set 실패:', error);
        alert('대기 순서 set 중 오류가 발생했습니다.');
        }
    });
}


// 유저의 가게별 이용내역 조회
/*function requestWaitingDetailApi() {

    const userId = document.getElementById("user-id").value;
    const restaurantId = 1;

    $.ajax({
        url: `/api/reservation/user/${userId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            reservations = response.data;

            reservations.forEach((reservation) => {
                if(reservation.restaurantId === restaurantId) {

                    $('#waiting-name')[0].textContent = reservation.restaurantId;
                    $('.waiting-time')[0].textContent = '접수일시: ' + formatDate(reservation.createdAt);
                    $('.waiting-time')[1].textContent = formatDate(reservation.createdAt);
                    $('#waiting-number')[0].textContent = reservation.reservationNumber + '번';
                    $('#waiting-people')[0].textContent = reservation.people + '명';

                    if(reservation.lastModifiedAt === null && reservation.isEntered === null) {
                        $('.waiting-information')[0].classList.add('badge', 'bg-secondary', 'text-decoration-none', 'link-light');

                        $('.waiting-information')[0].textContent = '이용예정';
                        $('.waiting-information')[1].textContent = '이용예정';

                    } else if(reservation.lastModifiedAt !== null && reservation.isEntered === null) {
                        $('.waiting-information')[0].classList.add('badge', 'bg-secondary', 'text-decoration-none', 'link-light');

                        $('.waiting-information')[0].textContent = '이용완료';
                        $('.waiting-information')[1].textContent = '이용완료';

                    } else if(reservation.isEntered === null) {
                        $('.waiting-information')[0].classList.add('badge', 'bg-secondary', 'text-decoration-none', 'link-light');

                        $('.waiting-information')[0].textContent = '취소';
                        $('.waiting-information')[1].textContent = '취소';
                    }

                    // restaurant-detail 클릭 시
                    document.getElementById("restaurant-detail").onclick = function() {
                        location.href = '/restaurant/' + restaurantId;
                    };
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
}*/

// waiting-detail 속 이용날짜 형식 변경
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

// 유저의 가게별 대기 순서 조회
/*function requestWaitingDetailNumApi() {

    const userId = document.getElementById("user-id").value;
    const restaurantId = document.getElementById("restaurant-id").value;
    const reservationId = document.getElementById("reservation-id").value;

    $.ajax({
        url: `/api/reservation/user/${restaurantId}/${reservationId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작

            console.log('대기 순서 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('대기 순서 set 실패:', error);
        alert('대기 순서 set 중 오류가 발생했습니다.');
        }
    });
}*/

// 예약 상세내역 조회
function requestReservationDetailApi() {

    const reservationId = document.getElementById("reservation-id").value;

    $.ajax({
        url: `/api/reservation/${reservationId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            reservation = response.data;

            $('#waiting-name')[0].textContent = reservation.restaurantId;
            $('.waiting-time')[0].textContent = '접수일시: ' + formatDate(reservation.createdAt);
            $('.waiting-time')[1].textContent = formatDate(reservation.createdAt);
            $('#waiting-number')[0].textContent = reservation.reservationNumber + '번';
            $('#waiting-people')[0].textContent = reservation.people + '명';

            if(reservation.lastModifiedAt === null && reservation.isEntered === null) {
                $('.waiting-information')[0].classList.add('badge', 'bg-secondary', 'text-decoration-none', 'link-light');

                $('.waiting-information')[0].textContent = '이용예정';
                $('.waiting-information')[1].textContent = '이용예정';

            } else if(reservation.lastModifiedAt !== null && reservation.isEntered === null) {
                $('.waiting-information')[0].classList.add('badge', 'bg-secondary', 'text-decoration-none', 'link-light');

                $('.waiting-information')[0].textContent = '이용완료';
                $('.waiting-information')[1].textContent = '이용완료';

            } else if(reservation.isEntered === null) {
                $('.waiting-information')[0].classList.add('badge', 'bg-secondary', 'text-decoration-none', 'link-light');

                $('.waiting-information')[0].textContent = '취소';
                $('.waiting-information')[1].textContent = '취소';
            }

            // restaurant-detail 클릭 시
            document.getElementById("restaurant-detail").onclick = function() {
                location.href = '/restaurant/' + restaurantId;
            };

            console.log('이용내역 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('이용내역 set 실패:', error);
        alert('이용내역 set 중 오류가 발생했습니다.');
        }
    });
}