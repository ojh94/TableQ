console.log("main.js 시작");

lucide.createIcons();

// 레스토랑 카드 생성 함수
function createRestaurantCard(restaurant, rating = 0, reviewsCount =0) {
    const card = document.createElement('div');
    const restaurantId = restaurant.id;
    let imageUrl = 'https://dummyimage.com/900x400/ced4da/6c757d.jpg';
    card.className = 'card';
    console.log('Received rating:', restaurant.rating);  // rating 값 확인
    console.log('Received reviewsCount:', restaurant.reviewsCount);  // reviewsCount 값 확인


    // rating 값이 유효한지 확인하고, 없으면 0으로 설정
    const validRating = (rating && !isNaN(rating)) ? rating : 0; // rating이 없거나 NaN이면 0으로 설정
    console.log('Valid rating:', validRating);  // validRating 값 확인

    $.ajax({
            url: `/api/restaurant-image/restaurant/${restaurantId}`,
            type: 'GET',
            async: false,
            success: function (response) {
                if("ERROR" !== response.resultCode) {
                    imageUrl = response.data[0].fileUrl;
                } else {
                    // 에러 발생 시
                }
            },
            error: function (xhr, status, error) {

            }
    });

    card.innerHTML =`
        <div class="card-header">
            <button id="favorite-btn-${restaurant.id}" class="favorite-btn" data-id="${restaurant.id}" data-favorite="${restaurant.isFavorite}" data-bookmark-id="${restaurant.bookmarkId || 'no-id'}">
                <i class="fa-regular fa-heart"></i>
            </button>
            <h4 class="card-title">${restaurant.name}</h4>
        </div>
        <div class="card-content">
            <img src="${imageUrl}" alt="${restaurant.name}" class="card-image">
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
    syncFavoriteButtonState(favoriteButton);


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

// 찜 버튼의 상태를 동기화하는 함수
function syncFavoriteButtonState(button) {

    const userId = document.getElementById("userId").value;
    const restaurantId = button.dataset.id;

    let isFavorite = false ;
    $.ajax({
        url: `/api/bookmark/is-exist/${userId}/${restaurantId}`,
        type: 'GET',
        async: false,  // 동기식 요청
        success: function (response) {
            if(response.resultCode !== 'ERROR'){
               isFavorite = true;
               button.setAttribute('data-bookmark-id',response.data.id);
            }
        },
        error: function (xhr, status, error) {

        }
    });

    // 버튼이 존재하지 않으면 경고 출력 후 함수 종료
    if (!button) {
        console.warn(`Button for restaurant ID ${restaurantId.dataset.id} not found.`);
        return;
    }

    button.dataset.favorite = isFavorite;

    renderFavoriteButton(button);
}

function toggleFavoriteStatus(button) {
    if (button.dataset.favorite === 'true') {
        // 현재 찜 상태
        // 현재 찜되어있으므로 해제로 토글
        requestToDeleteBookmark(button);
    } else {
        // 현재 찜 안된 상태
        // 현재 찜 안되어있으므로 추가로 토글
        requestToCreateBookmark(button);
    }
}

// 찜 버튼 그리기
function renderFavoriteButton(button) {
    if (button.dataset.favorite === 'true') {
        // 찜 추가 상태
        button.classList.add('favorited');
        button.querySelector('i').classList.remove('fa-regular', 'fa-heart');
        button.querySelector('i').classList.add('fa-solid', 'fa-heart');
    } else {
        // 찜 해제 상태
        button.classList.remove('favorited');
        button.querySelector('i').classList.remove('fa-solid', 'fa-heart');
        button.querySelector('i').classList.add('fa-regular', 'fa-heart');
        /*button.querySelector('i').classList.toggle("fa-solid");
        button.querySelector('i').classList.toggle("fa-regular");*/
    }
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
function requestToCreateBookmark(button) {

    const userId = document.getElementById("userId").value;
    const isFavorite = button.getAttribute('data-favorite')

    // button.setAttribute('data-favorite', true);;

    if (!button || !userId) {
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
                id: userId
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

            if(response.resultCode === 'OK') {
                button.setAttribute('data-favorite', true);
                button.setAttribute('data-bookmark-id', response.data.id); // 새로운 bookmarkId를 설정

                renderFavoriteButton(button);

                console.log(isFavorite ? "Added to favorites" : "Removed from favorites");
            } else {
                console.log('/api/bookmark의 create 응답 ERROR')
            }
        },
        error: function (xhr, status, error) {
            console.error("Error adding to favorites:", error);
            // 실패 시, 아무 것도 안함 (에러 발생 시)
        }
    });
}


// 즐겨찾기 삭제
function requestToDeleteBookmark(button) {

    if (!(button instanceof HTMLButtonElement)) {
        console.error("The button is not a valid HTMLButtonElement", button);
        return;
    }

    const bookmarkId = button.getAttribute('data-bookmark-id'); // 버튼에서 북마크 ID 가져오기
    button.setAttribute('data-favorite', false);
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
        success: function (response) {
            if(response.resultCode === 'OK') {

                button.setAttribute('data-favorite', false);

                renderFavoriteButton(button);

                console.log("Removed from favorites");
            } else {
                console.log('/api/bookmark의 delete 응답 ERROR')
            }
        },
        error: function (xhr, status, error) {
            console.error("Error removing from favorites:", error);

            // 실패 시, 아무것도 안함 (에러 발생 시)
        }
    });
}

// 기본 페이지와 페이지 크기 설정
let currentPage = 0;
// 1페이지 당 뜨는 개수
const pageSize = 6;
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



console.log("main.js 끝");