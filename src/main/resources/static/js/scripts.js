$(document).ready(function() {
    if (window.location.pathname
    === '/restaurant/' + document.getElementById("restaurant-id").value) {
        requestRestaurantApi();

        // 점주 상세페이지 수정 버튼 클릭
        document.getElementById("modify-button").onclick = function() {
            const id = document.getElementById("restaurant-id").value;
            location.href = '/restaurant/modify/' + id;
        };
    }

    if (window.location.pathname
    === '/restaurant/modify/' + document.getElementById("restaurant-id").value) {
        requestRestaurantModifyApi();

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
                    <input type="file" style="display: none;" accept="image" onchange="updateImage(event, this)" />
                    <i class="bi bi-x-square px-4" style="font-size: 25px; cursor: pointer;" onclick="deleteMenuItem(this)"></i>
                </div>
            `;

            document.getElementById('addMenu').insertAdjacentHTML('beforebegin', menuItemHTML);
        });
    }
});

// review 탭으로 이동
document.getElementById("goToReview").addEventListener("click", function(event) {
    const reviewTab = new bootstrap.Tab(document.getElementById("review-tab"));
    reviewTab.show();
});

// 주소 복사 기능
function copyText() {
    // 복사할 텍스트 가져오기
    const text = document.getElementById("text-copy").textContent;

    // 클립보드에 복사
    navigator.clipboard.writeText(text)
        .then(() => {
            alert("주소가 복사되었습니다.");
        })
        .catch((err) => {
            console.error("복사 실패:", err);
        });
}

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

// 상세 메인 이미지 파일 불러오기
function updateActiveCarouselImage(event) {
    const file = event.target.files[0];
    const reader = new FileReader();

    reader.onload = function(e) {
        const activeImage = document.querySelector('#carouselExampleControls .carousel-item.active img');

        if (activeImage) {
            activeImage.src = e.target.result; // 이미지 src를 업데이트합니다.
        }
    };

    if (file) {
        reader.readAsDataURL(file); // 파일을 Data URL로 읽어 이미지 업데이트
    }

    // 같은 파일 선택 시에도 업데이트를 트리거하기 위해 input value를 초기화합니다.
    event.target.value = '';
}


// 점주 상세페이지 restaurant API
function requestRestaurantApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            const rName = $('body > div > div.container.mt-5 > div > div > article:nth-child(1) > header > h1');
            const rAddress = $('#home > p:nth-child(4)');
            const rIntroduction = $('#home > p:nth-child(11)');
            const rContactNumber = $('#home > table > tbody > tr:nth-child(4) > td:nth-child(2)');

            console.log(response);

            rName[0].textContent = response.data.name;
            rAddress[0].textContent = response.data.address;
            rIntroduction[0].textContent = response.data.introduction;
            rContactNumber[0].textContent = response.data.contactNumber;

            if (response.data.available == false) {
                $('#available').css("display" ,"none");
                $("body > div > div.container.mt-5 > div > div > article:nth-child(1) > table > tbody > tr:nth-child(1) > td:nth-child(2)")
                .text("현장대기 가능");
            } else {
                $("body > div > div.container.mt-5 > div > div > article:nth-child(1) > table > tbody > tr:nth-child(1) > td:nth-child(2)")
                .text("원격줄서기, 현장대기 모두 가능");
            }

            console.log('가게 set 완료');

        },
        error: function(xhr, status, error) {
            // 요청 실패 시 동작
            console.error('수정 실패:', error);
            alert('수정 중 오류가 발생했습니다.');
        }
    });
}

// 점주 상세 수정페이지 restaurant API
function requestRestaurantModifyApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            const rName = $('body > div > div.container.mt-5 > div > div > article:nth-child(1) > header > h1 > input');
            const rAddress = $('#home > div:nth-child(4) > input');
            const rIntroduction = $('#home > textarea');
            const rContactNumber = $('#times-modal > div > div > div.modal-body > div.card.mb-3 > div > table > tbody > tr:nth-child(1) > td:nth-child(2) > input');

            console.log(response);

            rName.val(response.data.name);
            rAddress.val(response.data.address);
            rIntroduction.val(response.data.introduction);
            rContactNumber.val(response.data.contactNumber);

            if (response.data.available == false) {
                $('#available').css("display" ,"none");
                $("body > div > div.container.mt-5 > div > div > article:nth-child(1) > table > tbody > tr:nth-child(1) > td:nth-child(2)")
                .text("현장대기 가능");
            } else {
                $("body > div > div.container.mt-5 > div > div > article:nth-child(1) > table > tbody > tr:nth-child(1) > td:nth-child(2)")
                .text("원격줄서기, 현장대기 모두 가능");
            }

            console.log('가게 set 완료');

        },
        error: function(xhr, status, error) {
            // 요청 실패 시 동작
            console.error('수정 실패:', error);
            alert('수정 중 오류가 발생했습니다.');
        }
    });
}

// restaurantImage API
function requestRestaurantImageApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/restaurant-image/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            const rImage = $('#carouselExampleControls > div > div.carousel-item.active > img');

            rImage.attr('src', response.data.path);

            console.log('가게 이미지 set 완료');

        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('수정 실패:', error);
        alert('수정 중 오류가 발생했습니다.');
        }
    });
}

// review API
function requestReviewApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/review/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작

            console.log('가게 이미지 set 완료');
        }
    });
}

/*

// 키워드 조회 (DB 속 키워드 개수 만큼 for문을 돌린다)
// 대상 요소를 선택
const targetElement = document.querySelector('#home > h4:nth-child(12)');

// 새 요소 생성
const newElement = document.createElement('a');
newElement.className = 'pick-outline mb-2';
newElement.textContent = response.data.name;

// 대상 요소 바로 아래에 새 요소 삽입
targetElement.insertAdjacentElement('afterend', newElement);

*/
