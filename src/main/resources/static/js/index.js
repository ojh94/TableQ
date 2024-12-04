console.log("index.js 시작");


$(document).ready(function () {
    console.log("DOM fully loaded.");

    const userIdInput = document.getElementById("userId");
    const userId = userIdInput ? userIdInput.value : null;

    if (!userId) {
        console.error("User ID 값이 비어 있습니다.");
        return;
    }

    // 즐겨찾기 버튼 이벤트 처리
    $("body").on("click", ".favorite-btn", function () {
        const button = $(this);
        const bookmarkId = button.data("bookmark-id"); // data-bookmark-id 값 가져오기
        const isFavorite = button.data("favorite");
        const restaurantId = button.data("id");

    console.log("restaurantId:", restaurantId);  // 로그로 restaurantId 값 확인
    console.log("bookmarkId:", bookmarkId);  // 로그로 bookmarkId 값 확인
    console.log("isFavorite:", isFavorite);  // isFavorite 값 확인

    if (!restaurantId || !bookmarkId) {
        console.error("Restaurant ID 또는 Bookmark ID가 정의되지 않았습니다.");
        return;
    }

    if (isFavorite) {
        removeFromFavorites(button);
    } else {
        addToFavorites(restaurantId, userId);
    }

    toggleFavoriteButton(button, isFavorite);
    });

    console.log("request URL:", `http://localhost/api/reservation/user/${userId}?page=0&size=10&sort=string`);
    console.log("index.js DOMContentLoaded 완료");
});
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




