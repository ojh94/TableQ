document.addEventListener('DOMContentLoaded', () => {
  const searchForm = document.getElementById('searchForm');
  const searchInput = document.getElementById('searchInput');
  const sortSelect = document.getElementById('sortSelect');
  const restaurantGrid = document.getElementById('restaurantGrid');
  const restaurantGrid2 = document.getElementById('restaurantGrid2');

  // 아이콘 초기화
  lucide.createIcons();



  // 레스토랑 데이터 (실제로는 API에서 가져올 것입니다)
  const restaurants = [
      { id: 1, name: '레스토랑 1',img:"/img/ramen.jpg", type: '한식', location: '서울시 강남구', rating: 4.5, reviews: 120 },
      { id: 2, name: '레스토랑 2',img:"/img/ramen.jpg", type: '일식', location: '서울시 마포구', rating: 4.2, reviews: 85 },
      { id: 3, name: '레스토랑 3',img:"/img/ramen.jpg", type: '양식', location: '서울시 종로구', rating: 4.7, reviews: 150 },
  ];
  // 방문했던 레스토랑 데이터
  const restaurants2 = [
    { id: 1, name: '레스토랑 1',img:"/img/ramen.jpg", type: '한식', location: '서울시 강남구', rating: 4.5, reviews: 120 },

];

  // 레스토랑 카드 생성 함수
  function createRestaurantCard(restaurant) {
      const card = document.createElement('div');
      card.className = 'card';
      card.innerHTML = `
          <div class="card-header">
              <button class="favorite-btn" onclick="toggleFavorite(${restaurant.id})">
                  <i data-lucide="heart"></i>
              </button>
              <h4 class="card-title">${restaurant.name}</h4>
              <p class="card-description">${restaurant.type} • ${restaurant.location}</p>
          </div>
          <div class="card-content">
              <img src="${restaurant.img}" alt="${restaurant.name}" class="card-image">
              <div class="rating">
                  <i data-lucide="star" class="rating-star"></i>
                  <span class="rating-value">${restaurant.rating}</span>
                  <span class="rating-count">(${restaurant.reviews}+ 리뷰)</span>
              </div>
              <div class="location">
                  <i data-lucide="map-pin" class="location-icon"></i>
                  <span>${restaurant.location}</span>
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

    function createRestaurantCardDetail(restaurant) {
        const card = document.createElement('div');
        card.className = 'card';
        card.innerHTML = `
            <div class="card-header">
                <button class="favorite-btn" onclick="toggleFavorite(${restaurant.id})">
                    <i data-lucide="heart"></i>
                </button>
                <h4 class="card-title">${restaurant.name}</h4>
                <p class="card-description">${restaurant.type} • ${restaurant.location}</p>
            </div>
            <div class="card-content">

                <div class="rating">
                    <i data-lucide="star" class="rating-star"></i>
                    <span class="rating-value">${restaurant.rating}</span>
                    <span class="rating-count">(${restaurant.reviews}+ 리뷰)</span>
                </div>
                <div class="location">
                    <i data-lucide="map-pin" class="location-icon"></i>
                    <span>${restaurant.location}</span>
                </div>
            </div>

            </div>
        `;
        return card;
    }

  // 레스토랑 카드 렌더링
  if(restaurantGrid){
   restaurants.forEach(restaurant => {
        restaurantGrid.appendChild(createRestaurantCard(restaurant));
    });
  }

  if(restaurantGrid2){
  restaurants2.forEach(restaurant => {
      restaurantGrid2.appendChild(createRestaurantCard(restaurant));
  });
  }


 restaurants2.forEach(restaurant => {
    restaurantGrid3.appendChild(createRestaurantCardDetail(restaurant));
});



  // 아이콘 다시 초기화 (동적으로 추가된 요소에 대해)
  lucide.createIcons();
});


// 예약 기능
function bookRestaurant(restaurantId) {
  console.log('Booking restaurant:', restaurantId);
  const detailPageUrl = `/restaurant/${restaurantId}`; // 상세 페이지 URL
    window.location.href = detailPageUrl;
}


// '예약하기' 버튼 클릭 시 이벤트 핸들러
document.querySelectorAll(".btn-reserve").forEach((button) => {
  button.addEventListener("click", () => {
    alert("예약이 완료되었습니다!");
  });
});



document.addEventListener('DOMContentLoaded', () => {
  // 표시할 레스토랑 ID (필요에 따라 변경 가능)
  const selectedRestaurantId = '1';  // 이 ID에 맞는 레스토랑 정보 표시
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

    // 배경 이미지 업데이트
    document.body.style.backgroundImage = `url('/img/restaurant-${selectedRestaurantId}.jpg')`;
  }
});
