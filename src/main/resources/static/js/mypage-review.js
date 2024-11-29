$(document).ready(function() {
    requestUserReviewApi();
});

function requestUserReviewApi() {

    const userId = document.getElementById("userId").value;

    $.ajax({
        url: `/api/review/user/${userId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        data: {
               "page" : 0,
               "size" : 5
              },
        success: function(response) {
            // 요청 성공 시 동작
            if (response.data.length === 0) {
                return;
            }

            $('#empty-1').remove();

            response.data.forEach((review) => {
                let reviewHtml =
                    `
                    <div class="card">
                      <div class="card-body">
                        <p>${review.restaurant.id}</p>
                        <p>${review.starRating}</p>
                        <p>${review.content}</p>
                        <p>${review.createdAt}</p>
                      </div>
                    </div>
                    `;

                // my-review 요소(외부) 끝에 추가
                $('#my-review').append(reviewHtml);
            });

            console.log('내 리뷰 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('내 리뷰 set 실패:', error);
        alert('내 리뷰 set 중 오류가 발생했습니다.');
        }
    });
}