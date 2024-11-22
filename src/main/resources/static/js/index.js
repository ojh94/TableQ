console.log("index.js 시작");
$(document).ready(function () {
    console.log("DOM fully loaded.");
    const userIdInput = document.getElementById("userId"); // HTMLInputElement 가져오기
    if (!userIdInput || !userIdInput.value) {
        console.error("User ID 요소를 찾을 수 없습니다.");
        return;
    }
    const userId = userIdInput.value; // value 속성을 가져옵니다.
    // 예약 정보 가져오기
    const reservationData = requestReservationData(userId);
    if (!reservationData || reservationData.length === 0) {
        console.error("No reservations found.");
        restaurantGrid2.html('<p>예약된 레스토랑이 없습니다.</p>');
        return;
    }
    const restaurantIds = [];

    reservationData.data.forEach((reservation)=> restaurantIds.push(reservation.restaurantId));

    if (!userId) {
        console.error("User ID 값이 비어 있습니다.");
        return;
    }

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

//    const reservationData = requestReservationData(userId);
//    if (!reservationData || reservationData.length === 0) {
//        restaurantGrid2.html('<p>예약된 레스토랑이 없습니다.</p>');
//        return;
//    }

    // const uniqueRestaurantIdList = [...new Set(getUniqueRestaurantIds(reservationData))];

    // 중복 제거 후 레스토랑 카드 추가
    // appendRestaurantCards(uniqueRestaurantIds, restaurantGrid2);

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

// 예약 정보 가져오기
    try {
        const restaurantIds = requestReservationData(userId);
        if (restaurantIds.length > 0) {
            restaurantGrid.empty();
            restaurantGrid2.empty(); // 기존 내용 초기화
            for (const restaurantId of uniqueRestaurantIds) {
                const restaurant = requestRestaurantById(restaurantId);
                if (restaurant) {
                    const { rating, reviewsCount } = requestReviewData(restaurant.id);
                    restaurantGrid.append(createRestaurantCard(restaurant, rating, reviewsCount));
                    restaurantGrid2.append(createRestaurantCard(restaurant, rating, reviewsCount));

                }
            }
        } else {
            console.log("No reservations found.");
            restaurantGrid2.append('<p>예약된 레스토랑이 없습니다.</p>');
        }
    } catch (error) {
        console.error("Error processing reservation data:", error);
    }




    console.log("index.js DOMContentLoaded 완료");
});
console.log("index.js 끝");



// 예약 정보 가져오기 (동기식 요청)
function requestReservationData(userId) {
    let reservationData = null; // 데이터를 담을 변수
    const url = `/api/reservation/user/${encodeURIComponent(userId)}?page=0&size=10&sort=string`;
    $.ajax({
        url: url,
        type: 'GET',
        async: false,  // 동기식 요청
        success: function (data) {
            console.log("Reservation data requested successfully:", data);
            reservationData = data; // 데이터를 변수에 저장
        },
        error: function (xhr) {
            console.error("Error requesting reservation data:", xhr.responseText);
        },
    });
    return reservationData; // 외부로 반환
}



// 레스토랑 카드 생성 함수
function createRestaurantCard(restaurant, starRating, reviewsCount) {
    const card = document.createElement('div');
    card.className = 'card';
    card.innerHTML = `
        <div class="card-header">
            <button id="favorite-btn-${restaurant.id}" class="favorite-btn" data-id="${restaurant.id}" data-favorite="true">
                <i class="fa-regular fa-heart"></i>
            </button>
            <h4 class="card-title">${restaurant.name}</h4>
        </div>
        <div class="card-content">
            <img src="/img/test-img/텐동.jpg" alt="${restaurant.name}" class="card-image">
            <div class="rating">
                <i data-lucide="star" class="rating-star"></i>
                <span class="rating-value">${starRating.toFixed(1)}</span>
                <span class="rating-count">(${reviewsCount} 리뷰)</span>
            </div>
            <div class="location">
                <i data-lucide="map-pin" class="location-icon"></i>
                <span>${restaurant.address}</span>
            </div>
        </div>
        <div class="card-footer">
            <button class="book-btn" onclick="bookRestaurant(${restaurant.id})">
                <i data-lucide="calendar"></i> 예약하기
            </button>
        </div>
    `;
    return card;
}

   // 검색 및 정렬 기능 처리
    const searchForm = document.getElementById("searchForm");
    const searchInput = document.getElementById("searchInput");
    searchForm.addEventListener("submit", (e) => {
        e.preventDefault();
        console.log("Searching for:", searchInput.value);
        // 여기에 검색 로직 추가
    });

    const sortSelect = document.getElementById("sortSelect");
    sortSelect.addEventListener("change", () => {
        console.log("Sorting by:", sortSelect.value);
        // 여기에 정렬 로직 추가
    });

// 레스토랑 정보 가져오기 (동기식 요청)
function requestRestaurantById(restaurantId) {
    let restaurant = null;
    $.ajax({
        url: `/api/restaurant/${restaurantId}`,
        type: 'GET',
        async: false,  // 동기식 요청
        success: function (data) {
            console.log(`requested restaurant data for ID: ${restaurantId}`, data);
            restaurant = data.data;
        },
        error: function (xhr, status, error) {
            console.error(`Error requesting restaurant data for ID: ${restaurantId}`, error);
        }
    });
    return restaurant;
}

// 레스토랑 리뷰 데이터 가져오기 (동기식 요청)
function requestReviewData(restaurantId) {
    let reviewData = { rating: 0, reviewsCount: 0 };
    $.ajax({
        url: `/api/review/restaurant/${restaurantId}`,
        type: 'GET',
        async: false,  // 동기식 요청
        success: function (response) {
            console.log(`responsed review data for restaurant ID: ${restaurantId}`, response);
            reviewData = {
                rating: 0 || response.data.reduce(function(acc, review) {
                    return acc + review.starRating;
                }, 0) / response.data.length,
                reviewsCount: 0 || response.data.length,
            };
        },
        error: function (xhr, status, error) {
            console.error(`Error requesting review data for restaurant ID: ${restaurantId}`, error);
        }
    });
    return reviewData;
}