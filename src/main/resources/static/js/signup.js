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

    // 이메일 중복 체크 버튼 클릭 시
    $('#checkEmailBtn').on('click', function() {
        const email = $('#email').val();

        if (email === '') {
            alert('이메일을 입력해주세요.');
            return;
        }

        // 이메일 중복 체크 API 호출
        $.ajax({
            url: '/api/user/check-email',   // API URL
            type: 'POST',                   // POST 요청
            contentType: 'application/json', // 요청 헤더: JSON 형식
            data: JSON.stringify({
                'data' : { 'email' : email }
            }),  // JSON 데이터
            success: function(response) {
                if (response.data === true) {
                    alert('사용 가능한 이메일입니다.');
                    isEmailChecked = true;  // 중복 체크 완료
                } else if(response.data === false) {
                    alert('이미 사용 중인 이메일입니다.');
                    isEmailChecked = false;  // 중복 체크되지 않음
                    $('#email').next().next('.invalid-feedback').text('유효한 이메일을 입력해주세요.');
                } else {
                  alert('이메일 중복 확인에 실패했습니다.');
              }
           },
           error: function() {
               alert('네트워크 에러.');
           }
        });
    });

    // 휴대전화 중복 체크 버튼 클릭 시
    $('#checkPhoneBtn').on('click', function() {
        const phoneNumber = $('#phone').val();

        if (phoneNumber === '') {
            alert('전화번호를 입력해주세요.');
            return;
        }

        // 전화번호 중복 체크 API 호출
        $.ajax({
            url: '/api/user/check-phonenumber',   // API URL
            type: 'POST',                         // POST 요청
            contentType: 'application/json',      // 요청 헤더: JSON 형식
            data: JSON.stringify({
                'data' : { 'phoneNumber' : phoneNumber }
            }),  // JSON 데이터
            success: function(response) {
                if (response.data === true) {
                    alert('사용 가능한 전화번호입니다.');
                    isPhoneChecked = true;  // 중복 체크 완료
                } else if(response.data === false) {
                    alert('이미 사용 중인 전화번호입니다.');
                    isPhoneChecked = false;  // 중복 체크되지 않음
                    $('#phone').next().next('.invalid-feedback').text('유효한 전화번호를 입력해주세요.');
                } else {
                    alert('전화번호 중복 확인에 실패했습니다.');
                }
            },
            error: function() {
                alert('네트워크 에러.');
            }
        });
    });

    // 동의 버튼 클릭 시 체크박스 체크
    $('#agreeButton').on('click', function() {
        $('#agreement').prop('checked', true); // 체크박스 체크
        $('#agreement').removeClass('is-invalid'); // 유효성 검사 스타일 제거
        $('#agreement').next('label').find('.text-danger').remove(); // 피드백 메시지 제거
        $('#agreement').next('label').text('');
        $('#agreement').next('label').text('[필수] 개인정보 수집 및 이용에 동의합니다.');
    });

    // 거절 버튼 클릭 시 체크박스 해제
    $('#rejectButton').on('click', function() {
        $('#agreement').prop('checked', false); // 체크박스 해제
        $('#agreement').addClass('is-invalid'); // 유효성 검사 스타일 추가
        $('#agreement').next('label').find('.text-danger').remove(); // 피드백 메시지 제거
        $('#agreement').next('label').text('');
        $('#agreement').next('label').append('<span class="text-danger">[필수] 개인정보 수집 및 이용에 동의해야 합니다.</span>'); // 오류 메시지 추가
    });


    // 폼 제출 시 유효성 검사
    $('#signupForm').on('submit', function(event) {
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

        // 이메일 중복 체크 여부 확인
        if (!isEmailChecked) {
            isValid = false;
            $('#email').addClass('is-invalid');
            $('#email').next().next('.invalid-feedback').text('이메일 중복 체크를 해주세요.');
            if (!firstInvalidField) firstInvalidField = $('#email');
        }

        // 전화번호 중복 체크 여부 확인
        if (!isPhoneChecked) {
            isValid = false;
            $('#phone').addClass('is-invalid');
            $('#phone').next().next('.invalid-feedback').text('전화번호 중복 체크를 해주세요.');
            if (!firstInvalidField) firstInvalidField = $('#phone');
        }

        // 체크박스 유효성 검사
        const isCheckboxChecked = $('#agreement').prop('checked'); // 체크박스 ID 확인
        if (!isCheckboxChecked) {
            isValid = false;
            $('#agreement').addClass('is-invalid'); // 체크박스에 오류 스타일 추가
            // 오류 메시지 제거 후 추가
            $('#agreement').next('label').text(''); // 기존 메시지 제거
            $('#agreement').next('label').append('<span class="text-danger">[필수] 개인정보 수집 및 이용에 동의해야 합니다.</span>');
        } else {
            $('#agreement').removeClass('is-invalid'); // 체크박스 스타일 제거
            $('#agreement').next('label').find('.text-danger').remove(); // 피드백 메시지 제거
        }

        // 유효하지 않은 필드로 포커스 이동
        if (isValid) {
            requestUserCreateApi();
        } else {
            firstInvalidField.focus();
        }
    });
});


function requestUserCreateApi() {
    // 사용자 입력 값 가져오기
    const name = $('#name').val();
    const email = $('#email').val();
    const phone = $('#phone').val();
    const password = $('#password').val();

    // 서버로 보낼 데이터 객체 생성
    const createUser = {
        'data' : {
            'name': name,
            'email': email,
            'phoneNumber': phone,
            'password': password
        }
    };

    // AJAX로 사용자 생성 API 호출
    $.ajax({
        url: '/api/user',  // 사용자 생성 API URL
        type: 'POST',      // POST 요청
        contentType: 'application/json',  // JSON 형식으로 요청
        data: JSON.stringify(createUser),  // 사용자 데이터 JSON으로 변환하여 전송
        success: function(response) {
            debugger;
            if (response.data) {
                // 성공적으로 사용자 생성된 경우
                alert('회원가입이 완료되었습니다.');
                window.location.href = '/login';  // 로그인 페이지로 리디렉션 (원하는 페이지로 변경 가능)
            } else {
                // 서버 응답에서 오류가 있을 경우
                alert('회원가입에 실패했습니다. 다시 시도해 주세요.');
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
            $('#pwConfirm').text(isValid ? '' : "비밀번호는 숫자, 영문, 특수문자가 혼합된 8~16자여야 합니다.");
            break;
        case 'confirm-password':
            const password = $('#password').val();
            isValid = validateConfirmPassword(password, value);
            toggleInvalidClass(field, isValid);
            $('#pwConfirm').text(isValid ? (password === value ? "비밀번호가 일치합니다." : '') : "비밀번호가 일치하지 않습니다.");
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
