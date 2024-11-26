document.getElementById('kakaoLogin').addEventListener('click', function() {
    window.location.href = '/oauth2/authorization/kakao'; // Spring Security 설정에 따라 변경 가능
});

document.getElementById('googleLogin').addEventListener('click', function() {
    window.location.href = '/oauth2/authorization/google'; // Spring Security 설정에 따라 변경 가능
});


$(document).ready(function() {
    const userId = $('#userId').text().trim(); // userId를 가져오는 방법
    const url = `/api/reservation/user/${userId}?page=0&size=10&sort=createdAt,desc`;
    const upcomingReservationsContainer = document.getElementById("upcomingReservationsGrid");
    const noReservationsMessage = document.getElementById("noReservationsMessage");
    const upcomingReservations = reservations.filter((reservation) => reservation.isEntered === null);
    console.log('필터링된 예약 목록:', upcomingReservations);  // 필터링된 결과 확인

    $('#signupForm').on('submit', function(event) {
        event.preventDefault(); // 폼의 기본 제출 동작을 막음

        // ID 값을 가져오기
        const userId = $('#id').text().trim(); // ID를 가져와서 공백 제거

        // 폼 데이터 수집
        const formData = {
            "data": {
                "nickname" : $('#nickname').val(),
                "address" : $('#address').val(),
                "password" : $('#password').val()
            }
            // 필요한 추가 필드가 있다면 여기에 추가
        };

        // AJAX 요청 보내기
        $.ajax({
            url: `/api/user/${userId}`,
            type: 'PUT', // 필요한 HTTP 메서드로 변경 (PUT 또는 PATCH 등도 가능)
            async: false,
            contentType: 'application/json', // JSON 형식으로 데이터 전송
            data: JSON.stringify(formData), // 데이터를 JSON 문자열로 변환
            success: function(response) {
                // 요청 성공 시 동작
                console.log(response);
                alert('정보가 성공적으로 수정되었습니다.');
                // 필요한 경우 페이지를 새로 고침
                location.reload();

            },
            error: function(xhr, status, error) {
                // 요청 실패 시 동작
                console.error('수정 실패:', error);
                alert('수정 중 오류가 발생했습니다.');
            }
        });
    });

    // API 호출로 데이터 가져오기
    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        success: function(data) {
            if (data.resultCode === "OK" && data.data) {
                const reservations = data.data;

                // isEntered가 null인 데이터만 필터링
                const upcomingReservations = reservations.filter((reservation) => reservation.isEntered === null);

                // 데이터가 있으면 그리드 표시, 없으면 메시지 표시
                if (upcomingReservations.length > 0) {
                    noReservationsMessage.style.display = "none"; // 메시지 숨기기
                    upcomingReservationsContainer.style.display = "grid"; // 그리드 표시

                    // 필터링된 데이터로 그리드 생성
                    upcomingReservations.forEach((reservation) => {
                        const gridItem = createGridItem(reservation);
                        upcomingReservationsContainer.appendChild(gridItem);
                    });
                } else {
                    upcomingReservationsContainer.style.display = "none"; // 그리드 숨기기
                    noReservationsMessage.style.display = "block"; // 메시지 표시
                }
            } else {
                console.error("예약 데이터를 가져오지 못했습니다.", data.description);
            }
        },
        error: function(xhr, status, error) {
            console.error("AJAX 요청 실패:", error);
        }
    });


    // 그리드 항목 생성 함수
    function createGridItem(reservation) {
        const gridItem = document.createElement("div");
        gridItem.className = "grid-item";

        // 예약 정보 렌더링
        gridItem.innerHTML =`
            <div class="grid-content">
                <h3>${reservation.restaurant.name}</h3>
                <p>예약인원: ${reservation.people}명</p>
                <p>주소: ${reservation.restaurant.address}</p>
                <p>전화번호: ${reservation.restaurant.contactNumber}</p>
            </div>
        `;
        return gridItem;
    }

    const reservationInfoContainer = document.querySelector('.reservation-info');

    fetch(`/api/reservation/user/${userId}?page=0&size=10&sort=createdAt`)
        .then(response => response.json())
        .then(data => {
            const reservations = data.data;

            // 예약 정보가 있을 경우 이모지 및 텍스트 변경
            const pendingReservations = reservations.filter(reservation => reservation.isEntered === null);

            if (pendingReservations.length > 0) {
                // 예약이 있을 경우
                reservationInfoContainer.innerHTML = `
                    <p>이용 예정 내역이 있어요!</p>
                    <ul>
                        ${pendingReservations.map(reservation => `
                            <li>
                                예약 번호: ${reservation.reservationNumber}<br>
                                예약 날짜: ${new Date(reservation.createdAt).toLocaleString()}<br>
                                인원: ${reservation.people}명<br>
                            </li>
                        `).join('')}
                    </ul>
                `;
            } else {
                // 예약이 없을 경우
                reservationInfoContainer.innerHTML = `
                    <div class="no-reservation">
                        <p class="emoji">😢</p>
                        <p>이용 예정인 내역이 없어요!</p>
                    </div>
                `;
            }
        })
        .catch(error => {
            console.error('예약 정보를 불러오는 중 오류가 발생했습니다.', error);
        });
});





