

function deleteCareer() {
    // "정말 삭제하시겠습니까?" 확인
    const confirmDelete = confirm("정말 삭제하시겠습니까?");

    if (confirmDelete) {
        // 현재 커리어의 ID 가져오기
        const careerId = document.getElementById("delete").getAttribute("data-career-id");
        axios.delete(`/career/${careerId}`, {
            headers: {
                'Authorization': `Bearer ${localStorage.getItem('token')}`
            }
        })
            .then(function(response) {
                // 성공적으로 삭제되었을 때를 대비한 처리
                alert("성공적으로 삭제되었습니다.");
                window.location.href = '/';
            })
            .catch(function(error) {
                // 삭제에 실패했을 때를 대비한 처리
                alert("삭제에 실패했습니다.");
                window.onload;
            });
    }
}