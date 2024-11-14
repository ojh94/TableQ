// 성인 및 아동의 버튼 클릭 이벤트
document.getElementById('adult-plus').addEventListener('click', function() {
    updateCount('adult', 1);
});

document.getElementById('adult-minus').addEventListener('click', function() {
    updateCount('adult', -1);
});

document.getElementById('child-plus').addEventListener('click', function() {
    updateCount('child', 1);
});

document.getElementById('child-minus').addEventListener('click', function() {
    updateCount('child', -1);
});

// 성인, 아동 카운트 업데이트 함수
function updateCount(type, change) {
    const countElement = document.getElementById(type + '-count');
    let currentCount = parseInt(countElement.textContent);

    // 성인과 아동 모두 0보다 적을 수 없도록
    if (currentCount + change >= 0) {
        currentCount += change;
        countElement.textContent = currentCount;
    }

    updateTotalCount();
}

// 총 인원 업데이트 함수
function updateTotalCount() {
    const adultCount = parseInt(document.getElementById('adult-count').textContent);
    const childCount = parseInt(document.getElementById('child-count').textContent);

    const totalCount = adultCount + childCount;
    document.getElementById('total-count').textContent = totalCount + '명';
}
