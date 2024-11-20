// 루시드 초기화 금지
document.body.addEventListener('click', (event) => {
    if (event.target.classList.contains('favorite-btn')) {
        handleFavoriteClick(event);
    }
});

document.addEventListener("DOMContentLoaded", function () {
    console.log("DOM fully loaded.");

    // 사용자 ID 가져오기
    const userId = document.getElementById("userId")?.value;
    console.log("User ID:", userId);

     if (!window.LucideInitialized) {
            lucide.createIcons();
            window.LucideInitialized = true;
        }

    const container = document.querySelector(".card-header");
    if (!container) {
        console.error("Container for buttons not found!");
        return;
    }

    const buttons = container.querySelectorAll('.favorite-btn');
    if (buttons.length === 0) {
        console.error('Favorite buttons not found. Check if the grid is rendered correctly.');
    } else {
        buttons.forEach(button => {
            button.addEventListener('click', handleFavoriteClick);
        });
    }

    buttons.forEach(button => {
        button.addEventListener('click', () => {
            const restaurantId = button.getAttribute('data-id');
            const icon = button.querySelector('i');

            if (button.getAttribute('data-favorite') === 'true') {
                icon.classList.remove("fa-solid", "fa-heart");
                icon.classList.add("fa-regular", "fa-heart");
                button.setAttribute('data-favorite', 'false');
            } else {
                icon.classList.remove("fa-regular", "fa-heart");
                icon.classList.add("fa-solid", "fa-heart");
                button.setAttribute('data-favorite', 'true');
            }

            toggleFavorite(restaurantId);
        });
    });

    // Event delegation을 사용하여 버튼 클릭 처리
    container.addEventListener('click', function(event) {
        if (event.target && event.target.matches('.favorite-btn')) {
            const button = event.target;
            const restaurantId = button.getAttribute('data-id');
            const icon = button.querySelector('i');

            if (button.getAttribute('data-favorite') === 'true') {
                icon.classList.remove("fa-solid", "fa-heart");
                icon.classList.add("fa-regular", "fa-heart");
                button.setAttribute('data-favorite', 'false');
            } else {
                icon.classList.remove("fa-regular", "fa-heart");
                icon.classList.add("fa-solid", "fa-heart");
                button.setAttribute('data-favorite', 'true');
            }

            toggleFavorite(restaurantId);
        }
    });

    // 즐겨찾기 데이터 가져오기
    fetch(`/api/bookmark/user/${userId}`)
        .then((response) => response.json())
        .then((data) => {
            const favoriteRestaurants = data.data;
            console.log("Favorite restaurants data:", favoriteRestaurants);

            favoriteRestaurants.forEach((item) => {
                const button = document.querySelector(`.favorite-btn[data-id="${item.restaurantId}"]`);
                const icon = button?.querySelector("i");
                if (button) {
                    button.setAttribute("data-favorite", "true");
                    if (icon) {
                        icon.classList.remove("fa-regular");
                        icon.classList.add("fa-solid"); // 채워진 하트
                    }
                }
            });
        });

    // 이벤트 위임: 클릭 이벤트 처리
    container.addEventListener("click", (event) => {
        const button = event.target.closest(".favorite-btn");
        if (!button) return; // 클릭한 대상이 버튼이 아니면 무시

        const restaurantId = button.dataset.id;
        const icon = button.querySelector("i");
        const isFavorite = button.getAttribute("data-favorite") === "true";

        if (isFavorite) {
            removeFavorite(restaurantId, button, icon);
        } else {
            addFavorite(restaurantId, button, icon, userId);
        }
    });

    // 즐겨찾기 추가
    function addFavorite(restaurantId, button, icon, userId) {
        fetch(`/api/bookmark`, {
            method: "POST",
            headers: {
                "Content-Type": "application/json",
            },
            body: JSON.stringify({
                userId: userId,
                restaurantId: restaurantId,
            }),
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.resultCode === "OK") {
                    button.setAttribute("data-favorite", "true");
                    icon.classList.remove("fa-regular");
                    icon.classList.add("fa-solid"); // 채워진 하트
                    console.log("Favorite added:", restaurantId);
                } else {
                    console.error("Failed to add favorite:", data.description);
                }
            })
            .catch((error) => {
                console.error("Error adding favorite:", error);
            });
    }

    // 즐겨찾기 삭제
    function removeFavorite(restaurantId, button, icon) {
        fetch(`/api/bookmark/${restaurantId}`, {
            method: "DELETE",
        })
            .then((response) => response.json())
            .then((data) => {
                if (data.resultCode === "OK") {
                    button.setAttribute("data-favorite", "false");
                    icon.classList.remove("fa-solid");
                    icon.classList.add("fa-regular"); // 비어있는 하트
                    console.log("Favorite removed:", restaurantId);
                } else {
                    console.error("Failed to remove favorite:", data.description);
                }
            })
            .catch((error) => console.error("Error removing favorite:", error));
    }

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
}); // DOMContentLoaded 이벤트 핸들러 닫기
