$(document).ready(function() {
    // 필드 입력 시 실시간 유효성 검사
    $('#name, #email, #pass1, #pass2').on('input', function() {
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
        $('#name, #email, #phone, #pass1, #pass2').each(function() {
            const field = $(this);
            if (!validateField(field)) {
                isValid = false;
                if (!firstInvalidField) firstInvalidField = field;
            }
        });

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
            console.log("폼 제출!");
            // 서버로 전송 로직 추가
        } else {
            firstInvalidField.focus();
        }
    });
});

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
        case 'pass1':
            isValid = validatePassword(value);
            toggleInvalidClass(field, isValid);
            $('#pwConfirm').text(isValid ? '' : "비밀번호는 숫자, 영문, 특수문자가 혼합된 8~16자여야 합니다.");
            break;
        case 'pass2':
            const password = $('#pass1').val();
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
