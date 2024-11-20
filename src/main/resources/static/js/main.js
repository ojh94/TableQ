$(document).ready(function () {
    const searchForm = document.getElementById('searchForm');
    const searchInput = document.getElementById('searchInput');
    const sortSelect = document.getElementById('sortSelect');
    const restaurantGrid = document.getElementById('restaurantGrid');
    const restaurantGrid2 = document.getElementById('restaurantGrid2');
    const userId = document.getElementById('userId').value;
//    const restaurantId = document.getElementById('restaurantId').value;
//    console.log('User ID:', userId); // userId 값 출력


    // '예약하기' 버튼 클릭 시 이벤트 핸들러
    document.querySelectorAll(".btn-reserve").forEach(button => {
        button.addEventListener("click", () => {
            alert("예약이 완료되었습니다!");
        });
    });

    // 검색 폼 이벤트 리스너
    searchForm.addEventListener('submit', (event) => {
        event.preventDefault();
        const query = searchInput.value.trim();
        if (query) {
            searchRestaurants(query)
                .then(renderRestaurants)
                .catch(error => console.error('Error searching restaurants:', error));
        }
    });


//     레스토랑 데이터 (실제로는 API에서 가져올 것입니다)
//     const restaurants = [
//         { id: 1, name: '레스토랑 1', img: "/img/ramen.jpg", type: '한식', location: '서울시 강남구', rating: 4.5, reviews: 120 },
//         { id: 2, name: '레스토랑 2', img: "/img/ramen.jpg", type: '일식', location: '서울시 마포구', rating: 4.2, reviews: 85 },
//         { id: 3, name: '레스토랑 3', img: "/img/ramen.jpg", type: '양식', location: '서울시 종로구', rating: 4.7, reviews: 150 },
//     ];
//     방문했던 레스토랑 데이터
//     const restaurants2 = [
//         { id: 1, name: '레스토랑 1', img: "/img/ramen.jpg", type: '한식', location: '서울시 강남구', rating: 4.5, reviews: 120 },
//     ];

    // 데이터 가져오기 및 렌더링
        fetchRestaurantsWithVisitedStatus(userId)
            .then(restaurants => {
                // 방문 여부에 따라 데이터 분리
                const restaurantsNotVisited = restaurants.filter(restaurant => !restaurant.visited);
                const restaurantsVisited = restaurants.filter(restaurant => restaurant.visited);

                // 방문하지 않은 레스토랑 렌더링
                restaurantsNotVisited.forEach(restaurant => {
                    fetchReviewData(restaurant.id)
                        .then(({ rating, reviewsCount }) => {
                            restaurantGrid.appendChild(createRestaurantCard(restaurant, rating, reviewsCount));
                            lucide.createIcons();

                        })
                        .catch(error => console.error('Error fetching review data:', error));
                });

//                // 방문한 레스토랑 렌더링
//                restaurantsVisited.forEach(restaurant => {
//                    if (restaurant.isEntered === true) {
//                        fetchReviewData(restaurant.id)
//                            .then(({ rating, reviewsCount }) => {
//                                restaurantGrid.appendChild(createRestaurantCard(restaurant, rating, reviewsCount));
//                                lucide.createIcons();
//                                console.log('Appending to Grid2:', card); // 확인용
//                                console.log('Rating:', rating, 'Reviews:', reviewsCount); // 확인용
//                                restaurantGrid2.appendChild(card);
//                                })
//                               .catch(error => console.error('Error fetching review data:', error));
//                    }
//                });
            })
            .catch(error => console.error('Error during restaurant rendering:', error));

     //찜 버튼 구현
     fetch(`/api/bookmark/user/${userId}`)
                .then(response => response.json())
                .then(data => {
                    if (data.resultCode === 'OK' && data.data.length > 0) {
                        const bookmarkedRestaurants = data.data.map(item => item.restaurant_id);

                        // 찜한 레스토랑에 대한 버튼 스타일 변경
                        bookmarkedRestaurants.forEach(restaurantId => {
                            const button = document.getElementById(`favorite-btn-${restaurantId}`);
                            if (button) {
                                const icon = button.querySelector('i');
                                if (icon) {
                                    icon.classList.remove("fa-regular", "fa-heart"); // 기존 스타일 제거
                                    icon.classList.add("fa-solid", "fa-heart"); // 채워진 하트 스타일 추가
                                    button.setAttribute('data-favorite', 'true');
                                }
                            }
                        });
                    }
                })
                .catch(error => {
                    console.error('Error fetching bookmarked restaurants:', error);
                });

    // 데이터 가져오기 및 렌더링
    fetch(`/api/reservation/user/${userId}`)
        .then(response => response.json())
        .then(data => {
            // isEntered가 true인 예약만 필터링
            const enteredReservations = data.data.filter(reservation => reservation.isEntered === true);

            // 레스토랑 ID 추출 (예약된 레스토랑만)
            const restaurantIds = enteredReservations.map(reservation => reservation.restaurantId);

             // **초기화 코드**
             restaurantGrid2.innerHTML = ''; // 이전 내용을 초기화

            // 필터링된 레스토랑 ID를 기반으로 레스토랑 정보 가져오기
            Promise.all(
                restaurantIds.map(restaurantId =>
                    fetchRestaurantById(restaurantId).then(restaurant => ({
                        restaurantId,
                        restaurant, // null일 수도 있음
                    }))
                )
            )
                .then(results => {
                     // 중복 데이터 제거
                    const uniqueResults = results.filter((value, index, self) =>
                        index === self.findIndex((t) => t.restaurantId === value.restaurantId)
                    );

                    uniqueResults.forEach(({ restaurantId, restaurant }) => {
                        if (!restaurant) {
                            console.warn(`Restaurant data not available for ID: ${restaurantId}`);
                            return; // 유효하지 않은 데이터는 무시
                        }

                        const restaurantData = restaurant.data;

                        fetchReviewData(restaurantData.id)
                            .then(({ rating = 0, reviewsCount = 0 }) => {
                                restaurantGrid2.appendChild(
                                    createRestaurantCard(restaurantData, rating, reviewsCount)
                                );
                                lucide.createIcons();
                            })
                            .catch(error => {
                                console.error(
                                    `Error fetching review data for restaurant ID: ${restaurantData.id}`,
                                    error
                                );
                            });
                    });
                })
                .catch(error => {
                    console.error('Error processing restaurant data:', error);
                });

        })
        .catch(error => console.error('Error fetching reservations:', error));



    // 특정 레스토랑 정보 표시
    // const selectedRestaurantId = '1';
    // const selectedRestaurant = document.querySelector(`.restaurant-item[data-id="${selectedRestaurantId}"]`);
    //
    // if (selectedRestaurant) {
    //     const restaurantName = selectedRestaurant.getAttribute('data-name');
    //     const restaurantRating = selectedRestaurant.getAttribute('data-rating');
    //     const restaurantReviews = selectedRestaurant.getAttribute('data-reviews');
    //
    //     // 헤더 정보 업데이트
    //     const header = document.querySelector('header');
    //     header.innerHTML += `
    //         <div class="restaurant-info">
    //             <h1>${restaurantName} (ID: ${selectedRestaurantId})</h1>
    //             <p>별점: ${restaurantRating} (리뷰: ${restaurantReviews}개)</p>
    //         </div>
    //     `;
    // }
});

document.querySelectorAll('.favorite-btn').forEach(button => {
    button.addEventListener('click', () => {
        const restaurantId = button.getAttribute('data-id');
        const icon = button.querySelector('i');
        const isFavorite = button.getAttribute('data-favorite') === 'true';

        // 찜 상태 변경 (하트 아이콘)
        if (isFavorite) {
            icon.classList.remove("fa-solid", "fa-heart");
            icon.classList.add("fa-regular", "fa-heart");
            button.setAttribute('data-favorite', 'false');
        } else {
            icon.classList.remove("fa-regular", "fa-heart");
            icon.classList.add("fa-solid", "fa-heart");
            button.setAttribute('data-favorite', 'true');
        }

        // 여기에서 서버에 즐겨찾기 상태를 업데이트하는 코드 추가 가능
        toggleFavorite(restaurantId);
    });
});

// 동적으로 버튼이 추가될 때마다 호출
function addFavoriteButtonListener() {
    const buttons = document.querySelectorAll('.favorite-btn');
    buttons.forEach(button => {
        button.addEventListener('click', () => {
            const restaurantId = button.getAttribute('data-id');
            const icon = button.querySelector('i');

            if (button.getAttribute('data-favorite') === 'true') {
                icon.classList.remove("fa-solid", "fa-heart");
                icon.classList.add("fa-regular", "fa-heart");
                button.setAttribute('data-favorite', 'false');
            } else {
                icon.classList.remove("fa-regular", "fa-heart");
                icon.classList.add("fa-solid", "fa-heart");
                button.setAttribute('data-favorite', 'true');
            }

            toggleFavorite(restaurantId);
        });
    });
}

// 예시: 버튼이 추가된 후에 호출
addFavoriteButtonListener();

function toggleFavorite(restaurantId) {
    const isFavorite = document.querySelector(`#favorite-btn-${restaurantId}`).getAttribute('data-favorite') === 'true';
    fetch(`/api/restaurant/favorite/${restaurantId}`, {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ isFavorite })
    }).then(response => {
        if (!response.ok) {
            alert("찜 상태 변경에 실패했습니다.");
        }
        return response.json();
    })
    .then(data => {
        console.log("찜 상태 업데이트 성공", data);
    })
    .catch(err => {
        console.error("Error while updating favorite:", err);
        alert("네트워크 오류가 발생했습니다.");
    });
}

function fetchRestaurantsWithVisitedStatus(userId) {
    return Promise.all([
        fetchAllRestaurants(),               // 모든 레스토랑 데이터
        fetchUserReviewedRestaurants(userId) // 사용자가 리뷰한 레스토랑 ID 목록
    ])
    .then(([restaurants, reviewedRestaurantIds]) => {
        // visited 상태를 설정
        restaurants.forEach(restaurant => {
            restaurant.visited = reviewedRestaurantIds.includes(Number(restaurant.id));
        });
        return restaurants; // visited 상태가 포함된 레스토랑 데이터 반환
    })
    .catch(error => {
        console.error('Error fetching data:', error);
    });
}


// 예약 기능
function bookRestaurant(restaurantId) {
    console.log('Booking restaurant:', restaurantId);
    sessionStorage.setItem('restaurantId', restaurantId);
    const detailPageUrl = `/restaurant/${restaurantId}`;
    window.location.href = detailPageUrl;
}

// 유저가 작성한 리뷰를 기반으로 레스토랑 가져오기
function fetchUserReviewedRestaurants(userId) {
    return fetch(`/api/review/user/${userId}`)
        .then(response => {
            console.log('API Response:', response); // 응답 로그 확인
            if (!response.ok) throw new Error('Failed to fetch user reviews');
            return response.json();
        })
        .then(data => {
            console.log('Data:', data);
            return data.data.map(review => review.restaurantId) || [];
        })
        .catch(function (error) {
            console.error('Error fetching user reviews:', error); // 오류 처리
        });
}

// 특정 레스토랑 데이터를 가져오는 함수
function fetchRestaurantById(restaurantId) {
    return fetch(`/api/restaurant/${restaurantId}`)
        .then(response => {
            if (!response.ok) {
                console.error(`Failed to fetch restaurant with ID ${restaurantId}:`, response.status);
                throw new Error(`Failed to fetch restaurant with ID ${restaurantId}`);
            }
            return response.json();
        })
        .then(data => {
            // 반환된 데이터 검증
            if (!data || !data.data || !data.data.id) {
                console.warn(`Invalid restaurant data for ID ${restaurantId}:`, data);
                return null; // 유효하지 않은 데이터를 null로 반환
            }
            return data;
        })
        .catch(error => {
            console.error(`Error fetching restaurant with ID ${restaurantId}:`, error);
            return null; // 에러가 발생했을 때도 null로 반환
        });
}


// 레스토랑 카드 생성 함수
function createRestaurantCard(restaurant, rating = 0, reviewsCount = 0) {
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
            <button class="book-btn" onclick="bookRestaurant(${restaurant.id})">
                <i data-lucide="calendar"></i> 예약하기
            </button>
        </div>
    `;
    return card;
}

// 검색 요청 함수
function searchRestaurants(query) {
    return fetch(`/api/restaurant/keywordSearch?arg0=${encodeURIComponent(query)}`)
        .then(response => response.ok ? response.json() : Promise.reject('Failed to search restaurants'))
        .then(data => Array.isArray(data) ? data : data.data);
}

// 검색 결과 렌더링 함수
function renderRestaurants(restaurants) {
    restaurantGrid.innerHTML = '';
    restaurants.forEach(restaurant => {
        fetchReviewData(restaurant.id)
            .then(({ rating, reviewsCount }) => {
                restaurantGrid.appendChild(createRestaurantCard(restaurant, rating, reviewsCount));
                lucide.createIcons();
            })
            .catch(error => console.error('Error fetching review data:', error));
    });
}

// 모든 레스토랑 데이터를 가져오는 함수
function fetchAllRestaurants() {
    return fetch('/api/restaurant')
        .then(response => response.ok ? response.json() : Promise.reject('Failed to fetch all restaurants'))
        .then(data => Array.isArray(data) ? data : data.data)
        .then(restaurants => restaurants.sort((a, b) => a.id - b.id));
}

// 특정 레스토랑의 리뷰 및 총 리뷰 수 가져오는 함수
function fetchReviewData(restaurantId) {
    return fetch(`/api/review/restaurant/${restaurantId}?page=0&size=1`)
     .then(response => response.ok ? response.json() : Promise.reject(`Failed to fetch reviews for restaurant ID ${restaurantId}`))
     .then(data => ({
       rating: data.data[0]?.starRating || 0,
       reviewsCount: data.pagination.totalElements || 0
     }))
     .catch(error => console.error('Error fetching review data:', error));
}


let initialized = false;
document.addEventListener('DOMContentLoaded', () => {
    if (initialized) return; // 이미 초기화된 경우 종료
    initialized = true;

    // 여기에서 이벤트 리스너 추가 및 초기화 작업 수행
});

console.log('restaurantGrid2:', restaurantGrid2); // 확인용
console.log('restaurantGrid2 before appending:', restaurantGrid2.innerHTML);

