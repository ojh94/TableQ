console.log("index.js 시작");

$(document).ready(function () {

    console.log("DOM fully loaded.");

    const userIdInput = document.getElementById("userId");
    const userId = userIdInput ? userIdInput.value : null;

    if (!userId) {
        console.error("User ID 값이 비어 있습니다.");
        return;
    }

    $("body").on("click", ".favorite-btn", function () {
        const button = this;
        console.log("restaurantId:", button.dataset.id);  // 로그로 restaurantId 값 확인
        console.log("bookmarkId:", button.dataset.bookmarkId);  // 로그로 bookmarkId 값 확인
        console.log("isFavorite:", button.dataset.favorite === 'true');  // isFavorite 값 확인

        if (!button.dataset.id || !button.dataset.bookmarkId) {
            console.error("Restaurant ID 또는 Bookmark ID가 정의되지 않았습니다.");
            return;
        }

        toggleFavoriteStatus(button);
    });

    console.log("request URL:", `http://localhost/api/reservation/user/${userId}?page=0&size=10&sort=string`);
    console.log("index.js DOMContentLoaded 완료");
});

/*$(document).ready(function () {
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

});*/

// 초기화 시 데이터를 로드
// DOMContentLoaded 이벤트에서 호출
/*document.addEventListener('DOMContentLoaded', () => {
    const userIdInput = document.getElementById("userId");
    const userId = userIdInput ? userIdInput.value : null;


    // userId가 null이 아니면 즐겨찾기 데이터 불러오기
    if (userId) {
        loadUserBookmarks(userId);  // 유저의 즐겨찾기 데이터 불러오기
    } else {
        console.error("User ID is not available");
    }

    const url = `/api/reservation/user/${userId}?page=0&size=10&sort=createdAt,desc`;



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
//       const userId = userIdInput.value; // value 속성을 가져옵니다.
//      if (!userId) {
//          console.error("User ID 값이 비어 있습니다.");
//          return;
//      }

    const reservationData = requestReservationData(userId);
    if (!reservationData || reservationData.length === 0) {
//        console.error("No reservations found.");
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

});*/

console.log("index.js 끝");


//// 레스토랑 카드 생성 함수
//function createRestaurantCard(restaurant, starRating, reviewsCount) {
//    const card = document.createElement('div');
//    card.className = 'card';
//    card.innerHTML = `
//        <div class="card-header">
//            <button id="favorite-btn-${restaurant.id}" class="favorite-btn" data-id="${restaurant.id}" data-favorite="true">
//                <i class="fa-regular fa-heart"></i>
//            </button>
//            <h4 class="card-title">${restaurant.name}</h4>
//        </div>
//        <div class="card-content">
//            <img src="/img/test-img/텐동.jpg" alt="${restaurant.name}" class="card-image">
//            <div class="rating">
//                <i data-lucide="star" class="rating-star"></i>
//                <span class="rating-value">${starRating.toFixed(1)}</span>
//                <span class="rating-count">(${reviewsCount} 리뷰)</span>
//            </div>
//            <div class="location">
//                <i data-lucide="map-pin" class="location-icon"></i>
//                <span>${restaurant.address}</span>
//            </div>
//        </div>
//        <div class="card-footer">
//            <button class="book-btn" onclick="bookRestaurant(${restaurant.id})">
//                <i data-lucide="calendar"></i> 예약하기
//            </button>
//        </div>
//    `;
//    return card;
//}

//   // 검색 및 정렬 기능 처리
//    const searchForm = document.getElementById("searchForm");
//    const searchInput = document.getElementById("searchInput");
//    searchForm.addEventListener("submit", (e) => {
//        e.preventDefault();
//        console.log("Searching for:", searchInput.value);
//        // 여기에 검색 로직 추가
//    });

//    const sortSelect = document.getElementById("sortSelect");
//    sortSelect.addEventListener("change", () => {
//        console.log("Sorting by:", sortSelect.value);
//        // 여기에 정렬 로직 추가
//    });




