//document.getElementById('kakaoLogin').addEventListener('click', function() {
//    window.location.href = '/oauth2/authorization/kakao'; // Spring Security 설정에 따라 변경 가능
//});
//
//document.getElementById('googleLogin').addEventListener('click', function() {
//    window.location.href = '/oauth2/authorization/google'; // Spring Security 설정에 따라 변경 가능
//});


$(document).ready(function () {
    const userIdInput = document.getElementById("userId");
    const userId = userIdInput ? userIdInput.value : null;
    console.log("userId:", userId);

    const url = `/api/reservation/user/${userId}?page=0&size=10&sort=createdAt,desc`;

    const upcomingReservationsContainer = document.getElementById("upcomingReservationsGrid");
    const noReservationsMessage = document.getElementById("noReservationsMessage");

//    $('#signupForm').on('submit', function(event) {
//        event.preventDefault(); // 폼의 기본 제출 동작을 막음
//
//        // ID 값을 가져오기
//        const userId = $('#id').text().trim(); // ID를 가져와서 공백 제거
//
//        // 폼 데이터 수집
//        const formData = {
//            "data": {
//                "nickname" : $('#nickname').val(),
//                "address" : $('#address').val(),
//                "password" : $('#password').val()
//            }
//            // 필요한 추가 필드가 있다면 여기에 추가
//        };
//    });
//
//    // AJAX 요청 보내기
//    $.ajax({
//        url: `/api/user/${userId}`,
//        type: 'PUT', // 필요한 HTTP 메서드로 변경 (PUT 또는 PATCH 등도 가능)
//        async: false,
//        contentType: 'application/json', // JSON 형식으로 데이터 전송
//        data: JSON.stringify(formData), // 데이터를 JSON 문자열로 변환
//        success: function(response) {
//            // 요청 성공 시 동작
//            console.log(response);
//            alert('정보가 성공적으로 수정되었습니다.');
//            // 필요한 경우 페이지를 새로 고침
//            location.reload();
//
//        },
//        error: function(xhr, status, error) {
//            // 요청 실패 시 동작
//            console.error('수정 실패:', error);
//            alert('수정 중 오류가 발생했습니다.');
//        }
//    });

    // API 호출로 데이터 가져오기
    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        success: function (data) {
            if (data.resultCode === "OK" && data.data) {
                const reservations = data.data;

                // isEntered가 없는 데이터만 필터링
                const filteredReservations = reservations.filter(
                (reservation) => !("isEntered" in reservation)
                );
                console.log('필터링된 데이터:', filteredReservations); // 확인용 로그

                if (filteredReservations.length > 0) {
                    // 예약이 있는 경우
                    noReservationsMessage.style.display = "none"; // "예약 없음" 메시지 숨기기
                    upcomingReservationsContainer.style.display = "grid"; // 예약 그리드 표시

                    // 필터링된 데이터로 그리드 생성
                    filteredReservations.forEach((reservation) => {
                    const gridItem = createGridItem(reservation);
                    upcomingReservationsContainer.appendChild(gridItem);
                    });
                } else {
                    // 예약이 없는 경우
                    displayNoReservations();
                }
            } else {
                console.error("예약 데이터를 가져오지 못했습니다.", data.description);
                displayNoReservations();
            }
        },
        error: function (xhr, status, error) {
             console.error("AJAX 요청 실패:", error);
             displayNoReservations();
        }
    });

    // 예약이 없는 경우 메시지 표시
    function displayNoReservations() {
      upcomingReservationsContainer.style.display = "none"; // 예약 그리드 숨기기
      noReservationsMessage.style.display = "block"; // "예약 없음" 메시지 표시
//      reservationInfoContainer.innerHTML = `
//          <div class="no-reservation">
//              <p class="emoji">😢</p>
//              <p>이용 예정인 내역이 없어요!</p>
//          </div>
//      `;
    }

    // 그리드 항목 생성 함수
    function createGridItem(reservation) {
      const gridItem = document.createElement("div");
      gridItem.className = "grid-item";

      // 예약 정보 렌더링
      gridItem.innerHTML = `
          <div class="grid-content">
            <h3>${reservation.restaurant.name}</h3>
            <p>예약 번호: ${reservation.reservationNumber}</p>
            <p>예약 날짜: ${new Date(reservation.createdAt).toLocaleString()}</p>
            <p>예약 인원: ${reservation.people}명</p>
            <p>주소: ${reservation.restaurant.address}</p>
            <p>전화번호: ${reservation.restaurant.contactNumber}</p>
          </div>
      `;
      return gridItem;
    }
});





