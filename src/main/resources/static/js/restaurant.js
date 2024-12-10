$(document).ready(function() {

    requestOwnerNavApi();

    if (window.location.pathname
    === '/restaurant/' + document.getElementById("restaurant-id").value) {
        requestRestaurantApi(false);
        requestRestaurantImageApi();
        requestReviewApi();
        requestMenuApi();
        requestOpeningHourApi();
        requestBreakHourApi();
        requestKeywordApi();
        requestAmenityApi();
        /*requestReviewPossibleApi();*/

        // 원격줄서기 버튼 클릭 시
        document.getElementById('apply').onclick = function() {
            const restaurantId = document.getElementById('restaurant-id').value;
            location.href = '/restaurant/reservation/apply/' + restaurantId;
        };

        // 리뷰탭 속 별점 주기
        $('.star_rating > .star').click(function() {
          $(this).parent().children('span').removeClass('on');
          $(this).addClass('on').prevAll('span').addClass('on');
        });
    }

    if (window.location.pathname
    === '/restaurant/modify/' + document.getElementById("restaurant-id").value) {
        requestRestaurantApi(true);
        requestRestaurantImageApi();
        requestReviewApi();
        requestMenuModifyApi();
        requestOpeningHourModifyApi();
        requestBreakHourModifyApi();
        requestKeywordModifyApi();
        requestAmenityModifyApi();

        // 새로운 메뉴 추가
        document.getElementById('addMenuButton').addEventListener('click', function() {
            const menuItemHTML = `
                <div class="menu-item mb-4" style="display: flex; align-items: center;">
                    <div class="item-info">
                        <div>
                            <h4 style="display: flex; align-items: center;">
                                <input value="" placeholder="메뉴명">&nbsp;
                                <span class="badge" style="background-color: rgba(237, 125, 49, 1); font-size: 13px; display: flex; align-items: center;">추천
                                    <input type="checkbox" style="margin-left: 6px;">
                                </span>
                            </h4>
                            <h5 class="price" style="margin-bottom: 0px;"><input value="" placeholder="가격"></h5>
                        </div>
                    </div>
                    <img class="photo menu-img-modify" src="" alt="메뉴" onerror="this.src='https://placehold.jp/150x150.png'" onclick="triggerFileInput(this)"/>
                    <input type="file" style="display: none;" accept="image" onchange="updateImage(event, this)" />
                    <i class="bi bi-x-square px-4" style="font-size: 25px; cursor: pointer;" onclick="deleteMenuItem(this)"></i>
                </div>
            `;
            document.getElementById('menu-list').insertAdjacentHTML('beforeend', menuItemHTML);
            // document.getElementById('addMenu').insertAdjacentHTML('beforebegin', menuItemHTML);
        });

        // keyword 취소 버튼 클릭 시 초기 상태로 복원
        document.getElementById('keyword-cancel').addEventListener('click', function() {
            document.querySelectorAll('#keyword input[type="checkbox"]').forEach(checkbox => {
                checkbox.checked = checkbox.getAttribute('data-initial-checked') === 'true';
            });
        });

        // amenity 취소 버튼 클릭 시 초기 상태로 복원
        document.getElementById('amenity-cancel').addEventListener('click', function() {
            document.querySelectorAll('#amenity input[type="checkbox"]').forEach(checkbox => {
                checkbox.checked = checkbox.getAttribute('data-initial-checked') === 'true';
            });
        });

        // keyword 선택완료 버튼 클릭 시
        $('#keyword-set').click(function() {
            $('#keywords-modal').modal('hide');
        });

        // amenity 선택완료 버튼 클릭 시
        $('#amenity-set').click(function() {
            $('#amenities-modal').modal('hide');
        });

        // 메뉴 수정 요청
        $('#modify').on('click', function() {
            if(confirm("수정이 완료된 후 되돌릴 수 없습니다. 계속 진행하시겠습니까?")) {
                requestRestaurantUpdateAllApi();
                /*requestRestaurantUpdateApi();
                requestKeywordUpdateApi();
                requestAmenityUpdateApi();*/
            }
        });

        // 예약현황 버튼 클릭 시
        $('#reservations').click(function() {
            const restaurantId = document.getElementById("restaurant-id").value;
            if (restaurantId) {
                location.href = '/owner/reservation/' + restaurantId;
            }
        });
    }

    // 리뷰 탭으로 이동
    document.getElementById("goToReview").addEventListener("click", function(event) {
        const reviewTab = new bootstrap.Tab(document.getElementById("review-tab"));
        reviewTab.show();
    });
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

// 메뉴 삭제
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

// 상세 수정 페이지의 메인 이미지 파일 불러오기
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

// restaurant 조회 API
function requestRestaurantApi(isModifyMode) {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            if (response.resultCode === "ERROR") {
                alert('유효하지 않은 페이지입니다. 이전 페이지로 이동합니다.');
                window.history.back(); // 이전 페이지로 이동
                return; // 이후 코드 실행 방지
            }

            // 요청 성공 시 동작
            const rName = $('#restaurant-name');
            const rAddress = $('#restaurant-address');
            const rInformation = $('#information');
            const rContactNumber = $('#restaurant-number');

            if (response.data.isAvailable === false) {
                $('#available').css("display" ,"none");
                $("#application")[0].textContent = "현장대기 가능";

            } else if (response.data.isAvailable === true) {
                $("#application")[0].textContent = "원격줄서기, 현장대기 모두 가능";
            }

            if (isModifyMode) {
                rName.val(response.data.name);
                rAddress.val(response.data.address);
                rInformation.val(response.data.information);
                rContactNumber.val(response.data.contactNumber);

            } else {
                rName[0].textContent = response.data.name;
                rAddress[0].textContent = response.data.address;
                rContactNumber[0].textContent = response.data.contactNumber;

                if(response.data.information === undefined) {
                    let informationHtml =
                        `
                        <div style="text-align: center;">
                            <p style="color: #696969;">설정된 매장소개 정보가 없습니다.</p>
                            <a class="pick-outline" type="button">매장에 등록 요청하기</a>
                        </div>
                        `;
                    // information 요소(외부) 끝에 추가
                    $('#information').after(informationHtml);

                } else {
                    rInformation[0].textContent = response.data.information;
                }
            }

            console.log('가게 set 완료');
        },
        error: function(xhr, status, error) {
            // 요청 실패 시 동작
            console.error('가게 set 실패:', error);
            alert('가게 set 중 오류가 발생했습니다.');
        }
    });
}

// restaurantImage API
function requestRestaurantImageApi() {

    const restaurantId = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/restaurant-image/restaurant/${restaurantId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            const restaurantImages = response.data;

            if (restaurantImages.length > 0) {
                $('#restaurant-images').empty();

                restaurantImages.forEach((image) => {
                    let restaurantImageHtml =
                        `
                        <div class="carousel-item">
                            <img src="${image.fileUrl}" class="d-block w-100 main-img" alt="점포이미지">
                        </div>
                        `;
                    // information 요소(내부) 끝에 추가
                    $('#restaurant-images').append(restaurantImageHtml);
                });

                $('#restaurant-images .carousel-item:first').addClass('active');

                console.log('가게 이미지 set 완료');
            }
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('가게 이미지 set 실패:', error);
        alert('가게 이미지 set 중 오류가 발생했습니다.');
        }
    });
}

// review API
function requestReviewApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/review/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        data: {
               "page" : 0,
               "size" : 10
              },
        success: function(response) {
            // 요청 성공 시 동작
            const reviews = response.data; // 리뷰 데이터 배열

            $('#reviews-number')[0].textContent = '리뷰 ' + reviews.length + '건';
            $('#goToReview')[0].textContent = reviews.length + '개의 리뷰';

            // 별점 평균 계산 및 출력
            const totalStars = reviews.reduce((sum, review) => sum + review.starRating, 0);
            const averageStars = (reviews.length > 0) ? (totalStars / reviews.length).toFixed(1) : 0;
            $('#average-1').html('&nbsp;' + averageStars +'&nbsp;/&nbsp;');
            $('#average-2').html('&nbsp;' + averageStars);

            // 별점별 리뷰 카운트 및 출력
            const fiveStarCount = reviews.filter(review => review.starRating === 5).length;
            const fourStarCount = reviews.filter(review => review.starRating === 4).length;
            const threeStarCount = reviews.filter(review => review.starRating === 3).length;
            const twoStarCount = reviews.filter(review => review.starRating === 2).length;
            const oneStarCount = reviews.filter(review => review.starRating === 1).length;

            $('#5-point').html(fiveStarCount + '개');
            $('#4-point').html(fourStarCount + '개');
            $('#3-point').html(threeStarCount + '개');
            $('#2-point').html(twoStarCount + '개');
            $('#1-point').html(oneStarCount + '개');

            // 별점별 bar 길이 설정
            fiveBar = (fiveStarCount / reviews.length) * 100;
            fourBar = (fourStarCount / reviews.length) * 100;
            threeBar = (threeStarCount / reviews.length) * 100;
            twoBar = (twoStarCount / reviews.length) * 100;
            oneBar = (oneStarCount / reviews.length) * 100;

            $('#5-bar').css('width', fiveBar + '%');
            $('#4-bar').css('width', fourBar + '%');
            $('#3-bar').css('width', threeBar + '%');
            $('#2-bar').css('width', twoBar + '%');
            $('#1-bar').css('width', oneBar + '%');

            // 각 리뷰를 form 아래에 반복해서 추가
            reviews.forEach((review) => {

                // TIMESTAMP를 Date 객체로 변환 후 년, 월, 일 포맷으로 변환
                const createdAt = new Date(review.createdAt);
                const formattedDate =
                `${createdAt.getFullYear()}-${String(createdAt.getMonth() + 1).padStart(2, '0')}-${String(createdAt.getDate()).padStart(2, '0')}`;

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

                const reviewHtml = `
                    <hr style="margin: 1.5rem 0;">
                    <div class="d-flex">
                        <div class="ms-3">
                            <div>${starIcons}<small>&nbsp;&nbsp;${formattedDate}</small></div>
                            <div class="fw-bold mt-2 mb-3">${review.user.email}</div>
                            <p style="margin: 0;">${review.content}</p>
                        </div>
                    </div>
                `;

                // review-form 요소(외부) 바로 뒤에 추가
                $('#review-form').after(reviewHtml);
            });

            console.log('리뷰 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('리뷰 set 실패:', error);
        alert('리뷰 set 중 오류가 발생했습니다.');
        }
    });
}

// 상세 페이지 속 '작성 가능 리뷰' 화면 출력
/*function requestReviewPossibleApi() {
    const restaurantId = document.getElementById("restaurant-id").value;
    const userId = document.getElementById("userId").value;

    let reservationCount;
    let reviewCount;

    $.ajax({
            url: `/api/reservation/${userId}/${restaurantId}`,
            type: 'GET', // 필요한 HTTP 메서드로 변경
            contentType: 'application/json', // JSON 형식으로 데이터 전송
            async: false,
            success: function(response) {
                // 요청 성공 시 동작
                if("OK" === response.resultCode) {
                     document.getElementById('review-form').setAttribute('data-id', response.data[0].id); // 작성 가능한 예약 중 첫번째
                }
            },
            error: function(xhr, status, error) {
                // 요청 실패 시 동작
                console.error('작성 가능한 예약 조회 오류:', error);
                alert('작성 가능한 예약 조회 오류');
            }
    });

    $.ajax({
        url: `/api/reservation/count/${userId}/${restaurantId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        async: false,
        success: function(response) {
            // 요청 성공 시 동작
            reservationCount = response.data;
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('3일간 방문한 reservation 조회 실패:', error);
        alert('3일간 방문한 reservation 조회 오류가 발생했습니다.');
        }
    });

    $.ajax({
        url: `/api/review/count/${userId}/${restaurantId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        async: false,
        success: function(response) {
            // 요청 성공 시 동작
            reviewCount = response.data;
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('3일간 작성한 review 조회 실패:', error);
        alert('3일간 작성한 review 조회 오류가 발생했습니다.');
        }
    });

    if (reservationCount - reviewCount > 0) {
        let reviewPossibleStarHtml =
            `
            <hr/>
            <div class="star_rating ms-1 mb-2">
                <span class="star on" value="1"> </span>
                <span class="star" value="2"> </span>
                <span class="star" value="3"> </span>
                <span class="star" value="4"> </span>
                <span class="star" value="5"> </span>
            </div>
            `;

        let reviewPossibleTextareaHtml =
            `
            <textarea id="review-textarea" class="form-control" rows="4" placeholder="솔직한 평가를 남겨주세요!"></textarea>
            `;

        let reviewPossibleButtonHtml =
            `
            <div class="d-flex justify-content-center">
                <button id="review-summit" type="button" class="btn btn-secondary mt-3">작성완료</button>
            </div>
            `;

        // reviews-number 요소(외부) 끝에 추가
        $('#reviews-number').after(reviewPossibleStarHtml);
        $('#review-form').append(reviewPossibleTextareaHtml);
        $('#review-textarea').after(reviewPossibleButtonHtml);

        requestReviewCreateAPI();
    }
}*/

// 리뷰 작성완료 버튼 클릭 시 create
/*function requestReviewCreateAPI() {
    document.getElementById('review-summit').addEventListener('click', function(event) {

        const starCount = document.querySelectorAll('.star_rating .star.on').length;
        const restaurantId = document.getElementById("restaurant-id").value;
        const userId = document.getElementById("userId").value;

        if($('#review-textarea').val() === "") {
            alert('내용을 입력해주세요.');
            return;
        }

        const formData = {
            "data": {
                "content" : $('#review-textarea').val(),
                "starRating" : starCount,
                "restaurant" : {
                    "id" : restaurantId
                },
                "user" : {
                    "id" : userId
                },
                "reservation": {
                    "id" : Number(document.getElementById('review-form').dataset.id)
                }
            }
        };

        // AJAX 요청 보내기
        $.ajax({
            url: `/api/review`,
            type: 'POST', // 필요한 HTTP 메서드로 변경 (PUT 또는 PATCH 등도 가능)
            contentType: 'application/json', // JSON 형식으로 데이터 전송
            data: JSON.stringify(formData), // 데이터를 JSON 문자열로 변환
            success: function(response) {
                // 요청 성공 시 동작
                debugger;
                if("OK" === response.resultCode()) {
                    alert('리뷰 등록이 완료되었습니다.');
                    location.reload();
                } else {
                    alert('리뷰 등록에 실패했습니다.');
                }
            },
            error: function(xhr, status, error) {
                // 요청 실패 시 동작
                console.error('생성 실패:', error);
                alert('생성 중 오류가 발생했습니다.');
            }
        });
    });
}*/

// 점주 상세 페이지 menu API
function requestMenuApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/menu-item/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            const menus = (response.data).sort((a, b) => a.id - b.id); // 메뉴 데이터 배열(오름차순)

            menus.forEach((menu) => {

                let menuHtml =
                    `
                    <div class="menu-item mb-4" style="display: flex; align-items: center;">
                        <div class="item-info">
                            <div style="display: flex; align-items: center; margin-bottom: 12px;">
                               <h4 class="m-0">${menu.name}</h4>
                    `;

                if (menu.recommendation) {
                    menuHtml +=
                        `
                                    <span class="badge ms-2" style="background-color: rgba(237, 125, 49, 1); font-size: 13px;">추천</span>
                        `;
                }

                menuHtml +=
                    `
                            </div>
                            <h5 class="price" style="margin-bottom: 0px;">${menu.price}원</h5>
                        </div>
                        <img class="photo menu-img" src="
                    `;

                if (menu.fileUrl && menu.fileUrl !== "") {
                    menuHtml += `${menu.fileUrl}`;
                }

                menuHtml +=
                    `" alt="메뉴" onerror="this.src='https://placehold.jp/150x150.png'"
                    />
                    </div>
                    `;

                // menu-list 요소(내부) 끝에 추가
                $('#menu-list').append(menuHtml);
            });

            console.log('메뉴 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('메뉴 set 실패:', error);
        alert('메뉴 set 중 오류가 발생했습니다.');
        }
    });
}

// 상세 수정 페이지 menu API
function requestMenuModifyApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/menu-item/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            const menus = (response.data).sort((a, b) => a.id - b.id); // 메뉴 데이터 배열(오름차순)

            menus.forEach((menu) => {

                let menuModifyHtml =
                    `
                    <div class="menu-item mb-4" style="display: flex; align-items: center;">
                        <div class="item-info">
                            <h4 style="display: flex; align-items: center;">
                                <input value="${menu.name}" placeholder="메뉴명">&nbsp;
                    `;

                if (menu.recommendation) {
                    menuModifyHtml +=
                        `
                                    <span class="badge" style="background-color: rgba(237, 125, 49, 1); font-size: 13px; display: flex; align-items: center;">추천
                                        <input type="checkbox" style="margin-left: 6px;" checked>
                                    </span>
                        `;
                } else {
                    menuModifyHtml +=
                        `
                                    <span class="badge" style="background-color: rgba(237, 125, 49, 1); font-size: 13px; display: flex; align-items: center;">추천
                                        <input type="checkbox" style="margin-left: 6px;">
                                    </span>
                        `;
                }

                menuModifyHtml +=
                    `
                            </h4>
                            <h5 class="price" style="margin-bottom: 0px;"><input value="${menu.price}원" placeholder="가격"></h5>
                        </div>
                        <img class="photo menu-img-modify" src="
                    `;

                if (menu.fileUrl && menu.fileUrl !== "") {
                    menuModifyHtml += `${menu.fileUrl}`;
                }

                menuModifyHtml +=
                    `" alt="메뉴" onerror="this.src='https://placehold.jp/150x150.png'" onclick="triggerFileInput(this)"
                    />
                         <input type="file" style="display: none;" accept="image/*" onchange="updateImage(event, this)" />
                         <i class="bi bi-x-square px-4" style="font-size: 25px; cursor: pointer;" onclick="deleteMenuItem(this)"></i>
                     </div>
                    `;

                // menu-list 요소(내부) 끝에 추가
                $('#menu-list').append(menuModifyHtml);
            });

            console.log('메뉴 수정 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('메뉴 수정 set 실패:', error);
        alert('메뉴 수정 set 중 오류가 발생했습니다.');
        }
    });
}

// 상세 페이지 opening-hour API
function requestOpeningHourApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/openinghour/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        data: {
               "page" : 0,
               "size" : 10
              },
        success: function(response) {
            // 요청 성공 시 동작
            const opens = response.data; // 운영시간 데이터 배열

            const today = new Date();
            const currentTime
                = today.getHours().toString().padStart(2, '0') + ":" + today.getMinutes().toString().padStart(2, '0');
            let formattedOpenTime;
            let formattedCloseTime;

            opens.forEach((open) => {
                switch (open.dayOfWeek) {
                    case 'MONDAY' :
                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        $('#monday-open')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;

                        if(today.getDay() === 1) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-open-3').html('오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-week')[0].textContent = '오늘 (월요일)';
                            $('#today-open-4')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;
                        }
                        break;
                    case 'TUESDAY' :
                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        $('#tuesday-open')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;

                        if(today.getDay() === 2) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-open-3').html('오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-week')[0].textContent = '오늘 (화요일)';
                            $('#today-open-4')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;
                        }
                        break;
                    case 'WEDNESDAY' :
                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        $('#wednesday-open')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;

                        if(today.getDay() === 3) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-open-3').html('오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-week')[0].textContent = '오늘 (수요일)';
                            $('#today-open-4')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;
                        }
                        break;
                    case 'THURSDAY' :
                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        $('#thursday-open')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;

                        if(today.getDay() === 4) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-open-3').html('오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-week')[0].textContent = '오늘 (목요일)';
                            $('#today-open-4')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;
                        }
                        break;
                    case 'FRIDAY' :
                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        $('#friday-open')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;

                        if(today.getDay() === 5) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-open-3').html('오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-week')[0].textContent = '오늘 (금요일)';
                            $('#today-open-4')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;
                        }
                        break;
                    case 'SATURDAY' :
                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        $('#saturday-open')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;

                        if(today.getDay() === 6) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-open-3').html('오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-week')[0].textContent = '오늘 (토요일)';
                            $('#today-open-4')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;
                        }
                        break;
                    case 'SUNDAY' :
                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        $('#sunday-open')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;

                        if(today.getDay() === 0) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-open-3').html('오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                            $('#today-week')[0].textContent = '오늘 (일요일)';
                            $('#today-open-4')[0].textContent = formattedOpenTime + ' ~ ' + formattedCloseTime;
                        }
                        break;
                }
            });

            // 원격줄서기 버튼 숨기기
            if ($('#today-open-1 strong').text() === '영업 전') {
                $('#apply').hide().prop('disabled', true);
            }

            console.log('운영시간 set 완료');
        },
         error: function(xhr, status, error) {
         // 요청 실패 시 동작
         console.error('운영시간 set 실패:', error);
         alert('운영시간 set 중 오류가 발생했습니다.');
         }
    });
}

// 상세 수정 페이지 opening-hour API
function requestOpeningHourModifyApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/openinghour/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        data: {
               "page" : 0,
               "size" : 10
              },
        success: function(response) {
            // 요청 성공 시 동작
            const opens = response.data; // 운영시간 데이터 배열

            const today = new Date();
            const currentTime
                = today.getHours().toString().padStart(2, '0') + ":" + today.getMinutes().toString().padStart(2, '0');
            let formattedOpenTime;
            let formattedCloseTime;

            opens.forEach((open) => {
                switch (open.dayOfWeek) {
                    case 'MONDAY' :
                        $('#monday-open').val(open.openAt);
                        $('#monday-close').val(open.closeAt);

                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        if(today.getDay() === 1) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                        }
                        break;
                    case 'TUESDAY' :
                        $('#tuesday-open').val(open.openAt);
                        $('#tuesday-close').val(open.closeAt);

                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        if(today.getDay() === 2) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                        }
                        break;
                    case 'WEDNESDAY' :
                        $('#wednesday-open').val(open.openAt);
                        $('#wednesday-close').val(open.closeAt);

                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        if(today.getDay() === 3) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                        }
                        break;
                    case 'THURSDAY' :
                        $('#thursday-open').val(open.openAt);
                        $('#thursday-close').val(open.closeAt);

                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        if(today.getDay() === 4) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                        }
                        break;
                    case 'FRIDAY' :
                        $('#friday-open').val(open.openAt);
                        $('#friday-close').val(open.closeAt);

                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        if(today.getDay() === 5) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                        }
                        break;
                    case 'SATURDAY' :
                        $('#saturday-open').val(open.openAt);
                        $('#saturday-close').val(open.closeAt);

                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        if(today.getDay() === 6) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                        }
                        break;
                    case 'SUNDAY' :
                        $('#sunday-open').val(open.openAt);
                        $('#sunday-close').val(open.closeAt);

                        formattedOpenTime = open.openAt.substring(0, 5);
                        formattedCloseTime = open.closeAt.substring(0, 5);

                        if(today.getDay() === 0) {
                            if (currentTime >= formattedOpenTime && currentTime < formattedCloseTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>영업중</strong> : 오늘 ' +  formattedOpenTime + ' ~ ' + formattedCloseTime);
                            }
                            $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;오늘 ' + formattedOpenTime + ' ~ ' + formattedCloseTime);
                        }
                        break;
                }
            });

            console.log('운영시간 수정 set 완료');
        },
         error: function(xhr, status, error) {
         // 요청 실패 시 동작
         console.error('운영시간 수정 set 실패:', error);
         alert('운영시간 수정 set 중 오류가 발생했습니다.');
         }
    });
}

// 상세 페이지 break-hour API
function requestBreakHourApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/breakhour/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        data: {
               "page" : 0,
               "size" : 10
              },
        success: function(response) {
            // 요청 성공 시 동작
            const breaks = response.data; // 브레이크타임 데이터 배열

            const today = new Date();
            const currentTime
                = today.getHours().toString().padStart(2, '0') + ":" + today.getMinutes().toString().padStart(2, '0');
            let formattedStartTime;
            let formattedEndTime;

            breaks.forEach((breaking) => {
                switch (breaking.dayOfWeek) {
                    case 'MONDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#monday-break')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;

                        if(today.getDay() === 1) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#apply').hide().prop('disabled', true);
                            }
                            $('#today-break-1')[0].textContent = '오늘 ' + formattedStartTime + ' ~ ' + formattedEndTime;
                            $('#today-break-2')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;
                        }
                        break;
                    case 'TUESDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#tuesday-break')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;

                        if(today.getDay() === 2) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#apply').hide().prop('disabled', true);
                            }
                            $('#today-break-1')[0].textContent = '오늘 ' + formattedStartTime + ' ~ ' + formattedEndTime;
                            $('#today-break-2')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;
                        }
                        break;
                    case 'WEDNESDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#wednesday-break')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;

                        if(today.getDay() === 3) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#apply').hide().prop('disabled', true);
                            }
                            $('#today-break-1')[0].textContent = '오늘 ' + formattedStartTime + ' ~ ' + formattedEndTime;
                            $('#today-break-2')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;
                        }
                        break;
                    case 'THURSDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#thursday-break')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;

                        if(today.getDay() === 4) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#apply').hide().prop('disabled', true);
                            }
                            $('#today-break-1')[0].textContent = '오늘 ' + formattedStartTime + ' ~ ' + formattedEndTime;
                            $('#today-break-2')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;
                        }
                        break;
                    case 'FRIDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#friday-break')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;

                        if(today.getDay() === 5) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#apply').hide().prop('disabled', true);
                            }
                            $('#today-break-1')[0].textContent = '오늘 ' + formattedStartTime + ' ~ ' + formattedEndTime;
                            $('#today-break-2')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;
                        }
                        break;
                    case 'SATURDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#saturday-break')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;

                        if(today.getDay() === 6) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#apply').hide().prop('disabled', true);
                            }
                            $('#today-break-1')[0].textContent = '오늘 ' + formattedStartTime + ' ~ ' + formattedEndTime;
                            $('#today-break-2')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;
                        }
                        break;
                    case 'SUNDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#sunday-break')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;

                        if(today.getDay() === 0) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#apply').hide().prop('disabled', true);
                            }
                            $('#today-break-1')[0].textContent = '오늘 ' + formattedStartTime + ' ~ ' + formattedEndTime;
                            $('#today-break-2')[0].textContent = formattedStartTime + ' ~ ' + formattedEndTime;
                        }
                        break;
                }
            });

            console.log('브레이크 타임 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('브레이크 타임 set 실패:', error);
        alert('브레이크 타임 set 중 오류가 발생했습니다.');
        }
    });
}

// 상세 수정 페이지 break-hour API
function requestBreakHourModifyApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/breakhour/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        data: {
               "page" : 0,
               "size" : 10
              },
        success: function(response) {
            // 요청 성공 시 동작
            const breaks = response.data; // 브레이크타임 데이터 배열

            const today = new Date();
            const currentTime
                = today.getHours().toString().padStart(2, '0') + ":" + today.getMinutes().toString().padStart(2, '0');
            let formattedStartTime;
            let formattedEndTime;

            breaks.forEach((breaking) => {
                switch (breaking.dayOfWeek) {
                    case 'MONDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#monday-break-start').val(formattedStartTime);
                        $('#monday-break-end').val(formattedEndTime);

                        if(today.getDay() === 1) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                            }
                        }
                        break;
                    case 'TUESDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#tuesday-break-start').val(formattedStartTime);
                        $('#tuesday-break-end').val(formattedEndTime);

                        if(today.getDay() === 2) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                            }
                        }
                        break;
                    case 'WEDNESDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#wednesday-break-start').val(formattedStartTime);
                        $('#wednesday-break-end').val(formattedEndTime);

                        if(today.getDay() === 3) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                            }
                        }
                        break;
                    case 'THURSDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#thursday-break-start').val(formattedStartTime);
                        $('#thursday-break-end').val(formattedEndTime);

                        if(today.getDay() === 4) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                            }
                        }
                        break;
                    case 'FRIDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#friday-break-start').val(formattedStartTime);
                        $('#friday-break-end').val(formattedEndTime);

                        if(today.getDay() === 5) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                            }
                        }
                        break;
                    case 'SATURDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#saturday-break-start').val(formattedStartTime);
                        $('#saturday-break-end').val(formattedEndTime);

                        if(today.getDay() === 6) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                            }
                        }
                        break;
                    case 'SUNDAY' :
                        formattedStartTime = breaking.breakStart.substring(0, 5);
                        formattedEndTime = breaking.breakEnd.substring(0, 5);

                        $('#sunday-break-start').val(formattedStartTime);
                        $('#sunday-break-end').val(formattedEndTime);

                        if(today.getDay() === 0) {
                            if (currentTime >= formattedStartTime && currentTime < formattedEndTime) {
                                $('#today-open-1').html('<i class="fas fa-clock"></i> <strong>브레이크 타임</strong> : ' +  formattedStartTime + ' ~ ' + formattedEndTime);
                                $('#today-open-2').html('<i class="fas fa-clock"></i> 원격줄서기 시간 :&nbsp;&nbsp;<strong>브레이크 타임</strong> ' + formattedStartTime + ' ~ ' + formattedEndTime);
                            }
                        }
                        break;
                }
            });

            console.log('브레이크 타임 수정 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('브레이크 타임 수정 set 실패:', error);
        alert('브레이크 타임 수정 set 중 오류가 발생했습니다.');
        }
    });
}

// 상세 페이지 keyword API
function requestKeywordApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/restaurant-keyword/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            if(Object.keys(response.data).length === 0) {
                let keywordHtml =
                    `
                    <div style="text-align: center;">
                        <p style="color: #696969;">설정된 키워드 정보가 없습니다.</p>
                        <a class="pick-outline" type="button">매장에 등록 요청하기</a>
                    </div>
                    `;
                // keyword 요소(내부) 끝에 추가
                $('#keyword').append(keywordHtml);

            } else {
                const restaurantKeyword = response.data; // 키워드 데이터 배열

                restaurantKeyword.forEach((keywords) => {
                    let keywordHtml =
                        `
                        <a class="pick-outline mb-2">${keywords.keyword.name}</a>
                        `;
                    // keyword 요소(내부) 끝에 추가
                    $('#keyword').append(keywordHtml);
                });
            }

            console.log('키워드 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('키워드 set 실패:', error);
        alert('키워드 set 중 오류가 발생했습니다.');
        }
    });
}

// 상세 수정 페이지 keyword API
function requestKeywordModifyApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/restaurant-keyword/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            const restaurantKeyword = response.data; // 키워드 데이터 배열
            const labels = document.querySelectorAll("#keyword .form-check-label");

            restaurantKeyword.forEach((keywords) => {
                labels.forEach((label) => {
                    if (label.textContent.trim() === keywords.keyword.name) {
                        label.previousElementSibling.checked = true; // 해당 라벨의 체크박스를 체크

                        // 초기 상태 저장
                        label.previousElementSibling.setAttribute('data-initial-checked', 'true');
                    }
                });
            });

            console.log('키워드 수정 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('키워드 수정 set 실패:', error);
        alert('키워드 수정 set 중 오류가 발생했습니다.');
        }
    });
}

// 상세 페이지 편의시설 API
function requestAmenityApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/restaurant-amenity/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            if(Object.keys(response.data).length === 0) {
                let amenityHtml =
                    `
                    <div style="text-align: center;">
                        <p style="color: #696969;">설정된 편의시설 정보가 없습니다.</p>
                        <a class="pick-outline" type="button">매장에 등록 요청하기</a>
                    </div>
                    `;
                // amenity 요소(내부) 끝에 추가
                $('#amenity').append(amenityHtml);

            } else {
                const restaurantAmenity = response.data; // 키워드 데이터 배열

                restaurantAmenity.forEach((amenitys) => {
                    let amenityHtml =
                        `
                        <p><i class="bi bi-check2-square"></i> ${amenitys.amenity.name}</p>
                        `;
                    // amenity 요소(내부) 끝에 추가
                    $('#amenity').append(amenityHtml);
                });
            }

            console.log('편의시설 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('편의시설 set 실패:', error);
        alert('편의시설 set 중 오류가 발생했습니다.');
        }
    });
}

// 상세 수정 페이지 편의시설 API
function requestAmenityModifyApi() {

    const id = document.getElementById("restaurant-id").value;

    $.ajax({
        url: `/api/restaurant-amenity/restaurant/${id}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            const restaurantAmenity = response.data; // 키워드 데이터 배열
            const labels = document.querySelectorAll("#amenity .form-check-label");

            restaurantAmenity.forEach((amenitys) => {
                labels.forEach((label) => {
                    if (label.textContent.trim() === amenitys.amenity.name) {
                        label.previousElementSibling.checked = true; // 해당 라벨의 체크박스를 체크

                        // 초기 상태 저장
                        label.previousElementSibling.setAttribute('data-initial-checked', 'true');
                    }
                });
            });

            console.log('편의시설 수정 set 완료');
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('편의시설 수정 set 실패:', error);
        alert('편의시설 수정 set 중 오류가 발생했습니다.');
        }
    });
}

// 수정 버튼 클릭 시
/*document.getElementById('modify').addEventListener('click', function(event) {
    const ownerConfirmed = confirm("정말로 수정하시겠습니까?");

    if(!userConfirmed) {
        return;
    }

    >> 점포 정보 수정 메소드 (이름, 위치, 전화번호, 매장소개) << ?
    >> 운영시간 수정 또는 생성 메소드  <<
    >> 브레이크 타임 수정 또는 생성 메소드  <<
    >> 점포 편의시설 삭제 후 생성 메소드  <<
    >> 점포 키워드 삭제 후 생성 메소드 <<
    >> 점포 메인 이미지 삭제 후 생성 메소드 << ?
    >> 메뉴 수정 또는 생성 메소드 << ?
}*/

// 레스토랑 정보 수정 메소드
function requestRestaurantUpdateApi() {

    const restaurantId = document.getElementById("restaurant-id").value;

    const formData = {
        "data": {
            "name" : $('#restaurant-name').val(),
            "address" : $('#restaurant-address').val(),
            "information" : $('#information').val(),
            "contact_number" : $('#restaurant-number').val()
        }
    };

    $.ajax({
        url: `/api/restaurant/${restaurantId}`,
        type: 'PUT', // 필요한 HTTP 메서드로 변경 (PUT 또는 PATCH 등도 가능)
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        data: JSON.stringify(formData), // 데이터를 JSON 문자열로 변환
        success: function(response) {
            // 요청 성공 시 동작
        },
        error: function(xhr, status, error) {
            // 요청 실패 시 동작
            console.error('레스토랑 정보 수정 실패:', error);
            alert('레스토랑 정보 수정 중 오류가 발생했습니다.');
        }
    });
}

// 운영시간 수정 또는 생성 메소드
function requestOpeningHourUpdateApi() {

}

// 브레이크 타임 수정 또는 생성 메소드
function requestBreakHourUpdateApi() {

}

// 키워드 삭제 후 생성 메소드
function requestKeywordUpdateApi() {
    // 현재 레스트랑 키워드 모두 삭제
    requestKeywordDeleteApi();

    // 체크한 키워드의 value를 배열에 담음
    const checkedValues = $('#keyword .form-check-input:checked').map(function() {
        return $(this).val();
    }).get();

    if (checkedValues.length === 0) {
        console.log("체크한 키워드가 없음.");
        return;
    }

    const restaurantId = document.getElementById("restaurant-id").value;

    checkedValues.forEach((checkKeyword) => {
        const formData = {
            "data": {
                "restaurant" : {
                    "id" : restaurantId
                },
                "keyword" : {
                    "id" : checkKeyword
                }
            }
        };

        $.ajax({
            url: `/api/restaurant-keyword`,
            type: 'POST', // 필요한 HTTP 메서드로 변경 (PUT 또는 PATCH 등도 가능)
            contentType: 'application/json', // JSON 형식으로 데이터 전송
            data: JSON.stringify(formData), // 데이터를 JSON 문자열로 변환
            success: function(response) {
                // 요청 성공 시 동작
            },
            error: function(xhr, status, error) {
                // 요청 실패 시 동작
                console.error('키워드 생성 실패:', error);
                alert('키워드 생성 중 오류가 발생했습니다.');
            }
        });
    });
}

// 레스토랑별 키워드 삭제
function requestKeywordDeleteApi() {

    const restaurantId = document.getElementById("restaurant-id").value;

    let restaurantKeywords;

    $.ajax({
        url: `/api/restaurant-keyword/restaurant/${restaurantId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        async: false,
        success: function(response) {
            // 요청 성공 시 동작
            restaurantKeywords = response.data.map(keywords => keywords.id);
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('키워드 불러오기 실패:', error);
        alert('키워드 불러오기 중 오류가 발생했습니다.');
        }
    });

    restaurantKeywords.forEach((restaurantKeywordId) => {

        $.ajax({
            url: `/api/restaurant-keyword/${restaurantKeywordId}`,
            type: 'DELETE', // 필요한 HTTP 메서드로 변경 (PUT 또는 PATCH 등도 가능)
            success: function(response) {
                // 요청 성공 시 동작
            },
            error: function(xhr, status, error) {
                // 요청 실패 시 동작
                console.error('키워드 삭제 실패:', error);
                alert('키워드 삭제 중 오류가 발생했습니다.');
            }
        });
    });
}

// 편의시설 삭제 후 생성 메소드
function requestAmenityUpdateApi() {
    // 현재 레스트랑 편의시설 모두 삭제
    requestAmenityDeleteApi();

    // 체크한 편의시설의 value를 배열에 담음
    const checkedValues = $('#amenity .form-check-input:checked').map(function() {
        return $(this).val();
    }).get();

    if (checkedValues.length === 0) {
        console.log("체크한 편의시설가 없음.");
        return;
    }

    const restaurantId = document.getElementById("restaurant-id").value;

    checkedValues.forEach((checkAmenity) => {
        const formData = {
            "data": {
                "restaurant" : {
                    "id" : restaurantId
                },
                "amenity" : {
                    "id" : checkAmenity
                }
            }
        };

        $.ajax({
            url: `/api/restaurant-amenity`,
            type: 'POST', // 필요한 HTTP 메서드로 변경 (PUT 또는 PATCH 등도 가능)
            contentType: 'application/json', // JSON 형식으로 데이터 전송
            data: JSON.stringify(formData), // 데이터를 JSON 문자열로 변환
            success: function(response) {
                // 요청 성공 시 동작
            },
            error: function(xhr, status, error) {
                // 요청 실패 시 동작
                console.error('편의시설 생성 실패:', error);
                alert('편의시설 생성 중 오류가 발생했습니다.');
            }
        });
    });
}

// 레스토랑별 편의시설 삭제
function requestAmenityDeleteApi() {

    const restaurantId = document.getElementById("restaurant-id").value;

    let restaurantAmenities;

    $.ajax({
        url: `/api/restaurant-amenity/restaurant/${restaurantId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        async: false,
        success: function(response) {
            // 요청 성공 시 동작
            restaurantAmenities = response.data.map(amenities => amenities.id);
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('편의시설 불러오기 실패:', error);
        alert('편의시설 불러오기 중 오류가 발생했습니다.');
        }
    });

    restaurantAmenities.forEach((restaurantAmenityId) => {

        $.ajax({
            url: `/api/restaurant-amenity/${restaurantAmenityId}`,
            type: 'DELETE', // 필요한 HTTP 메서드로 변경 (PUT 또는 PATCH 등도 가능)
            success: function(response) {
                // 요청 성공 시 동작
            },
            error: function(xhr, status, error) {
                // 요청 실패 시 동작
                console.error('편의시설 삭제 실패:', error);
                alert('편의시설 삭제 중 오류가 발생했습니다.');
            }
        });
    });
}

// 레스토랑 관련 전체 수정
function requestRestaurantUpdateAllApi() {

    const restaurantId = $('#restaurant-id').val();
    const restaurantImageList = [];

    const formData = new FormData();

    const restaurantImageFiles = document.getElementById('restaurant-image-input').files;

    for (const restaurantImageFile of restaurantImageFiles) {
        restaurantImageList.push({ "needFileChange": true });
        formData.append('restaurantImages', restaurantImageFile);
    }

    if (restaurantImageFiles) {
        formData.append('restaurantImages', restaurantImageFiles);
    }

    // 메뉴 데이터를 수집
    const menuItems = document.getElementById('menu-list').getElementsByClassName('menu-item');
    const menuItemList = [];

    // 메뉴 항목을 순회하며 데이터와 이미지 처리
    Array.from(menuItems).forEach((menuItem, index) => {
        // 메뉴 데이터 수집
        const nameInput = menuItem.querySelector('input[placeholder="메뉴명"]');
        const priceInput = menuItem.querySelector('input[placeholder="가격"]');
        const recommendationCheckbox = menuItem.querySelector('input[type="checkbox"]');

        // 메뉴 이미지 처리
        const imageInput = menuItem.querySelector('input[type="file"]');

        let needFileChange = false;

        if (imageInput && imageInput.files.length > 0) {
            const menuImageFile = imageInput.files[0];
            formData.append('menuImages', menuImageFile); // 실제 파일 추가

            needFileChange = true;
        } else {
            // 빈 파일에 대한 자리 맞추기
            formData.append('menuImages', new Blob(), null); // 더미 데이터 추가
        }

        menuItemList.push({
            // id: 0, // 기본값
            needFileChange: needFileChange, // 파일 변경 필요 여부 기본값
            name: nameInput ? nameInput.value : "", // 메뉴명 값 설정
            price: priceInput.value.endsWith("원") ? priceInput.value.slice(0, -1) : "",  // 가격 값 설정
            description: "", // description은 화면 입력값이 없으므로 기본값
            recommendation: recommendationCheckbox ? recommendationCheckbox.checked : false // 추천 여부
        });
    });

    const request = {
           "name": $('#restaurant-name').val(),
           "address": $('#restaurant-address').val(),
           "information": $('#information').val(),
           "contact_number": $('#restaurant-number').val(),
           "openingHourList": getTimeList(true),
           "breakHourList": getTimeList(false),
           "restaurantAmenityList": getCheckedAmenities(),
           "restaurantKeywordList":getCheckedKeywords(),
           "restaurantImageList": restaurantImageList,
           "menuItemList": menuItemList
        };

    formData.append('data', JSON.stringify(request));

    $.ajax({
        url: `/api/restaurant/all/${restaurantId}`,
        method: 'PUT',
        data: formData,
        processData: false,
        contentType: false,
        success: function(response) {
            if(response.resultCode === 'OK') {
                if ('scrollRestoration' in history) {
                    history.scrollRestoration = 'manual'; // 자동 복원을 방지
                }
                window.location.reload();
            } else {
                alert('수정에 실패하였습니다.');
            }
        },
        error: function(xhr, status, error) {
            alert(`${xhr}
                   ${status}
                   ${error}`)
        }
    });
}

function getTimeList(isOpeningHours) {
    const startColumnName = isOpeningHours? 'openAt' : 'breakStart';
    const endColumnName = isOpeningHours? 'closeAt' : 'breakEnd';

    const daysOfWeek = [
        { id: "monday", name: "MONDAY" },
        { id: "tuesday", name: "TUESDAY" },
        { id: "wednesday", name: "WEDNESDAY" },
        { id: "thursday", name: "THURSDAY" },
        { id: "friday", name: "FRIDAY" },
        { id: "saturday", name: "SATURDAY" },
        { id: "sunday", name: "SUNDAY" },
    ];

    return daysOfWeek.map(day => {

        let startTime;
        let endTime;

        if(isOpeningHours) {
            startTime = $(`#${day.id}-open`).val();
            endTime = $(`#${day.id}-close`).val();
        } else {
            startTime = $(`#${day.id}-break-start`).val();
            endTime = $(`#${day.id}-break-end`).val();
        }


        // 입력값이 없는 경우 건너뛰기
        if (!startTime || !endTime) return null;

        return {
            [startColumnName]: startTime, // 계산된 속성 이름
            [endColumnName]: endTime,    // 계산된 속성 이름
            dayOfWeek: day.name
        };
    }).filter(entry => entry !== null); // 유효한 항목만 포함
}



function getCheckedAmenities() {

    let $amenity = document.getElementById('amenity');
    // 'form-check-input' 클래스를 가진 모든 자식 요소를 가져옴
    let inputs = $amenity.getElementsByClassName('form-check-input');

    let result = [];

    // 모든 input 요소를 순회
    Array.from(inputs).forEach((input, index) => {
                   // 체크되어 있는 경우만 처리
                   if (input.checked) {
                       // JSON 형식으로 추가
                       result.push({
                           "amenity": {
                               "id": parseInt(input.value) // value를 숫자로 변환
                           }
                       });
                   }
               });

    return result;
}

function getCheckedKeywords() {

    let $keyword = document.getElementById('keyword');
    // 'form-check-input' 클래스를 가진 모든 자식 요소를 가져옴
    let inputs = $keyword.getElementsByClassName('form-check-input');

    let result = [];

    // 모든 input 요소를 순회
    Array.from(inputs).forEach((input, index) => {
                   // 체크되어 있는 경우만 처리
                   if (input.checked) {
                       // JSON 형식으로 추가
                       result.push({
                           "keyword": {
                               "id": parseInt(input.value) // value를 숫자로 변환
                           }
                       });
                   }
               });

    return result;
}

// 점주 별 레스토랑 정보 조회
function requestOwnerNavApi() {
    const userId = $('#userId').val();

    $.ajax({
        url: `/api/restaurant/owner/my-restaurants/${userId}`,
        type: 'GET', // 필요한 HTTP 메서드로 변경
        contentType: 'application/json', // JSON 형식으로 데이터 전송
        success: function(response) {
            // 요청 성공 시 동작
            if(response.data) {
                response.data.forEach((restaurant) => {
                    let restaurantHtml =
                        `
                        <li><a class="dropdown-item" href="/restaurant/modify/${restaurant.id}">${restaurant.name}</a></li>
                        `;

                    // information 요소(내부) 끝에 추가
                    $('.dropdown-menu').prepend(restaurantHtml);
                });
            }
        },
        error: function(xhr, status, error) {
        // 요청 실패 시 동작
        console.error('점주 레스토랑 불러오기 실패:', error);
        alert('점주 레스토랑 불러오기 중 오류가 발생했습니다.');
        }
    });
}
