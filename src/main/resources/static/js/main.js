document.addEventListener('DOMContentLoaded', () => {
  const searchForm = document.getElementById('searchForm');
  const searchInput = document.getElementById('searchInput');
  const sortSelect = document.getElementById('sortSelect');
  const restaurantGrid = document.getElementById('restaurantGrid');
  const restaurantGrid2 = document.getElementById('restaurantGrid2');

  // 모든 레스토랑 데이터를 가져오는 함수
  function fetchAllRestaurants() {
    return fetch('/api/restaurant')
      .then(response => {
        if (!response.ok) {
          throw new Error('Failed to fetch all restaurants');
        }
        return response.json();
      })
      .then(data => {
        const restaurants = Array.isArray(data) ? data : data.data;
        return restaurants.sort((a, b) => a.id - b.id);
      });
  }

  // 특정 레스토랑의 리뷰와 평점 데이터를 가져오는 함수
  function fetchReviewData(restaurantId) {
    return fetch(`/api/review/restaurant/${restaurantId}`)
      .then(response => {
        if (!response.ok) {
          throw new Error(`Failed to fetch reviews for restaurant ID ${restaurantId}`);
        }
        return response.json();
      });
  }

   // 레스토랑 데이터 (실제로는 API에서 가져올 것입니다)
  //  const restaurants = [
  //      { id: 1, name: '레스토랑 1',img:"/img/ramen.jpg", type: '한식', location: '서울시 강남구', rating: 4.5, reviews: 120 },
  //      { id: 2, name: '레스토랑 2',img:"/img/ramen.jpg", type: '일식', location: '서울시 마포구', rating: 4.2, reviews: 85 },
  //      { id: 3, name: '레스토랑 3',img:"/img/ramen.jpg", type: '양식', location: '서울시 종로구', rating: 4.7, reviews: 150 },
  //  ];
  //  // 방문했던 레스토랑 데이터
  //  const restaurants2 = [
  //    { id: 1, name: '레스토랑 1',img:"/img/ramen.jpg", type: '한식', location: '서울시 강남구', rating: 4.5, reviews: 120 },
  //
  //];

  // 레스토랑 카드 생성 함수
  function createRestaurantCard(restaurant, rating = 0, reviewsCount = 0) {
    const card = document.createElement('div');
    card.className = 'card';
    card.innerHTML = `
      <div class="card-header">
        <button class="favorite-btn" onclick="toggleFavorite(${restaurant.id})">
          <i data-lucide="heart"></i>
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

  // 전체 레스토랑 데이터 가져와서 렌더링
  fetchAllRestaurants()
    .then(data => {
      const restaurants = data.filter(restaurant => !restaurant.visited);
      const restaurants2 = data.filter(restaurant => restaurant.visited);

      restaurants.forEach(restaurant => {
        fetchReviewData(restaurant.id)
          .then(reviewData => {
            const rating = reviewData.rating || 0;
            const reviewsCount = reviewData.reviews || 0;
            restaurantGrid.appendChild(createRestaurantCard(restaurant, rating, reviewsCount));
            lucide.createIcons();
          })
          .catch(error => console.error('Error fetching review data:', error));
      });

      restaurants2.forEach(restaurant => {
        fetchReviewData(restaurant.id)
          .then(reviewData => {
            const rating = reviewData.rating || 0;
            const reviewsCount = reviewData.reviews || 0;
            restaurantGrid2.appendChild(createRestaurantCard(restaurant, rating, reviewsCount));
            lucide.createIcons();
          })
          .catch(error => console.error('Error fetching review data:', error));
      });
    })
    .catch(error => console.error('Error fetching restaurant data:', error));

  // 예약 기능
  function bookRestaurant(restaurantId) {
    console.log('Booking restaurant:', restaurantId);
    sessionStorage.setItem('restaurantId', restaurantId);
    const detailPageUrl = `/restaurant/${restaurantId}`;
    window.location.href = detailPageUrl;
  }

  // '예약하기' 버튼 클릭 시 이벤트 핸들러
  document.querySelectorAll(".btn-reserve").forEach(button => {
    button.addEventListener("click", () => {
      alert("예약이 완료되었습니다!");
    });
  });

  // 특정 레스토랑 정보 표시
  const selectedRestaurantId = '1';
  const selectedRestaurant = document.querySelector(`.restaurant-item[data-id="${selectedRestaurantId}"]`);

  if (selectedRestaurant) {
    const restaurantName = selectedRestaurant.getAttribute('data-name');
    const restaurantRating = selectedRestaurant.getAttribute('data-rating');
    const restaurantReviews = selectedRestaurant.getAttribute('data-reviews');

    // 헤더 정보 업데이트
    const header = document.querySelector('header');
    header.innerHTML += `
      <div class="restaurant-info">
        <h1>${restaurantName} (ID: ${selectedRestaurantId})</h1>
        <p>별점: ${restaurantRating} (리뷰: ${restaurantReviews}개)</p>
      </div>
    `;
  }
});

