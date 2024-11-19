document.addEventListener("DOMContentLoaded", function() {
    console.log("DOM fully loaded.");

    // lucide.js가 이미 로드된 경우 바로 replace()
    if (typeof lucide !== "undefined") {
        console.log("Lucide object found:", lucide);
        if (typeof lucide.replace === 'function') {
            lucide.replace();  // 아이콘 교체
            console.log("Lucide icons replaced immediately.");
        } else {
            console.error("lucide.replace is not available yet.");
        }
    } else {
        // Lucide.js가 로드되지 않았다면, 미리 로드하기
        const script = document.createElement("script");
        script.src = "https://unpkg.com/lucide@0.46.0/dist/umd/lucide.min.js";  // 특정 버전 지정
        script.async = true;  // 비동기적으로 로드

        // 스크립트 로드가 완료되면 replace() 호출
        script.onload = () => {
            console.log("Lucide.js loaded successfully.");
            console.log("Lucide object:", lucide);  // lucide 객체가 정상적으로 로드되었는지 확인

            // lucide 객체가 올바르게 로드된 후 메서드가 존재하는지 확인
            if (lucide && typeof lucide.replace === 'function') {
                lucide.replace();  // 아이콘 교체
                console.log("Lucide icons replaced after script load.");
            } else {
                console.error("Lucide.replace() is still unavailable.");
            }
        };

        // 로드 실패 처리
        script.onerror = () => {
            console.error("Lucide.js script failed to load.");
        };

        // head에 스크립트 추가
        document.head.appendChild(script);
    }
    // 사용자 ID 가져오기
    const userId = document.getElementById("userId")?.value;
    console.log("User ID:", userId);

    // 즐겨찾기 데이터 가져오기
    fetch(`/api/bookmark/user/${userId}`)
        .then((response) => response.json())
        .then((data) => {
            console.log("Favorite restaurants data:", data);
            const favoriteRestaurants = data.data;

            // 즐겨찾기 상태 업데이트
            document.querySelectorAll(".favorite-btn").forEach((button) => {
                const restaurantId = button.dataset.id;
                const icon = button.querySelector("i");
                const isFavorited = favoriteRestaurants.some((item) => item.restaurantId == restaurantId);

                if (isFavorited) {
                    button.setAttribute("data-favorite", "true");
                    icon.setAttribute("data-lucide", "heart-fill"); // 하트 채워짐
                } else {
                    button.setAttribute("data-favorite", "false");
                    icon.setAttribute("data-lucide", "heart"); // 하트 비어 있음
                }
            });

            // Lucide 아이콘 다시 렌더링
            if (typeof lucide !== 'undefined' && lucide.replace) {
                lucide.replace();  // 다른 아이콘 초기화하지 않도록 리플레이스
                console.log("Lucide icons replaced after favorite update.");
            }
        })
        .catch((error) => {
            console.error("Error fetching favorite restaurants:", error);
        });

    // 검색 및 정렬 기능은 여기서 계속 처리
    const searchForm = document.getElementById("searchForm");
    const searchInput = document.getElementById("searchInput");
    searchForm.addEventListener("submit", (e) => {
        e.preventDefault();
        console.log("Searching for:", searchInput.value);
        // 여기에 실제 검색 로직 구현
    });

    const sortSelect = document.getElementById("sortSelect");
    sortSelect.addEventListener("change", () => {
        console.log("Sorting by:", sortSelect.value);
        // 여기에 실제 정렬 로직 구현
    });
});
