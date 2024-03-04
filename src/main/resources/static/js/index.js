function logout() {
    axios.post('/logout', {}, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    })
        .then(function (response) {
            // 로그아웃이 성공적으로 이루어졌다면, 아래와 같이 페이지를 리다이렉트 하거나 사용자에게 알림을 주는 등의 처리를 해주시면 될 것 같습니다.
            alert("로그아웃이 완료되었습니다.")
            localStorage.clear(); // 이 코드는 로컬 스토리지의 모든 값을 삭제합니다.
            window.location.href = '/';
        })
        .catch(function (error) {
            console.error('Logout Failed:', error);
        });
}
window.onload = function() {
    const isLoggedIn = localStorage.getItem('isLoggedIn');
    const logoutButton = document.querySelector('button[onclick="logout()"]');
    const loginLink = document.getElementById('sign-text');

    if (isLoggedIn) {
        logoutButton.style.display = 'block';
        loginLink.style.display = 'none';
    } else {
        logoutButton.style.display = 'none';
        loginLink.style.display = 'block';
    }
};