$(document).ready(function() {
    requestWaitingApi();
    requestWaitingNumApi();

    // 페이지를 이전 페이지로 이동
    document.getElementById('cancel').addEventListener('click', function() {
        window.history.back();
    });

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

    // 신청하기 버튼 클릭 시
    document.getElementById("apply").addEventListener("click", function() {
        if (confirm("신청하시겠습니까?")) {
            document.getElementById("apply-form").submit();
        }
    });
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

// 가게 별 이름 조회
function requestWaitingApi() {

    const id = document.getElementById("waiting-id").value;

    $.ajax({
        url: `/api/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            $('#restaurant-name')[0].textContent = response.data.name;

            console.log('가게 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('가게 set 실패:', error);
        alert('가게 set 중 오류가 발생했습니다.');
        }
    });
}

// 가게 별 대기 순서 조회
function requestWaitingNumApi() {

    const restaurantId = document.getElementById("waiting-id").value;

    $.ajax({
        url: `/api/reservation/restaurant/queue/${restaurantId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            $('#team')[0].textContent = response.data + '팀';

            console.log('대기 순서 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('대기 순서 set 실패:', error);
        alert('대기 순서 set 중 오류가 발생했습니다.');
        }
    });
}
