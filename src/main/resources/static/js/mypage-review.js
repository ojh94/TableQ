$(document).ready(function() {
    requestUserReviewApi();

    // 이전 페이지로 이동
    document.querySelector('.cancel').addEventListener('click', function() {
        window.history.back();
    });
});

function requestUserReviewApi() {

    const userId = document.getElementById("userId").value;

    $.ajax({
        url: `/api/review/user/${userId}`,
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
            }

            $('#my-review-empty').remove();

            response.data.sort((a, b) => new Date(b.createdAt) - new Date(a.createdAt)); // 유저별 리뷰 데이터 배열(최신순)

            response.data.forEach((review) => {

                // 별점별 별 아이콘을 추가할 문자열
                let starIcons = '';
                if (review.starRating === 5) {
                    starIcons = '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>';
                } else if (review.starRating === 4) {
                    starIcons = '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #e0e0e0 !important;"></i>';

                } else if (review.starRating === 3) {
                    starIcons = '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #e0e0e0 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #e0e0e0 !important;"></i>';

                } else if (review.starRating === 2) {
                    starIcons = '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #e0e0e0 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #e0e0e0 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #e0e0e0 !important;"></i>';

                } else if (review.starRating === 1) {
                    starIcons = '<i class="bi bi-star-fill" style="color: #FAC608 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #e0e0e0 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #e0e0e0 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #e0e0e0 !important;"></i>'
                              + '<i class="bi bi-star-fill" style="color: #e0e0e0 !important;"></i>';
                }

                // TIMESTAMP를 Date 객체로 변환 후 년, 월, 일 포맷으로 변환
                const createdAt = new Date(review.createdAt);
                const formattedDate =
                `${createdAt.getFullYear()}-${String(createdAt.getMonth() + 1).padStart(2, '0')}-${String(createdAt.getDate()).padStart(2, '0')}`;

                let reviewHtml =
                    `
                    <div class="card mb-2">
                        <div class="card-body">
                            <h6 class="fw-bold mt-3 restaurant-link" data-restaurant-id="${review.restaurant.id}">
                                ${review.restaurant.name} <i class="bi bi-chevron-right"></i>
                            </h6>
                            <p>${starIcons}<small>&nbsp;&nbsp;${formattedDate}</small></p>
                            <p>${review.content}</p>
                        </div>
                    </div>
                    `;

                // my-review 요소(내부) 시작 부분에 추가
                $('#my-review').prepend(reviewHtml);
            });

            $(document).on('click', '.restaurant-link', function () {
                const restaurantId = $(this).data('restaurant-id');
                if (restaurantId) {
                    location.href = '/restaurant/' + restaurantId;
                }
            });

            $('#my-review-page').show();

            console.log('내 리뷰 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('내 리뷰 set 실패:', error);
        alert('내 리뷰 set 중 오류가 발생했습니다.');
        }
    });
}