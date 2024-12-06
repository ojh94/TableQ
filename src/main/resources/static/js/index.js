console.log("index.js 시작");

$(document).ready(function () {

    console.log("DOM fully loaded.");

    // 즐겨찾기 버튼 이벤트 처리
    $("body").on("click", ".favorite-btn", function () {
        const button = $(this);
        const restaurantId = button.data("id");
        const isFavorite = button.data("favorite");

        if (isFavorite) {
            removeFromFavorites(restaurantId, userId);
        } else {
            addToFavorites(restaurantId, userId);
        }

        toggleFavoriteButton(button, isFavorite);
    });

    console.log("request URL:", `http://localhost/api/reservation/user/${userId}?page=0&size=10&sort=string`);

     // 초기 레스토랑 데이터 불러오기 (ID 순)
        const initialSortType = $('#sortId').val();
        fetchRestaurants(initialSortType, 0);

        // 페이지네이션 버튼 이벤트 리스너

        // 이전 페이지 버튼
        $('#prevPage').on('click', function () {
            if (currentPage > 0) {
                currentPage--;
                console.log("이전 페이지, 커런트 페이지", currentPage); // currentPage 값 확인
                const sortType = $('#sortId').val(); // 현재 드롭다운 값 읽기
                fetchRestaurants(sortType , currentPage);
            }
        });

        // 다음 페이지 버튼
        $('#nextPage').on('click', function () {
            if (currentPage < totalPages - 1) {
                currentPage++;
                console.log("Next Page, currentPage:", currentPage); // currentPage 값 확인
                const sortType = $('#sortId').val(); // 현재 드롭다운 값 읽기
                fetchRestaurants(sortType , currentPage);
            }
        });

        // 정렬 기준 변경
        $('#sortId').on('change', function () {
            console.log('Dropdown value:', $(this).val());
            const sortType = $(this).val(); // 드롭다운에서 선택된 정렬 기준
            currentPage = 0; // 정렬 변경 시 첫 페이지로 이동
            console.log('Selected sort value:', sortType);
            fetchRestaurants(sortType, currentPage);
        });

     fetch(url)
           .then(response => response.json())
           .then(data => {
             console.log("Reservation data:", data);  // 데이터 확인
             // 여기에 데이터를 처리하는 코드 추가
           })
           .catch(error => {
             console.error("예약 데이터를 가져오지 못했습니다.", error);
           });

          console.log("User ID (value):", userId); // userId 값 확인

             if (userId) {
                 const requestURL = `/api/reservation/user/${userId}?page=0&size=10&sort=createdAt,desc`;
                 console.log("Request URL:", requestURL); // 요청 URL 확인

                 fetch(requestURL)
                     .then((response) => {
                         if (!response.ok) {
                             throw new Error(`HTTP error! status: ${response.status}`);
                         }
                         return response.json();
                     })
                     .then((data) => {
                         console.log("Reservation data:", data);
                     })
                     .catch((error) => {
                         console.error("API 요청 중 오류 발생:", error);
                     });
             } else {
                 console.error("userId를 찾을 수 없습니다.");
             }

         renderGrids();

         if (!userIdInput) {
             console.error("User ID 요소를 찾을 수 없습니다.");
             return;
         }

         const reservationData = requestReservationData(userId);
         if (!reservationData || reservationData.length === 0) {
             restaurantGrid2.html('<p>예약된 레스토랑이 없습니다.</p>');
             return;
         }
         const restaurantIds = [...new Set(reservationData.map((reservation) => reservation.restaurant.id))];

         // 중복된 ID 제거
         const uniqueRestaurantIds = [...new Set(restaurantIds)];
         const restaurantGrid = $('#restaurantGrid');
          const restaurantGrid2 = $('#restaurantGrid2');
          if (restaurantIds.length > 0) {
              restaurantGrid2.empty();
              for (const restaurantId of restaurantIds) {
                  const restaurant = requestRestaurantById(restaurantId);
                  if (restaurant) {
                      const { rating, reviewsCount } = requestReviewData(restaurant.id);
                      restaurantGrid2.append(createRestaurantCard(restaurant, rating, reviewsCount));
                  }
              }
          } else {
              restaurantGrid2.html('<p>예약된 레스토랑이 없습니다.</p>');
          }

    console.log("index.js DOMContentLoaded 완료");
});
