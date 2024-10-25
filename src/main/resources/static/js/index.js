

 // 검색 기능
  searchForm.addEventListener('submit', (e) => {
      e.preventDefault();
      console.log('Searching for:', searchInput.value);
      // 여기에 실제 검색 로직을 구현
  });

  // 정렬 기능
  sortSelect.addEventListener('change', () => {
      console.log('Sorting by:', sortSelect.value);
      // 여기에 실제 정렬 로직을 구현
  });


  // 찜하기 기능
  function toggleFavorite(restaurantId) {
    console.log('Toggled favorite for restaurant:', restaurantId);
    // 여기에 실제 찜하기 로직을 구현
  }

// '검색' 버튼 클릭 시 이벤트 핸들러
document.querySelector('.toolbar-item[href="#searchInput"]').addEventListener('click', function (e) {
    e.preventDefault();

    // 페이지를 맨 위로 스크롤
    window.scrollTo({
        top: 0,
        behavior: 'smooth'
    });

    // 포커스를 검색 입력란에 맞춤
    document.querySelector('#searchInput').focus();
});