console.log("main.js 시작");

// 레스토랑 카드 생성 함수
function createRestaurantCard(restaurant, rating, reviewsCount) {
    const card = document.createElement('div');
    card.className = 'card';
    card.innerHTML = `
        <div class="card-header">
            <button id="favorite-btn-${restaurant.id}" class="favorite-btn" data-id="${restaurant.id}" data-favorite="true"  >
                <i class="fa-regular fa-heart"></i>
            </button>
            <h4 class="card-title">${restaurant.name}</h4>
        </div>
        <div class="card-content">
            <img src="/img/test-img/텐동.jpg" alt="${restaurant.name}" class="card-image">
            <div class="rating">
                <i data-lucide="star" class="rating-star"></i>
                <span class="rating-value">${rating.toFixed(1)}</span>
                <span class="rating-count">(${reviewsCount} 리뷰)</span>
            </div>
            <div class="location">
                <i data-lucide="map-pin" class="location-icon"></i>
                <span>${restaurant.address}</span>
            </div>
        </div>
        <div class="card-footer">
            <button class="book-btn" onclick="window.location.href='/restaurant/${restaurant.id}'">
                <i data-lucide="calendar"></i> 예약하기
            </button>
        </div>
    `;
    return card;
}

// 추천 레스토랑 데이터를 가져오는 API 요청
function requestRecommendedRestaurants(page = 0, size = 6) {
    const url = `http://localhost/api/restaurants?page=${page}&size=${size}`;
    $.ajax({
            url: url,
            type: 'GET',
            async: false,  // 동기식 요청
            success: function(data) {
                renderRestaurants('restaurantGrid', data);
            },
            error: function(xhr, status, error) {
                console.error('Error requesting recommended restaurants:', error);
            }
        });
}

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


// 레스토랑 세부 데이터를 동기적으로 가져오는 함수 (AJAX로 동기화된 방식으로 변경)
function requestRestaurantById(restaurantId) {
    let restaurantData = null;
    $.ajax({
        url: `/api/restaurant/${restaurantId}`,
        type: 'GET',
        async: false,  // 동기식 요청
        success: function(data) {
            console.log(`Restaurant data requested for ID: ${restaurantId}`, data);
            restaurantData = data.data;
        },
        error: function(xhr, status, error) {
            console.error("Failed to request restaurant data", error);
        }
    });
    return restaurantData;
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

// 예약 데이터를 화면에 표시하는 함수 (AJAX로 동기화된 방식으로 변경)
function displayReservedRestaurants() {
    const reservationData = requestReservationData();

    if (reservationData.length === 0) {
        console.log("No reservations found.");
        return;
    }

    reservationData.forEach(reservation => {
        console.log("Displaying reservation:", reservation);
        const restaurant = requestRestaurantById(reservation.restaurantId); // Assuming reservation has a restaurantId
        if (restaurant) {
            const reviewData = requestReviewData(restaurant.id);
            console.log("Review data:", reviewData);
            // Render restaurant data on page (for example, append to a grid)
        }
    });
}

//displayReservedRestaurants();

//// Expose functions to global scope
//window.requestReservationData = requestReservationData;
//window.requestRestaurantById = requestRestaurantById;
//window.requestReviewData = requestReviewData;



// 초기화 시 데이터를 로드
// DOMContentLoaded 이벤트에서 호출
document.addEventListener('DOMContentLoaded', () => {
    const userIdInput = document.getElementById("userId");
      if (!userIdInput) {
          console.error("User ID 요소를 찾을 수 없습니다.");
          return;
      }
      const userId = userIdInput.value; // value 속성을 가져옵니다.
      if (!userId) {
          console.error("User ID 값이 비어 있습니다.");
          return;
      }

      const reservationData = requestReservationData(userId);
      if (!reservationData || reservationData.length === 0) {
          console.error("No reservations found.");
          restaurantGrid2.html('<p>예약된 레스토랑이 없습니다.</p>');
          return;
      }
      const restaurantIds = [...new Set(reservationData.data.map((reservation) => reservation.restaurantId))];

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

//    function displayReservedRestaurants(userId) {
//        const reservationData = requestReservationData(userId);
//        reservationData.forEach(reservation => {
//            const restaurant = requestRestaurantById(reservation.restaurantId);
//            if (restaurant) {
//                const reviewData = requestReviewData(restaurant.id);
//                const card = createRestaurantCard(restaurant, reviewData.rating, reviewData.reviewsCount);
//                document.getElementById('reservedRestaurantGrid').appendChild(card);
//            }
//        });
//    }

//  requestPickedRestaurants();
  console.log("main.js DOMContentLoaded 완료");
});

// 즐겨찾기 버튼 토글여부 확인
function toggleFavoriteButton(button, isFavorite) {
    button[0].setAttribute('data-favorite', !isFavorite);
    button[0].querySelector('i').classList.toggle("fa-solid");
    button[0].querySelector('i').classList.toggle("fa-regular");
}

//즐겨찾기 추가
function addToFavorites(restaurantId, userId) {
    const button = document.querySelector(`[data-id="${restaurantId}"]`);
    const isFavorite = button.getAttribute('data-favorite') === 'true';

    // UI에서 즉시 변경 (버튼 상태 반영)
    button.setAttribute('data-favorite', !isFavorite);
    button.querySelector('i').classList.toggle("fa-solid");
    button.querySelector('i').classList.toggle("fa-regular");

    // 동기식 Ajax 요청
    $.ajax({
        url: '/api/bookmark',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify({ restaurantId, userId }),
        async: false, // 동기식으로 요청
        success: function () {
            console.log("Added to favorites");
        },
        error: function (xhr, status, error) {
            console.error("Error adding to favorites:", error);

            // 실패 시, UI 상태 원복 (에러 발생 시)
            button.setAttribute('data-favorite', isFavorite);
            button.querySelector('i').classList.toggle("fa-solid");
            button.querySelector('i').classList.toggle("fa-regular");
        }
    });
}


// 즐겨찾기 삭제
function removeFromFavorites(restaurantId, userId) {
    const button = document.querySelector(`[data-id="${restaurantId}"]`);
    const isFavorite = button.getAttribute('data-favorite') === 'true';

    // UI에서 즉시 변경 (버튼 상태 반영)
    button.setAttribute('data-favorite', !isFavorite);
    button.querySelector('i').classList.toggle("fa-solid");
    button.querySelector('i').classList.toggle("fa-regular");

    // 동기식 Ajax 요청
    $.ajax({
        url: `/api/bookmark/${restaurantId}`,
        type: 'DELETE',
        async: false, // 동기식으로 요청
        success: function () {
            console.log("Removed from favorites");
        },
        error: function (xhr, status, error) {
            console.error("Error removing from favorites:", error);

            // 실패 시, UI 상태 원복 (에러 발생 시)
            button.setAttribute('data-favorite', isFavorite);
            button.querySelector('i').classList.toggle("fa-solid");
            button.querySelector('i').classList.toggle("fa-regular");
        }
    });
}

console.log("main.js 끝");