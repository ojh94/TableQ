console.log("search.js 시작");

// 레스토랑 검색 함수
async function fetchSearchResults(keyword, page = currentPage) {
    const apiUrl = `/api/restaurant/keyword/search?name=${encodeURIComponent(keyword)}&page=${page}&size=${pageSize}`;
    console.log("검색 API 호출 URL:", apiUrl);

    try {
        const restaurantResponse = await $.ajax({
            url: apiUrl,
            type: 'GET',
        });

        if (restaurantResponse && restaurantResponse.data) {
            isSearchMode = true;
            searchResults = restaurantResponse.data; // 검색 결과 저장
            currentPage = page;
            totalPages = restaurantResponse.pagination.totalPages;
            console.log('검색 결과:', searchResults);

            const reviewPromises = searchResults.map(async (restaurant) => {
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
                        rating: 0, // 기본값 설정
                        reviewsCount: 0,
                    };
                }
            });

            const mergedRestaurants = await Promise.all(reviewPromises);
            console.log('결합된 레스토랑 데이터:', mergedRestaurants);

            // 렌더링 및 페이지네이션 업데이트
            const start = currentPage * pageSize;
            const end = start + pageSize;
            renderRestaurantCards(mergedRestaurants.slice(start, end)); // 페이지네이션 반영
            updatePagination(totalPages); // 페이지 수 업데이트
        } else {
            console.error('Invalid search response:', restaurantResponse);
        }
    } catch (error) {
        console.error('Error fetching search results:', error);
    }
}

// 페이지네이션 처리 함수
function handlePagination(event) {
    if (event.target.id === 'nextPage' && currentPage < totalPages - 1) {
        currentPage++;
    } else if (event.target.id === 'prevPage' && currentPage > 0) {
        currentPage--;
    }

    // 검색 모드에서 페이지네이션 처리
    if (isSearchMode) {
        const start = currentPage * pageSize;
        const end = start + pageSize;

        // 페이지 번호에 해당하는 검색 결과 렌더링
        renderRestaurantCards(searchResults.slice(start, end)); // 검색 결과에서 해당 페이지 범위만 렌더링
        fetchSearchResults($('#searchInput').val().trim(), currentPage); // 검색어와 페이지를 넘겨서 검색
    } else {
        fetchRestaurants($('#sortId').val(), currentPage);
    }
    updatePagination(totalPages); // 페이지네이션 업데이트
}

// 페이지네이션 업데이트
function updatePaginationForSearch(totalPages) {
    $('#totalPages').text(totalPages);
    $('#currentPage').text(currentPage + 1); // 페이지는 1부터 시작
    $('#prevPage').prop('disabled', currentPage === 0);
    $('#nextPage').prop('disabled', currentPage === totalPages - 1);
}

// DOM 이벤트 처리
$(document).ready(function () {
    $('#searchBtn').on('click', function (event) {
        event.preventDefault();
        const keyword = $('#searchInput').val().trim();
        if (keyword) {
            fetchSearchResults(keyword, 0); // 검색 시 첫 페이지로 초기화
        } else {
            alert("검색어를 입력하세요.");
        }
    });

    // 현재 URL의 쿼리 파라미터를 가져옴
    const urlParams = new URLSearchParams(window.location.search);

    // 'searchKeyword'라는 key가 있는지 확인하고, 값 가져오기
    const searchKeyword = urlParams.get('searchKeyword');

    if(searchKeyword) {
        console.log(`searchKeyword: ${searchKeyword}`);

        // #searchBtn 요소를 강제로 클릭하도록 트리거
        $('#searchBtn').click();
    }else{
        console.log("searchKeyword가 존재하지 않습니다.");
    }


    // 이전 페이지 버튼
    $('#prevPage').on('click', handlePagination); // 이전 페이지

    // 다음 페이지 버튼
    $('#nextPage').on('click', handlePagination); // 다음 페이지

    // 정렬 기준 변경
    $('#sortId').on('change', function () {
        const sortType = $(this).val();
        handleSortAfterSearch(sortType); // 정렬 후 처리
    });
});



console.log("search.js 끝");
