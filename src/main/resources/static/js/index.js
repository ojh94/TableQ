document.body.addEventListener('click', (event) => {
    if (event.target.classList.contains('favorite-btn')) {
        handleFavoriteClick(event);
    }
});

$(document).ready(function() {
    console.log("DOM fully loaded.");

    doMainProcess(); // 여전히 비동기 처리 필요하면 여기에 AJAX 방식 적용

    // 사용자 ID 가져오기
    const userId = $('#userId').val();
        if (!userId) {
            console.error("User ID is missing!");
            return;
        }

    if (!window.LucideInitialized) {
        lucide.createIcons();
        window.LucideInitialized = true;
    }

//    const container = document.querySelector(".card-header");
//    if (!container) {
//        console.error("Container for buttons not found!");
//        return;
//    }

     // 방문한 레스토랑 렌더링
     restaurantsVisited.forEach(restaurant => {
         fetchReviewData(restaurant.id)
             .then(({ rating, reviewsCount }) => {
                 restaurantGrid2.append(createRestaurantCard(restaurant, rating, reviewsCount));
                 lucide.createIcons();
             })
            .catch(error => console.error('Error fetching review data:', error));
     });

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

    const buttons = container.querySelectorAll('.favorite-btn');
    if (buttons.length === 0) {
        console.error('Favorite buttons not found. Check if the grid is rendered correctly.');
    } else {
        buttons.forEach(button => {
            button.addEventListener('click', handleFavoriteClick);
        });
    }


    // 즐겨찾기 데이터 가져오기
    getFavorites(userId);

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
    container.addEventListener('click', function(event) {
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
