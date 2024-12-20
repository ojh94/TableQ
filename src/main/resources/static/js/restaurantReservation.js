$(document).ready(function() {

    let restaurantId = (document.getElementById("restaurant-id")) ? document.getElementById("restaurant-id").value : '';
    let reservationId = (document.getElementById("reservation-id")) ? document.getElementById("reservation-id").value : '';

    if (window.location.pathname
    === '/restaurant/reservation/apply/' + restaurantId) {
        requestReservationApi();
        requestReservationNumApi();
        peopleUpdate();
        requestReservationCreateApi();
    }

    if (window.location.pathname
    === '/restaurant/reservation/detail/' + reservationId) {
        requestReservationDetailApi();
    }

    // 이전 페이지로 이동
    document.querySelector('.cancel').addEventListener('click', function() {
        window.history.back();
    });
});


/* reservation-apply 함수 */

// apply 예약 할 가게 이름 조회
function requestReservationApi() {

    const restaurantId = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/restaurant/${restaurantId}`,
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

// apply 예약 할 가게 대기 순서 조회
function requestReservationNumApi() {

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

// apply 인원 수 업데이트
function peopleUpdate() {
    function updateCount(type, change) {
        // 성인과 아동 구분
        const countElement = document.getElementById(type + '-count');
        let currentCount = parseInt(countElement.textContent);

        // 성인과 아동 모두 0보다 적을 수 없도록
        if (currentCount + change >= 0) {
            currentCount += change;
            countElement.textContent = currentCount;
        }

        // 총 인원 업데이트
        const adultCount = parseInt(document.getElementById('adult-count').textContent);
        const childCount = parseInt(document.getElementById('child-count').textContent);

        const totalCount = adultCount + childCount;
        document.getElementById('total-count').value = totalCount;
        document.getElementById('total-count').textContent = totalCount + '명';
    }

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
}

// 새로운 예약 생성 reservation API
function requestReservationCreateApi() {
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

        const userConfirmed = confirm("원격줄서기를 신청하시겠습니까?");

        if(!userConfirmed) {
            return;
        }

        const formData = {
            "data": {
                "people" : totalCount,
                "restaurant" : {
                    "id" : restaurantId
                },
                "user" : {
                    "id" : userId
                }
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
                if (response.message === 'Already Reserved User') {
                    alert('이미 예약된 사용자입니다.');
                } else {
                    alert('원격줄서기 신청이 완료되었습니다.');
                    location.href = '/restaurant/reservation/detail/' + response.data.id;
                }
            },
            error: function(xhr, status, error) {
                // 요청 실패 시 동작
                console.error('생성 실패:', error);
                alert('생성 중 오류가 발생했습니다.');
            }
        });
    });
}



/* reservation-detail 함수 */

// detail 예약 이용날짜 형식 변경
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

// detail 예약 상세내역 조회
function requestReservationDetailApi() {

    const reservationId = document.getElementById("reservation-id").value;

    $.ajax({
        url: `/api/reservation/${reservationId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            reservation = response.data;

            $('#restaurant-id').val(reservation.restaurant.id);

            $('#reservation-name')[0].textContent = reservation.restaurant.name;
            $('.reservation-time')[0].textContent = '접수일시: ' + formatDate(reservation.createdAt);
            $('.reservation-time')[1].textContent = formatDate(reservation.createdAt);
            $('#reservation-number')[0].textContent = reservation.reservationNumber + '번';
            $('#reservation-people')[0].textContent = reservation.people + '명';

            if(reservation.isEntered === undefined) {
                $('.reservation-information')[0].classList.add('badge', 'bg-secondary', 'text-decoration-none', 'link-light');
                $('.reservation-information')[0].textContent = '이용예정';
                $('.reservation-information')[1].textContent = '이용예정';

                requestReservationDetailNumApi();
                setInterval(requestReservationDetailNumApi, 10000); // 10초

                // refresh 클릭 시
                document.getElementById("refresh").onclick = function() {
                    requestReservationDetailNumApi();
                };

                requestReservationCancelApi();

            } else if(reservation.isEntered === true) {
                $('.reservation-information')[0].classList.add('badge', 'bg-secondary', 'text-decoration-none', 'link-light');
                $('.reservation-information')[0].textContent = '이용완료';
                $('.reservation-information')[1].textContent = '이용완료';

            } else if(reservation.isEntered === false) {
                $('.reservation-information')[0].classList.add('badge', 'bg-secondary', 'text-decoration-none', 'link-light');
                $('.reservation-information')[0].textContent = '취소';
                $('.reservation-information')[1].textContent = '취소';
            }

            // restaurant-detail 클릭 시
            document.getElementById("restaurant-detail").onclick = function() {
                location.href = '/restaurant/' + reservation.restaurant.id;
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

// detail 예약 대기순서 조회
function requestReservationDetailNumApi() {

    const reservationId = document.getElementById("reservation-id").value;

    $.ajax({
        url: `/api/reservation/user/queue-left/${reservationId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            $('#reservation-order')[0].textContent = response.data + '번째';
            document.getElementById('refresh').style.display = '';
            document.getElementById('reservation-cancel').style.display = '';

            console.log('대기 순서 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('대기 순서 set 실패:', error);
        alert('대기 순서 set 중 오류가 발생했습니다.');
        }
    });
}

// detail 예약 취소
function requestReservationCancelApi() {
    document.getElementById('reservation-cancel').addEventListener('click', function(event) {
        event.preventDefault(); // 폼의 기본 제출 동작을 막음

        const userConfirmed = confirm("정말로 취소하시겠습니까?");

        if(!userConfirmed) {
            return;
        }

        const formData = {
            "data": {
                "isEntered" : false
            }
        };

        const reservationId = document.getElementById("reservation-id").value;

        $.ajax({
            url: `/api/reservation/${reservationId}`,
            type: 'PUT', // 필요한 HTTP 메서드로 변경 (PUT 또는 PATCH 등도 가능)
            contentType: 'application/json', // JSON 형식으로 데이터 전송
            data: JSON.stringify(formData), // 데이터를 JSON 문자열로 변환
            success: function(response) {
                // 요청 성공 시 동작
                alert('예약이 취소되었습니다.');
                location.reload();
            },
            error: function(xhr, status, error) {
                // 요청 실패 시 동작
                console.error('생성 실패:', error);
                alert('생성 중 오류가 발생했습니다.');
            }
        });
    });
}
