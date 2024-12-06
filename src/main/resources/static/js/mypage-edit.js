$(document).ready(function() {

    let isEmailChecked = false; // 이메일 중복 체크 여부
    let isPhoneChecked = false; // 전화번호 중복 체크 여부

    // 필드 입력 시 실시간 유효성 검사
    $('#name, #email, #password, #confirm-password').on('input', function() {
        const field = $(this);
        validateField(field);
    });

    // 전화번호 자동 포맷팅 및 유효성 검사
    $('#phone').on('input', function() {
        let phone = $(this).val();

        // 숫자만 추출
        phone = phone.replace(/[^0-9]/g, '');

        // 11자리 번호인 경우 (3-4-4)
        if (phone.length === 11) {
            phone = phone.replace(/(\d{3})(\d{4})(\d{4})/, '$1-$2-$3');
        }
        // 10자리 번호인 경우 (3-3-4)
        else if (phone.length === 10) {
            phone = phone.replace(/(\d{3})(\d{3})(\d{4})/, '$1-$2-$3');
        }

        // 포맷팅된 전화번호를 입력 필드에 업데이트
        $(this).val(phone);

        // 전화번호 유효성 검사
        validateField($(this));
    });

    // 폼 제출 시 유효성 검사
    $('#editForm').on('submit', function(event) {
        event.preventDefault();  // 기본 제출 동작 방지

        let isValid = true; // 폼의 유효성 여부
        let firstInvalidField = null; // 첫 번째 유효하지 않은 필드

        // 모든 필드 유효성 검사
        $('#name, #email, #phone, #password, #confirm-password').each(function() {
            const field = $(this);
            if (!validateField(field)) {
                isValid = false;
                if (!firstInvalidField) firstInvalidField = field;
            }
        });

        // 유효하지 않은 필드로 포커스 이동
        if (isValid) {
            requestUserCreateApi();
        } else {
            firstInvalidField.focus();
        }
    });
});


function requestUserCreateApi() {
    const userId = Number(document.getElementById('userId').value);

    // 사용자 입력 값 가져오기
    const nickname = $('#nickname').val();
    const address = $('#address').val();
    const password = $('#password').val();

    // 서버로 보낼 데이터 객체 생성
    const createUser = {
        'data' : {
            'nickname': nickname,
            'address': address,
            'password': password
        }
    };

    // AJAX로 사용자 생성 API 호출
    $.ajax({
        url: `/api/user/${userId}`,  // 사용자 생성 API URL
        type: 'PUT',      // POST 요청
        contentType: 'application/json',  // JSON 형식으로 요청
        data: JSON.stringify(createUser),  // 사용자 데이터 JSON으로 변환하여 전송
        success: function(response) {
            debugger;
            if (response.data) {
                // 성공적으로 사용자 생성된 경우
                alert('회원정보 수정이 완료되었습니다.');
                window.location.href = '/login';  // 로그인 페이지로 리디렉션 (원하는 페이지로 변경 가능)
            } else {
                // 서버 응답에서 오류가 있을 경우
                alert('회원정보 수정에 실패했습니다. 다시 시도해 주세요.');
                console.log(response.description)
            }
        },
        error: function() {
            // 네트워크 에러 발생 시 처리
            alert('네트워크 오류가 발생했습니다. 다시 시도해 주세요.');
        }
    });
}


// 필드 유효성 검사
function validateField(field) {
    const value = field.val();
    let isValid = true;

    switch(field.attr('id')) {
        case 'name':
            isValid = validateName(value);
            toggleInvalidClass(field, isValid);
            break;
        case 'email':
            isValid = validateEmail(value);
            toggleInvalidClass(field, isValid);
            break;
        case 'phone':
            const phoneValue = value.replace(/[^0-9]/g, ''); // 하이픈 제거
            isValid = validatePhone(phoneValue);
            toggleInvalidClass(field, isValid);
            break;
        case 'password':
            isValid = validatePassword(value);
            toggleInvalidClass(field, isValid);
            $('#password').next().text(isValid ? '' : "비밀번호는 숫자, 영문, 특수문자가 혼합된 8~16자여야 합니다.");
            break;
        case 'confirm-password':
            const password = $('#password').val();
            isValid = validateConfirmPassword(password, value);
            toggleInvalidClass(field, isValid);
            $('#confirm-password').next().text(isValid ? (password === value ? "비밀번호가 일치합니다." : '') : "비밀번호가 일치하지 않습니다.");
            break;
    }

    return isValid;
}

// 유효성 검사 후 스타일 업데이트
function toggleInvalidClass(field, isValid) {
    if (isValid) {
        field.removeClass('is-invalid');
    } else {
        field.addClass('is-invalid');
    }
}

// 이름 유효성 검사
function validateName(name) {
    const namePattern = /^[가-힣]{2,}$/;  // 두 글자 이상의 한글 이름만 허용
    return namePattern.test(name);
}

// 이메일 유효성 검사
function validateEmail(email) {
    return validator.isEmail(email);
}

// 전화번호 유효성 검사 (숫자만, 한국 번호 형식)
function validatePhone(phoneNumber) {
    return validator.isMobilePhone('+82' + phoneNumber, 'ko-KR', { strictMode: true });
}

// 비밀번호 유효성 검사 (길이 확인 및 패턴 검사)
function validatePassword(password) {
    const passwordPattern = /^(?=.*[a-zA-Z])(?=.*\d)(?=.*[\W_]).{8,16}$/; // 영문, 숫자, 특수문자 포함 8~16자
    return passwordPattern.test(password);
}

// 비밀번호 확인 유효성 검사
function validateConfirmPassword(password, confirmPassword) {
    return password === confirmPassword;
}