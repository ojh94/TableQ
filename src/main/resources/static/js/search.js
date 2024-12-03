console.log("search.js 시작");

// 정렬 함수
function sortRestaurants(restaurants, sortType) {
    switch (sortType) {
        case 'rating':
            return restaurants.sort((a, b) => b.rating - a.rating);  // 별점 높은순
        case 'reviewsCount':
            return restaurants.sort((a, b) => b.reviewsCount - a.reviewsCount);  // 리뷰 많은순
        default:
            return restaurants.sort((a, b) => a.id - b.id);  // 기본 id 오름차순
    }
}

// 레스토랑 검색 함수
async function searchRestaurants(keyword, page = currentPage) {
    // 검색어를 URL에 올바르게 인코딩하여 전달
    const apiUrl = `/api/restaurant/keyword/search?name=${encodeURIComponent(keyword)}&page=${page}&size=${pageSize}`;
    console.log("검색 API 호출 URL:", apiUrl);

    try {
        const restaurantResponse = await $.ajax({
            url: apiUrl,
            type: 'GET',
        });

        console.log("API 응답 데이터:", restaurantResponse);

        if (restaurantResponse && restaurantResponse.data) {
            totalPages = restaurantResponse.pagination.totalPages;

            const reviewPromises = restaurantResponse.data.map(async (restaurant) => {
                try {
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
                        rating: Math.round(rating * 10) / 10,
                        reviewsCount: reviews.length,
                    };
                } catch (error) {
                    console.error(`Failed to fetch reviews for restaurant ID: ${restaurant.id}`, error);
                    return {
                        ...restaurant,
                        rating: 0,
                        reviewsCount: 0,
                    };
                }
            });

            const mergedRestaurants = await Promise.all(reviewPromises);

            // 정렬 적용
            const sortedRestaurants = sortRestaurants(mergedRestaurants, sortType);
            console.log('정렬된 레스토랑 데이터:', sortedRestaurants);

            renderRestaurantCards(sortedRestaurants);
            updatePagination(restaurantResponse.pagination.totalPages);
        } else {
            console.error('Invalid response format:', restaurantResponse);
        }
    } catch (error) {
        console.error("Error fetching search results:", error.responseJSON || error);
    }
}



// DOM 이벤트 처리
$(document).ready(function () {
    console.log("DOM fully loaded.");

    // 검색 버튼 클릭
    $('#searchBtn').on('click', function () {
        const keyword = $('#searchInput').val().trim();
        console.log("입력된 검색어:", keyword);

        if (keyword) {
            searchRestaurants(keyword);
        } else {
            console.warn("검색어가 입력되지 않았습니다.");
        }
    });
});

console.log("search.js 끝");
