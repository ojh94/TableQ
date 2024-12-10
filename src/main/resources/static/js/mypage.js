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
            <button class="book-btn" onclick="window.location.href='/restaurant/reservation/detail/${reservation.id}'">
                <i></i> 예약확인
            </button>
          </div>
      `;
      return gridItem;
    }
});





