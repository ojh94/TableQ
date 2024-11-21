document.body.addEventListener('click', function(event) {
        if (event.target && event.target.classList.contains('favorite-btn')) {
        handleFavoriteClick(event);
    }
});

$(document).ready(function() {
    console.log("DOM fully loaded.");
    const userId = $('#userId').val();
    let restaurantGrid = $('#restaurantGrid');
    var restaurantGrid2 = $('#restaurantGrid2');

    // grid가 정상적으로 렌더링되었는지 확인
    var restaurantGrid2 = $('#restaurantGrid2');
    if (restaurantGrid2.length) {
        console.log("Grid is rendered correctly");

        // favorite 버튼 찾기
        var favoriteButtons = restaurantGrid2.find('.favorite-btn');
        if (favoriteButtons.length) {
            console.log("Favorite buttons found", favoriteButtons);
            // 여기서 favorite 버튼 관련 작업 수행
        } else {
            console.log("Favorite buttons not found");
        }
    }


//    doMainProcess(); // 여전히 비동기 처리 필요하면 여기에 AJAX 방식 적용



    if (!window.LucideInitialized) {
        lucide.createIcons();
        window.LucideInitialized = true;
    }

//    const container = document.querySelector(".card-header");
//    if (!container) {
//        console.error("Container for buttons not found!");
//        return;
//    }

//     // 방문한 레스토랑 렌더링
//     restaurantsVisited.forEach(restaurant => {
//         fetchReviewData(restaurant.id)
//             .then(({ rating, reviewsCount }) => {
//                 restaurantGrid2.append(createRestaurantCard(restaurant, rating, reviewsCount));
//                 lucide.createIcons();
//             })
//            .catch(error => console.error('Error fetching review data:', error));
//     });



     // 예약 정보 가져오기
     fetchReservationData(userId)
         .then(restaurantIds => {
             restaurantGrid2.empty(); // 기존 내용 초기화
             restaurantIds.forEach(restaurantId => {
                 fetchRestaurantById(restaurantId)
                     .then(restaurant => {
                         if (restaurant) {
                             fetchReviewData(restaurant.id)
                                 .then(({ rating, reviewsCount }) => {
                                     restaurantGrid2.append(createRestaurantCard(restaurant, rating, reviewsCount));
                                     lucide.createIcons();
                                 })
                                 .catch(error => console.error(`Error fetching review data for restaurant ID: ${restaurant.id}`, error));
                         }
                     })
                     .catch(error => console.error(`Error fetching restaurant data for ID: ${restaurantId}`, error));
             });
         })
         window.fetchReservationData = fetchReservationData;



//
//    // 즐겨찾기 데이터 가져오기
//    getFavorites(userId);

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

    // 이벤트 위임을 사용하여 버튼 클릭 처리
    document.addEventListener('click', function(event) {
        if (event.target && event.target.matches('.favorite-btn')) {
            const button = event.target;
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
        }
    });



    // 검색 및 정렬 기능은 여기서 계속 처리
    const searchForm = document.getElementById("searchForm");
    const searchInput = document.getElementById("searchInput");
    searchForm.addEventListener("submit", (e) => {
        e.preventDefault();
        console.log("Searching for:", searchInput.value);
        // 여기에 실제 검색 로직 구현
    });

    const sortSelect = document.getElementById("sortSelect");
    sortSelect.addEventListener("change", () => {
        console.log("Sorting by:", sortSelect.value);
        // 여기에 실제 정렬 로직 구현
    });
}); // DOMContentLoaded 이벤트 핸들러 닫기



    // 즐겨찾기 추가
    function addToFavorites(restaurantId, userId) {
      const data = {
        restaurantId: restaurantId,
        userId: userId
      };

      $.ajax({
        url: '/api/bookmark',
        method: 'POST',
        contentType: 'application/json',
        async: false,
        data: JSON.stringify(data)
      })
      .done(function(response) {
        if (response.resultCode === 'OK') {
          console.log('즐겨찾기 추가 성공');
          // 즐겨찾기 추가 후, 즐겨찾기 목록을 다시 로드하여 UI 업데이트
          fetchFavoriteRestaurants(userId);
        } else {
          console.error('즐겨찾기 추가 실패');
        }
      })
      .fail(function(error) {
        console.error('API 요청 중 오류가 발생했습니다:', error);
      });
    }

    // 즐겨찾기 삭제
    function removeFromFavorites(bookmarkId, userId) {
      $.ajax({
        url: `/api/bookmark/${bookmarkId}`,
        method: 'DELETE',
        async: false
      })
      .done(function(response) {
        if (response.resultCode === 'OK') {
          console.log('즐겨찾기 삭제 성공');
          // 즐겨찾기 삭제 후, 즐겨찾기 목록을 다시 로드하여 UI 업데이트
          fetchFavoriteRestaurants(userId);
        } else {
          console.error('즐겨찾기 삭제 실패');
        }
      })
      .fail(function(error) {
        console.error('API 요청 중 오류가 발생했습니다:', error);
      });
    }



    // 즐겨찾기 레스토랑을 화면에 렌더링하는 함수
    function renderFavoriteRestaurants(favoriteRestaurants) {
        const container = $('#favorite-restaurants-container');
        container.empty(); // 기존의 즐겨찾기 목록을 지우고

        if (favoriteRestaurants && favoriteRestaurants.length > 0) {
            favoriteRestaurants.forEach(function(restaurant) {
                const restaurantElement = $('<div>').addClass('restaurant');
                restaurantElement.html(`
                <h3>${restaurant.restaurant_name}</h3>
                <p>${restaurant.restaurant_address}</p>
                <button onclick="removeFromFavorites(${restaurant.id}, ${userId})">즐겨찾기 삭제</button>
            `);
              container.append(restaurantElement);
            });
        } else {
          container.append('<p>즐겨찾기한 레스토랑이 없습니다.</p>');
        }
    }





    $('#add-favorite-button').on('click', function() {

      addToFavorites(restaurantId, currentUserId); // 즐겨찾기 추가 요청
    });


    const buttons = document.querySelectorAll('.favorite-btn');
        if (buttons.length === 0) {
            console.error('Favorite buttons not found. Check if the grid is rendered correctly.');
        } else {
            buttons.forEach(button => {
                button.addEventListener('click', handleFavoriteClick);
            });
        }

    fetchFavoriteRestaurants().then(function(data) {
        // 데이터 로딩 후 버튼 찾기
        var restaurantGrid2 = $('#restaurantGrid2');
        var favoriteButtons = restaurantGrid2.find('.favorite-btn');
        // 버튼 관련 처리
    });

