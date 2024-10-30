$(document).ready(function() {
    // review 탭으로 이동
    document.getElementById("goToReview").addEventListener("click", function(event) {
        const reviewTab = new bootstrap.Tab(document.getElementById("review-tab"));
        reviewTab.show();
    });

    requestRestaurantApi();
});

// 페이지를 이전 페이지로 이동
document.getElementById('cancel').addEventListener('click', function() {
    window.history.back();
});

// 새로운 메뉴 추가
document.getElementById('addMenuButton').addEventListener('click', function() {
    const menuItemHTML = `
        <div class="menu-item mb-4" style="display: flex; align-items: center;">
            <div class="item-info">
                <div>
                    <h4><input value="" placeholder="메뉴명"></h4>
                    <h5 class="price" style="margin-bottom: 0px;"><input value="" placeholder="가격"></h5>
                </div>
            </div>
            <img class="photo menu-img-modify" src="/img/test-img/텐동.jpg" alt="#" onclick="triggerFileInput(this)"/>
            <input type="file" style="display: none;" accept="image/*" onchange="updateImage(event, this)" />
            <i class="bi bi-x-square px-4" style="font-size: 25px; cursor: pointer;" onclick="deleteMenuItem(this)"></i>
        </div>
    `;

    document.getElementById('addMenu').insertAdjacentHTML('beforebegin', menuItemHTML);
});

// 메뉴 단일 삭제
function deleteMenuItem(iconElement) {
    const menuItem = iconElement.closest('.menu-item');
    if (menuItem) {
        menuItem.remove();
    }
}

// 메뉴 이미지 파일 불러오기
function triggerFileInput(imgElement) {
    const fileInput = imgElement.nextElementSibling; // 해당 이미지 다음 형제 요소인 input을 선택
    fileInput.click(); // 클릭 이벤트 트리거
}

function updateImage(event, fileInputElement) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = function(e) {
        const imgElement = fileInputElement.previousElementSibling; // 해당 input의 이전 형제 요소인 img를 선택
        imgElement.src = e.target.result; // 새로운 이미지로 src 변경
    };

    if (file) {
        reader.readAsDataURL(file); // 파일 읽기
    }
}


function requestRestaurantApi() {

    const id = 1; // 추후 수정 필요

    $.ajax({
        url: `/api/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            const rName = $('body > div > div.container.mt-5 > div > div > article:nth-child(1) > header > h1');
            const rAddress = $('#home > p:nth-child(4)');


            console.log(response);

            rName[0].textContent = response.data.name;
            rAddress[0].textContent = response.data.address;

            console.log('가게 name set 완료');
            console.log('가게 address set 완료');

        },
        error: function(xhr, status, error) {
            // 요청 실패 시 동작
            console.error('수정 실패:', error);
            alert('수정 중 오류가 발생했습니다.');
        }
    });
}