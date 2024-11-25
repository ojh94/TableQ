document.getElementById('kakaoLogin').addEventListener('click', function() {
    window.location.href = '/oauth2/authorization/kakao'; // Spring Security 설정에 따라 변경 가능
});

document.getElementById('googleLogin').addEventListener('click', function() {
    window.location.href = '/oauth2/authorization/google'; // Spring Security 설정에 따라 변경 가능
});


$(document).ready(function() {
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
});

document.addEventListener("DOMContentLoaded", function() {
    const userId = 3; // 현재 로그인된 사용자의 ID (동적으로 처리 필요)
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


