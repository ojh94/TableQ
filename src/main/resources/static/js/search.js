console.log("search.js 시작");

let isSearchMode = false; // 검색 모드 여부
let searchResults = []; // 키워드 검색 결과 저장
let filteredResults = []; // 정렬 후 필터링된 결과 저장


// 레스토랑 검색 함수
async function  fetchSearchResults(keyword) {
    // 검색어를 URL에 올바르게 인코딩하여 전달
    const apiUrl = `/api/restaurant/keyword/search?name=${encodeURIComponent(keyword)}`;
    console.log("검색 API 호출 URL:", apiUrl);

    try {
        const restaurantResponse = await $.ajax({
            url: apiUrl,
            type: 'GET',
        });

        if (restaurantResponse && restaurantResponse.data) {
            isSearchMode = true; // 검색 모드 활성화
            searchResults = restaurantResponse.data; // 검색 결과 저장
            currentPage = 0; // 페이지 초기화
            totalPages = Math.ceil(searchResults.length / pageSize); // 검색 결과 기반 페이지 수 계산
            console.log('검색 결과:', searchResults);

            // 검색 결과를 렌더링
            renderRestaurantCards(searchResults.slice(0, pageSize));
            updatePagination(totalPages); // 검색 결과에 맞는 페이지네이션 갱신

        } else {
            console.error('Invalid search response:', restaurantResponse);
        }
    } catch (error) {
        console.error('Error fetching search results:', error);
    }
}

function sortAndPaginateRestaurants(searchResults, sortType) {
    console.log('Sorting search results with sort:', sortType);

    // 정렬 기준 적용
    let sortedResults = [...searchResults]; // 검색 결과 복사
    if (sortType === 'mostVisit') {
        sortedResults.sort((a, b) => b.monsVisit - a.monsVisit);
    } else if (sortType === 'recommendationScore') {
        sortedResults.sort((a, b) => b.recommendationScore - a.recommendationScore);
    } else {
        sortedResults.sort((a, b) => a.id - b.id); // 기본 오름차순 정렬
    }

    // 페이지네이션 계산
    totalPages = Math.ceil(sortedResults.length / pageSize);
    currentPage = 0; // 초기 페이지 설정

    // 첫 페이지 렌더링
    renderRestaurantCards(sortedResults.slice(0, pageSize));
    updatePagination(totalPages);

    // 데이터 저장 (필요 시 다른 전역 변수에 저장)
    return sortedResults;
}

function handleSortAfterSearch(sortType) {
    if (searchResults.length > 0) {
        filteredResults = sortAndPaginateRestaurants(searchResults, sortType); // 검색 결과 정렬
    } else {
        console.error('No search results to sort');
    }
}

function handlePagination(event) {
    if (event.target.id === 'nextPage' && currentPage < totalPages - 1) {
        currentPage++;
    } else if (event.target.id === 'prevPage' && currentPage > 0) {
        currentPage--;
    }

    if (isSearchMode) {
        const start = currentPage * pageSize;
        const end = start + pageSize;
        renderRestaurantCards(searchResults.slice(start, end));
    } else {
        fetchRestaurants($('#sortId').val(), currentPage);
    }
    updatePagination(totalPages);
}

// DOM 이벤트 처리
$(document).ready(function () {
    console.log("DOM fully loaded.");

    // 검색 버튼 클릭
    $('#searchBtn').on('click', function () {
        event.preventDefault(); // 폼 기본 동작 방지

        const keyword = $('#searchInput').val().trim();
        console.log("입력된 검색어:", keyword);

        if (keyword) {
            fetchSearchResults(keyword);
        } else {
            alert("검색어를 입력하세요."); // 사용자 경고 메시지
            console.warn("검색어가 입력되지 않았습니다.");
        }
    });

   $('#sortId').on('change', function () {
       const sortType = $(this).val();
       if (isSearchMode) {
           searchResults.sort((a, b) => {
               if (sortType === 'mostVisit') {
                   return b.monsVisit - a.monsVisit;
               } else if (sortType === 'recommendationScore') {
                   return b.recommendationScore - a.recommendationScore;
               }
               return a.id - b.id; // 기본 id 오름차순
           });

           renderRestaurantCards(searchResults.slice(0, pageSize));
           updatePagination(Math.ceil(searchResults.length / pageSize));
       } else {
           fetchRestaurants(sortType, currentPage);
       }
   });

   $('#clearSearch').on('click', function () {
       isSearchMode = false;
       searchResults = [];
       fetchRestaurants('id', 0); // 초기화 후 기본 데이터를 다시 불러옴
   });

   // 페이지네이션 버튼 이벤트 리스너

   // 이전 페이지 버튼
   $('#prevPage').on('click', function () {
       if (currentPage > 0) {
           currentPage--;
           console.log("이전 페이지, 커런트 페이지", currentPage); // currentPage 값 확인
           const sortType = $('#sortId').val(); // 현재 드롭다운 값 읽기
           fetchRestaurants(sortType , currentPage);
       }
   });

   // 다음 페이지 버튼
   $('#nextPage').on('click', function () {
       if (currentPage < totalPages - 1) {
           currentPage++;
           console.log("Next Page, currentPage:", currentPage); // currentPage 값 확인
           const sortType = $('#sortId').val(); // 현재 드롭다운 값 읽기
           fetchRestaurants(sortType , currentPage);
       }
   });

   // 정렬 기준 변경
   $('#sortId').on('change', function () {
       console.log('Dropdown value:', $(this).val());
       const sortType = $(this).val(); // 드롭다운에서 선택된 정렬 기준
       currentPage = 0; // 정렬 변경 시 첫 페이지로 이동
       console.log('Selected sort value:', sortType);
       fetchRestaurants(sortType, currentPage);
   });
});

console.log("search.js 끝");
