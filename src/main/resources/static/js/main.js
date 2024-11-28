console.log("main.js 시작");

// 레스토랑 카드 생성 함수
function createRestaurantCard(restaurant, rating = 0, reviewsCount =0) {
    const card = document.createElement('div');
    card.className = 'card';
    console.log('Received rating:', restaurant.rating);  // rating 값 확인
    console.log('Received reviewsCount:', restaurant.reviewsCount);  // reviewsCount 값 확인



    // rating 값이 유효한지 확인하고, 없으면 0으로 설정
    const validRating = (rating && !isNaN(rating)) ? rating : 0; // rating이 없거나 NaN이면 0으로 설정
    console.log('Valid rating:', validRating);  // validRating 값 확인

    card.innerHTML =`
        <div class="card-header">
            <button id="favorite-btn-${restaurant.id}" class="favorite-btn" data-id="${restaurant.id}" data-favorite="false"  >
                <i class="fa-regular fa-heart"></i>
            </button>
            <h4 class="card-title">${restaurant.name}</h4>
        </div>
        <div class="card-content">
            <img src="/img/test-img/텐동.jpg" alt="${restaurant.name}" class="card-image">
            <div class="rating">
                <i data-lucide="star" class="rating-star"></i>
                <span class="rating-value">${validRating.toFixed(1)}</span>
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

// 그리드를 나누기 위한 함수
function renderGrids() {
    const userIdInput = document.getElementById("userId");
    const userId = userIdInput ? userIdInput.value : null;

    if (!userId) {
        console.error("User ID 값이 비어 있습니다.");
        return;
    }

    const reservationData = requestReservationData(userId);

    // 예약이 없는 경우
    const gridNoReservation = $('#gridNoReservation');
    const gridReserved = $('#gridReserved');

    if (!reservationData || reservationData.length === 0) {
        gridNoReservation.html('<p>예약된 레스토랑이 없습니다.</p>');
        return;
    }

    const enteredRestaurants = [];
    const reservedRestaurants = [];

    reservationData.forEach(reservation => {
        const restaurant = requestRestaurantById(reservation.restaurant.id);
        if (restaurant) {
            const reviewData = requestReviewData(restaurant.id);
            const card = createRestaurantCard(restaurant, reviewData.rating, reviewData.reviewsCount);

            // 예약 상태에 따라 그리드에 추가
            if (reservation.isEntered) {
                enteredRestaurants.push(card);
            } else {
                reservedRestaurants.push(card);
            }
        }
    });

     // 예약 상태에 따라 카드 배치
    gridNoReservation.empty();
    enteredRestaurants.forEach(card => gridNoReservation.append(card));

    gridReserved.empty();
    reservedRestaurants.forEach(card => gridReserved.append(card));
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
        success: function (response) {
            console.log("Reservation data requested successfully:", response.data);
            reservationData = response.data; // 데이터를 변수에 저장
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
                }, 0) / response.data.length || 0,
                reviewsCount: 0 || response.data.length,
            };
        },
        error: function (xhr, status, error) {
            console.error(`Error requesting review data for restaurant ID: ${restaurantId}`, error);
        }
    });
    return reviewData;
}

// 레스토랑 카드 생성 및 추가 함수
function addRestaurantsToGrid(restaurantGrid, restaurants) {
    if (restaurants && restaurants.length > 0) {
        restaurants.forEach((restaurant) => {
            const { rating, reviewsCount } = requestReviewData(restaurant.id); // 리뷰 데이터 가져오기
            restaurantGrid.append(createRestaurantCard(restaurant, rating, reviewsCount)); // 카드 추가
        });
    } else {
        displayNoDataMessage(restaurantGrid, "표시할 레스토랑이 없습니다."); // 데이터 없을 경우 메시지
    }
}

// 기본 메시지 출력 함수
function displayNoDataMessage(grid, message) {
    grid.html(`<p>${message}</p>`);
}

// 기본 그리드 생성 함수
function displayDefaultRestaurants() {
    const defaultRestaurants = requestRecommendedRestaurants(); // 추천 레스토랑 요청
    const restaurantGrid = $('#restaurantGrid'); // 기본 그리드 선택
    addRestaurantsToGrid(restaurantGrid, defaultRestaurants.data);
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
        const restaurant = requestRestaurantById(reservation.restaurantId);
        if (restaurant) {
            const reviewData = requestReviewData(restaurant.id);
            console.log("Review data:", reviewData);
            // Render restaurant data on page (for example, append to a grid)
        }
    });
}

// 예약 데이터 기반 그리드 생성 함수
function displayReservedRestaurants(userId) {
    const reservationData = requestReservationData(userId);
    const restaurantGrid2 = $('#restaurantGrid2'); // 예약 기반 그리드 선택

    if (reservationData && reservationData.data.length > 0) {
        const reservedRestaurantIds = [...new Set(reservationData.data.map((reservation) => reservation.restaurantId))];
        const reservedRestaurants = reservedRestaurantIds.map((id) => requestRestaurantById(id)); // 레스토랑 데이터 요청
        addRestaurantsToGrid(restaurantGrid2, reservedRestaurants);
    } else {
        displayNoDataMessage(restaurantGrid2, "예약된 레스토랑이 없습니다."); // 예약 데이터 없을 경우 메시지
    }
}

// 초기화 시 데이터를 로드
// DOMContentLoaded 이벤트에서 호출
document.addEventListener('DOMContentLoaded', () => {
    const userIdInput = document.getElementById("userId");
    const userId = userIdInput ? userIdInput.value : null;
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
        console.error("No reservations found.");
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

// 기본 페이지와 페이지 크기 설정
let currentPage = 0;
const pageSize = 10;

// 레스토랑 데이터를 가져오는 함수
async function fetchRestaurants(sortType = 'id', page = currentPage) {
    try {
        // 레스토랑 정보 가져오기
        const restaurantResponse = await $.ajax({
            url: `/api/restaurant?page=${page}&size=${pageSize}&sort=${sortType},asc`,
            method: 'GET'
        });

        if (restaurantResponse && restaurantResponse.data) {
            console.log('레스토랑 데이터:', restaurantResponse.data);

            // 각 레스토랑의 리뷰 데이터 가져오기
            const restaurants = restaurantResponse.data;
           const reviewPromises = restaurantResponse.data.map(async (restaurant) => {
               try {
                   // 각 레스토랑에 해당하는 리뷰를 가져오기
                   const reviewResponse = await $.ajax({
                       url: `/api/review/restaurant/${restaurant.id}`,
                       method: 'GET',
                   });

                   const reviews = reviewResponse.data || [];
                   const rating = reviews.length > 0
                       ? reviews.reduce((sum, r) => sum + r.starRating, 0) / reviews.length
                       : 0;

                   return {
                       ...restaurant,
                       rating: Math.round(rating * 10) / 10, // 소수점 1자리 반올림
                       reviewsCount: reviews.length,
                   };
               } catch (error) {
                   console.error(`Failed to fetch reviews for restaurant ID: ${restaurant.id}`, error);
                   return {
                       ...restaurant,
                       rating: 0, // 기본값 설정
                       reviewsCount: 0,
                   };
               }
           });

           const mergedRestaurants = await Promise.all(reviewPromises);
           console.log('결합된 레스토랑 데이터:', mergedRestaurants);

            // 렌더링 및 페이지네이션 업데이트
            renderRestaurantCards(mergedRestaurants);
            updatePagination(restaurantResponse.pagination.totalPages);
        } else {
            console.error('Invalid response format:', restaurantResponse);
        }
    } catch (error) {
        console.error('Error fetching restaurant data:', error);
    }
}



// 레스토랑 카드를 렌더링하는 함수
function renderRestaurantCards(restaurants) {
    const $restaurantGrid = $('#restaurantGrid3');
    $restaurantGrid.empty(); // 기존 카드 초기화

    restaurants.forEach(restaurant => {
        console.log('Restaurant to be rendered:', restaurant);  // 데이터 확인
        const $card = createRestaurantCard(restaurant, restaurant.rating, restaurant.reviewsCount);
        $restaurantGrid.append($card);
    });
}


// 페이지네이션 업데이트 함수
function updatePagination(totalPages) {
    $('#prevPage').prop('disabled', currentPage === 0);
    $('#nextPage').prop('disabled', currentPage === totalPages - 1);
    $('#currentPage').text(currentPage + 1); // 현재 페이지 표시
}

// 페이지 변경 처리 함수
 function handlePagination(event) {
    if (event.target.id === 'nextPage' && currentPage < totalPages - 1) {
        currentPage++;
    } else if (event.target.id === 'prevPage' && currentPage > 0) {
        currentPage--;
    }

    // 페이지 변경 시 데이터를 새로 불러옴
    fetchRestaurants($('#sortId').val(), currentPage);
}

// 정렬 기준 변경 처리 함수
function handleSortChange() {
    const sortType = $('#sortId').val(); // 인기순, 추천순 선택
    fetchRestaurants(sortType, currentPage); // 선택된 기준에 따라 레스토랑 데이터 가져오기
}



$(document).ready(function () {
    // 초기 레스토랑 데이터 불러오기 (ID 순)
    fetchRestaurants('id', 0);

    // 페이지네이션 버튼 이벤트 리스너
    $('#prevPage').on('click', handlePagination);
    $('#nextPage').on('click', handlePagination);

    // 정렬 기준 변경 시 이벤트 리스너
    $('#sortId').on('change', handleSortChange);
});



console.log("main.js 끝");