$(document).ready(function() {

    // 이전 페이지로 이동
    document.querySelector('.cancel').addEventListener('click', function() {
        window.history.back();
    });
}

function requestUserHistoryApi() {
    const userId = document.getElementById("userId").value;

    $.ajax({
        url: `/api/reservation/user/${userId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        data: {
               "page" : 0,
               "size" : 3
              },
        success: function(response) {
            // 요청 성공 시 동작
            if (response.data.length === 0) {
                return;

            } else if (response.data.filter(item => item.isEntered === null).length > 0) {
                const expected = response.data.filter(item => item.isEntered === null);
                /*expected.forEach((plans) => {

                });*/

            } else if (response.data.filter(item => item.isEntered === true).length > 0) {
                const complete = response.data.filter(item => item.isEntered === true);

            } else if (response.data.filter(item => item.isEntered === false).length > 0) {
                const cancellation = response.data.filter(item => item.isEntered === false);
            }
        },
         error: function(xhr, status, error) {
         // 요청 실패 시 동작
         console.error('내 리뷰 set 실패:', error);
         alert('내 리뷰 set 중 오류가 발생했습니다.');
         }
    });
}