$(document).ready(function() {
    $('#signupForm').on('submit', function(event) {
        event.preventDefault(); // 폼의 기본 제출 동작을 막음

        // ID 값을 가져오기
        const userId = $('#id').text().trim(); // ID를 가져와서 공백 제거

        // 폼 데이터 수집
        const formData = {
            "data": {
                "nickname" : $('#nickname').val(),
                "address" : $('#address').val(),
                "password" : $('#password').val()
            }
            // 필요한 추가 필드가 있다면 여기에 추가
        };

        // AJAX 요청 보내기
        $.ajax({
            url: `/api/user/${userId}`,
            type: 'PUT', // 필요한 HTTP 메서드로 변경 (PUT 또는 PATCH 등도 가능)
            contentType: 'application/json', // JSON 형식으로 데이터 전송
            data: JSON.stringify(formData), // 데이터를 JSON 문자열로 변환
            success: function(response) {
                // 요청 성공 시 동작
                console.log(response);
                alert('정보가 성공적으로 수정되었습니다.');
                // 필요한 경우 페이지를 새로 고침
                location.reload();

            },
            error: function(xhr, status, error) {
                // 요청 실패 시 동작
                console.error('수정 실패:', error);
                alert('수정 중 오류가 발생했습니다.');
            }
        });
    });
});

