$(document).ready(function () {
    const userId = $('#userId').val();
    let restaurantGrid = $('#restaurantGrid');
    let restaurantGrid2 = $('#restaurantGrid2');

    // 페이지 로드 시 즐겨찾기 목록 불러오기
    fetchFavoriteRestaurants(userId);

    // 찜 버튼 클릭 시 처리
    $(document).on('click', '.favorite-btn', function (event) {
        const button = $(this);
        const restaurantId = button.data('id');
        const icon = button.find('i');

        // 찜 상태 토글
        if (button.data('favorite') === true) {
            icon.removeClass("fa-solid fa-heart").addClass("fa-regular fa-heart");
            button.data('favorite', false);
        } else {
            icon.removeClass("fa-regular fa-heart").addClass("fa-solid fa-heart");
            button.data('favorite', true);
        }

        // 즐겨찾기 상태 업데이트
        toggleFavorite(restaurantId);
    });

    // 즐겨찾기 목록 불러오기
    function fetchFavoriteRestaurants(userId) {
        return $.ajax({
            url: `/api/bookmark/user/${userId}`,
            method: 'GET',
            async: false,
            dataType: 'json'
        })
            .done(function (response) {
                if (response.data && response.data.length > 0) {
                    console.log('즐겨찾기 데이터:', response.data);
                    renderFavoriteRestaurants(response.data);
                } else {
                    console.log('즐겨찾기 데이터가 없습니다.');
                }
            })
            .fail(function (error) {
                console.error('API 요청 중 오류가 발생했습니다:', error);
            });
    }

    // 예약 정보 가져오기
    function fetchReservationData(userId) {
        return $.ajax({
            url: `/api/reservation/user/${userId}`,
            type: 'GET',
            async: false,
            dataType: 'json'
        })
            .done(function (response) {
                const enteredReservations = response.data.filter(reservation => reservation.isEntered === true);
                return enteredReservations.map(reservation => reservation.restaurantId);
            })
            .fail(function (error) {
                console.error('Error fetching reservation data:', error);
                return [];
            });
    }

    // 레스토랑 카드 생성
    function createRestaurantCard(restaurant, rating = 0, reviewsCount = 0) {
        const card = $(`
            <div class="card">
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
                        <span class="rating-value">${rating.toFixed(1)}</span>
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
            </div>
        `);
        return card;
    }

    // 예약하기 동작
    function bookRestaurant(restaurantId) {
        console.log('Booking restaurant:', restaurantId);
        sessionStorage.setItem('restaurantId', restaurantId);
        window.location.href = `/restaurant/${restaurantId}`;
    }
});
