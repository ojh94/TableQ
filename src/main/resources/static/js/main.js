function doMainProcess() {
    const userId = $('#userId').val();
    const restaurantGrid = $('#restaurantGrid');
    const restaurantGrid2 = $('#restaurantGrid2');

    function fetchRestaurantsWithVisitedStatus(userId) {
        return $.ajax({
            url: '/api/restaurant',
            type: 'GET',
             async: false,
            dataType: 'json'
        })
            .then(restaurants => {
                return $.ajax({
                    url: `/api/review/user/${userId}`,
                    type: 'GET',
                    async: false,
                    dataType: 'json'
                }).then(response => {
                    const reviewedRestaurantIds = response.data.map(review => review.restaurantId);
                    restaurants.forEach(restaurant => {
                        restaurant.visited = reviewedRestaurantIds.includes(Number(restaurant.id));
                    });
                    return restaurants;
                });
            })
            .catch(error => {
                console.error('Error fetching restaurants or user reviews:', error);
                return [];
            });
    }



//    // 레스토랑 방문 여부 가져오기
//    function fetchRestaurantsWithVisitedStatus(userId) {
//        return $.get('/api/restaurant')
//            .then(restaurants => {
//                return $.get(`/api/review/user/${userId}`)
//                    .then(response => {
//                        const reviewedRestaurantIds = response.data.map(review => review.restaurantId);
//                        restaurants.forEach(restaurant => {
//                            restaurant.visited = reviewedRestaurantIds.includes(Number(restaurant.id));
//                        });
//                        return restaurants;
//                    });
//            })
//            .catch(error => {
//                console.error('Error fetching restaurants or user reviews:', error);
//                return [];
//            });
//    }

    // 특정 레스토랑 정보 가져오기
    function fetchRestaurantById(restaurantId) {
        return $.get(`/api/restaurant/${restaurantId}`)
            .then(response => response.data)
            .catch(error => {
                console.error(`Error fetching restaurant by ID ${restaurantId}:`, error);
                return null;
            });
    }



    // 특정 레스토랑의 리뷰 데이터 가져오기
    function fetchReviewData(restaurantId) {
        return $.get(`/api/review/restaurant/${restaurantId}`)
            .then(data => ({
                rating: data.rating || 0,
                reviewsCount: data.reviewsCount || 0
            }))
            .catch(error => {
                console.error('Error fetching review data:', error);
                return { rating: 0, reviewsCount: 0 };
            });
    }

    // 유저의 예약 정보 가져오기
    function fetchReservationData(userId) {
        return $.ajax({
            url: `/api/reservation/user/${userId}`,
            type: 'GET',
            async: false,
            dataType: 'json'
        })
            .then(response => {
                const enteredReservations = response.data.filter(reservation => reservation.isEntered === true);
                return enteredReservations.map(reservation => reservation.restaurantId);
            })
            .catch(error => {
                console.error('Error fetching reservation data:', error);
                return [];
            });
    }

    // 레스토랑 카드 생성 함수
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

    // 예약하기 버튼 동작
    function bookRestaurant(restaurantId) {
        console.log('Booking restaurant:', restaurantId);
        sessionStorage.setItem('restaurantId', restaurantId);
        window.location.href = `/restaurant/${restaurantId}`;
    }

//    // 유저의 찜한 레스토랑 불러오기
//        function fetchBookmarkedRestaurants(userId) {
//            return $.get(`/api/bookmark/user/${userId}`)
//                .then(data => {
//                    if (data.resultCode === 'OK' && data.data.length > 0) {
//                        const bookmarkedRestaurants = data.data.map(item => item.restaurant_id);
//                        bookmarkedRestaurants.forEach(restaurantId => {
//                            const button = $(`#favorite-btn-${restaurantId}`);
//                            const icon = button.find('i');
//                            if (button.length > 0) {
//                                icon.removeClass('fa-regular fa-heart').addClass('fa-solid fa-heart');
//                                button.attr('data-favorite', 'true');
//                            }
//                        });
//                    }
//                })
//                .catch(error => console.error('Error fetching bookmarked restaurants:', error));
//        }

    // 즐겨찾기 목록 불러오기
        function getFavorites(userId) {
            $.ajax({
                url: `/api/bookmark/user/${userId}`,
                type: 'GET',
                success: function(data) {
                    const favoriteRestaurants = data.data;
                    console.log("Favorite restaurants data:", favoriteRestaurants);

                    favoriteRestaurants.forEach((item) => {
                        const button = document.querySelector(`.favorite-btn[data-id="${item.restaurantId}"]`);
                        const icon = button?.querySelector("i");
                        if (button) {
                            button.setAttribute("data-favorite", "true");
                            if (icon) {
                                icon.classList.remove("fa-regular");
                                icon.classList.add("fa-solid"); // 채워진 하트
                            }
                        }
                    });
                },
                error: function(error) {
                    console.error("Error fetching favorite restaurants:", error);
                }
            });
        }

            // 즐겨찾기 추가
            function addFavorite(restaurantId, button, icon, userId) {
                $.ajax({
                    url: '/api/bookmark',
                    type: 'POST',
                    contentType: 'application/json',
                    data: JSON.stringify({
                        userId: userId,
                        restaurantId: restaurantId,
                    }),
                    success: function(data) {
                        if (data.resultCode === "OK") {
                            button.setAttribute("data-favorite", "true");
                            icon.classList.remove("fa-regular");
                            icon.classList.add("fa-solid"); // 채워진 하트
                            console.log("Favorite added:", restaurantId);
                        } else {
                            console.error("Failed to add favorite:", data.description);
                        }
                    },
                    error: function(error) {
                        console.error("Error adding favorite:", error);
                    }
                });
            }

            // 즐겨찾기 삭제
            function removeFavorite(restaurantId, button, icon) {
                $.ajax({
                    url: `/api/bookmark/${restaurantId}`,
                    type: 'DELETE',
                    success: function(data) {
                        if (data.resultCode === "OK") {
                            button.setAttribute("data-favorite", "false");
                            icon.classList.remove("fa-solid");
                            icon.classList.add("fa-regular"); // 비어있는 하트
                            console.log("Favorite removed:", restaurantId);
                        } else {
                            console.error("Failed to remove favorite:", data.description);
                        }
                    },
                    error: function(error) {
                        console.error("Error removing favorite:", error);
                    }
                });
            }

}









