console.log("main.js 시작");

//function updateFavoriteButtonState(button, isFavorite) {
//    if (isFavorite) {
//        button.dataset.favorite = 'true';
//        button.querySelector('i').classList.remove('fa-regular', 'fa-heart');
//        button.querySelector('i').classList.add('fa-solid', 'fa-heart');
//    } else {
//        button.dataset.favorite = 'false';
//        button.querySelector('i').classList.remove('fa-solid', 'fa-heart');
//        button.querySelector('i').classList.add('fa-regular', 'fa-heart');
//    }
//}

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
            <button id="favorite-btn-${restaurant.id}" class="favorite-btn" data-id="${restaurant.id}" data-favorite="${restaurant.isFavorite}" data-bookmark-id="${restaurant.bookmarkId || 'no-id'}">
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

    // 찜 버튼 상태 업데이트
    const favoriteButton = card.querySelector(`#favorite-btn-${restaurant.id}`);
    updateFavoriteButtonState(favoriteButton, restaurant.id);

//    // 찜 버튼 클릭 시 처리
//    favoriteButton.addEventListener('click', () => {
//        const isFavorite = favoriteButton.dataset.favorite === 'true';
//        toggleFavoriteButton(favoriteButton, isFavorite);
////        updateFavoriteButtonState(favoriteButton, restaurant.id);
//        if (isFavorite) {
//            removeFromFavorites(favoriteButton);
//        } else {
//            addToFavorites(favoriteButton);
//        }
//    });


    return card;
}

//// 즐겨찾기 상태를 토글하는 함수
//function toggleFavorite(button, isFavorite) {
//    // 찜 상태 토글
//        if (isFavorite) {
//            // 찜을 해제
//            button.dataset.favorite = 'false';
//            button.querySelector('i').classList.remove('fa-solid', 'fa-heart');
//            button.querySelector('i').classList.add('fa-regular', 'fa-heart');
//        } else {
//            // 찜을 추가
//            button.dataset.favorite = 'true';
//            button.querySelector('i').classList.remove('fa-regular', 'fa-heart');
//            button.querySelector('i').classList.add('fa-solid', 'fa-heart');
//        }
//
//    let favorites = JSON.parse(localStorage.getItem('favorites')) || [];
//
//    const index = favorites.indexOf(restaurantId);
//    if (index === -1) {
//        favorites.push(restaurantId);  // 찜 추가
//    } else {
//        favorites.splice(index, 1);  // 찜 취소
//    }
//
//    localStorage.setItem('favorites', JSON.stringify(favorites));
//}

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
            if (response && response.data && Array.isArray(response.data)) {
                const reviews = response.data;

                // 리뷰 데이터가 있을 때 평균 평점 계산
                const totalRating = reviews.reduce(function(acc, review) {
                    return acc + (review.starRating || 0);  // starRating이 없으면 0으로 처리
                }, 0);

                // 리뷰 개수가 0이면 평점은 0, 아니면 평점 계산
                reviewData.rating = reviews.length ? totalRating / reviews.length : 0;
                reviewData.reviewsCount = reviews.length;
            } else {
                console.warn("Invalid review data received.");
                reviewData.rating = 0;
                reviewData.reviewsCount = 0;
            }
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

});



// 찜 버튼의 상태를 업데이트하는 함수
function updateFavoriteButtonState(button, restaurantId) {
    // localStorage에서 즐겨찾기 데이터를 가져옵니다.
    const userId = document.getElementById("userId").value;
    let isFavorite = false ;
    $.ajax({
        url: `/api/bookmark/is-exist/${userId}/${restaurantId}`,
        type: 'GET',
        async: false,  // 동기식 요청
        success: function (response) {
            if(response.data){
               isFavorite = true;
               button.setAttribute('data-bookmark-id',response.data.id);
            }
        },
        error: function (xhr, status, error) {

        }
    });

    // const button = document.querySelector(`#favorite-btn-${restaurantId.dataset.id}`);

    // 버튼이 존재하지 않으면 경고 출력 후 함수 종료
    if (!button) {
        console.warn(`Button for restaurant ID ${restaurantId.dataset.id} not found.`);
        return;
    }

    if (isFavorite) {
        button.classList.add('favorited');
    } else {
        button.classList.remove('favorited');
    }

    button.dataset.favorite = isFavorite; // 이 부분에서 button이 undefined일 가능성이 있음
    button.innerHTML = isFavorite
        ? '<i class="fa-solid fa-heart"></i>'
        : '<i class="fa-regular fa-heart"></i>';

//    try {
//        isFavorite = JSON.parse(localStorage.getItem('favorites')) || [];
//    } catch (e) {
//        // localStorage 데이터가 JSON 파싱에 실패하면 빈 배열로 초기화
//        favorites = [];
//        localStorage.setItem('favorites', JSON.stringify(isFavorite));
//    }
//
//    // favorites가 배열인지 확인
//    if(!Array.isArray(isFavorite)) {
//        favorites = [];
//        localStorage.setItem('favorites', JSON.stringify(isFavorite));
//    }

//     레스토랑 ID가 favorites에 있는지 확인
//    isFavorite = isFavorite.includes(button.dataset.id);

    if (isFavorite) {
        button.classList.add('favorited');
        button.setAttribute('data-favorite', 'true');
        toggleFavoriteButton(button,'true');
    } else {
        button.classList.remove('favorited');
        button.setAttribute('data-favorite', 'false');
        toggleFavoriteButton(button,'false');
    }
}

// 즐겨찾기 버튼 토글여부 확인
function toggleFavoriteButton(button, isFavorite) {
    if (isFavorite) {
        // 찜을 추가
        button.dataset.favorite = 'true';
//        button.querySelector('i').classList.remove('fa-regular', 'fa-heart');
        button.querySelector('i').classList.add('fa-solid', 'fa-heart');
    } else {
        // 찜을 해제
        button.dataset.favorite = 'false';
        button.querySelector('i').classList.remove('fa-solid', 'fa-heart');
//        button.querySelector('i').classList.add('fa-regular', 'fa-heart');
    }
}

// 즐겨찾기 상태를 토글하는 함수
function toggleFavorite(restaurantId) {
    let favorites;
    try {
        favorites = JSON.parse(localStorage.getItem('favorites')) || [];
    } catch (e) {
        favorites = [];
    }

    // 즐겨찾기 상태 변경
    const index = favorites.indexOf(restaurantId);
    if (index === -1) {
        favorites.push(restaurantId); // 찜 추가
    } else {
        favorites.splice(index, 1); // 찜 제거
    }

    // 업데이트된 데이터를 localStorage에 저장
    localStorage.setItem('favorites', JSON.stringify(favorites));
}

// 유저의 즐겨찾기 데이터 불러오기
function loadUserBookmarks(userId) {
    fetch(`/api/bookmark/user/${userId}`)
       .then(response => response.json())
       .then(bookmarks => {
           // 로드된 즐겨찾기 데이터를 로컬 스토리지에 저장
           localStorage.setItem('favorites', JSON.stringify(bookmarks));
       });
}


//즐겨찾기 추가
function addToFavorites(button, userId) {
    // const button = document.querySelector(`[data-id="${restaurantId}"]`);
    button.setAttribute('data-favorite', 'true');
    const isFavorite = button.getAttribute('data-favorite');
    const userIdInput = document.getElementById("userId");

    if (!button || !userIdInput.value) {
        console.error("Restaurant ID 또는 User ID가 정의되지 않았습니다.");
        return;
    }

    // 요청 데이터
    const requestBody = {
        data: {
            restaurant: {
                id: button.dataset.id
            },
            user: {
                id: userIdInput.value
            }
        }
    };

    // 동기식 Ajax 요청
    $.ajax({
        url: '/api/bookmark',
        type: 'POST',
        contentType: 'application/json',
        data: JSON.stringify(requestBody),
        async: false, // 동기식으로 요청
        success: function (response) {
            button.setAttribute('data-favorite', isFavorite);
            button.setAttribute('data-bookmark-id', response.data.id); // 새로운 bookmarkId를 설정
            button.querySelector('i').classList.toggle("fa-solid");
            button.querySelector('i').classList.toggle("fa-regular");
            console.log(isFavorite ? "Added to favorites" : "Removed from favorites");

        },
        error: function (xhr, status, error) {
            console.error("Error adding to favorites:", error);

            // 실패 시, UI 상태 원복 (에러 발생 시)
            button.setAttribute('data-favorite', !isFavorite);
            button.setAttribute('data-bookmark-id', 'no-id');
            button.querySelector('i').classList.toggle("fa-solid");
            button.querySelector('i').classList.toggle("fa-regular");
        }
    });
}


// 즐겨찾기 삭제
function removeFromFavorites(button) {

    if (!(button instanceof HTMLButtonElement)) {
        console.error("The button is not a valid HTMLButtonElement", button);
        return;
    }

    const bookmarkId = button.getAttribute('data-bookmark-id'); // 버튼에서 북마크 ID 가져오기
    button.setAttribute('data-favorite', 'false');
    const isFavorite = button.getAttribute('data-favorite');

     if (!bookmarkId || bookmarkId === 'no-id') {
        console.error("Bookmark ID is missing!");
        return;
     }


    // 동기식 Ajax 요청
    $.ajax({
        url: `/api/bookmark/${bookmarkId}`,
        type: 'DELETE',
        async: false, // 동기식으로 요청
        success: function () {
            button.setAttribute('data-favorite', 'false');
            button.querySelector('i').classList.toggle("fa-solid");
            button.querySelector('i').classList.toggle("fa-regular");
            console.log("Removed from favorites");
        },
        error: function (xhr, status, error) {
            console.error("Error removing from favorites:", error);

            // 실패 시, UI 상태 원복 (에러 발생 시)
            button.setAttribute('data-favorite', 'true');
            button.querySelector('i').classList.remove('fa-regular');
            button.querySelector('i').classList.add('fa-solid');
        }
    });
}

// 기본 페이지와 페이지 크기 설정
let currentPage = 0;
const pageSize = 10;
let totalPages = 1;
let sortType = 'id';        // 기본 정렬 기준

// 레스토랑 데이터를 가져오는 함수
async function fetchRestaurants(sortType, page = currentPage) {
    console.log('Fetching with sort:', sortType, 'Page:', page);
     let apiUrl;

        // sortType에 따라 다른 API 주소로 요청
        if (sortType === 'mostVisit') {
            apiUrl = `/api/restaurant/popular/restaurants?page=${page}&size=${pageSize}`;
        } else if (sortType === 'recommendationScore') {
            apiUrl = `/api/restaurant/recommend/restaurants?page=${page}&size=${pageSize}`;
        } else {
            apiUrl = `/api/restaurant?page=${page}&size=${pageSize}&sort=${sortType},asc`;
        }


    try {
        // 레스토랑 정보 가져오기
        const restaurantResponse = await $.ajax({
            url: apiUrl,
            method: 'GET'
        });

        if (restaurantResponse && restaurantResponse.data) {
            console.log('레스토랑 데이터:', restaurantResponse.data);

            // 총 페이지 수 갱신
            totalPages = restaurantResponse.pagination.totalPages;

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
    updatePagination(totalPages); // 페이지 상태 갱신
}

// 정렬 기준 변경 처리 함수
function handleSortChange() {
    const sortType = $('#sortId').val(); // 인기순, 추천순 선택
    fetchRestaurants(sortType, currentPage); // 선택된 기준에 따라 레스토랑 데이터 가져오기
}

function updatePagination(totalPages) {
    $('#prevPage').prop('disabled', currentPage === 0); // 이전 버튼 비활성화 조건
    $('#nextPage').prop('disabled', currentPage === totalPages - 1); // 다음 버튼 비활성화 조건
    $('#currentPage').text(currentPage + 1); // 현재 페이지 표시
    $('#totalPages').text(totalPages); // 전체 페이지 수 표시
}




$(document).ready(function () {
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

});



console.log("main.js 끝");