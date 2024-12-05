$(document).ready(function() {
    requestOwnerApi();

    // 비밀번호 변경 클릭 시
    document.getElementById("pass-card").onclick = function() {
        location.href = '/owner/password';
    };
});

// owner API
function requestOwnerApi() {

    const id = document.getElementById("ownerId").value;
    console.log(id);

    $.ajax({
        url: `/api/user/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            const oName = $('#name');
            const oEmail = $('#email');
            const oPhoneNumber = $('#phone');

            oName.val(response.data.name);
            oEmail.val(response.data.email);
            oPhoneNumber.val(response.data.phoneNumber);

            console.log('점주 set 완료');

        },
        error: function(xhr, status, error) {
            // 요청 실패 시 동작
            console.error('점주 set 실패:', error);
            alert('점주 set 중 오류가 발생했습니다.');
        }
    });
}