function deleteCareer() {
    // "정말 삭제하시겠습니까?" 확인
    const confirmDelete = confirm("정말 삭제하시겠습니까?");

    if (confirmDelete) {
        // 현재 커리어의 ID 가져오기
        const careerId = document.getElementById("delete").getAttribute("data-career-id");
        const token = document.cookie;

        // DELETE 요청을 보내어 커리어 삭제
        axios.delete(`http://localhost:8080/career/${careerId}`, {
            headers: {
                Authorization: `Bearer ${token}`  // Bearer 스킴 사용
            }
        })
            .then(function (response) {
                // 삭제 성공 시 알림 표시
                console.log(response);
                alert("커리어가 삭제되었습니다.");
                window.location.href = "/";
            })
            .catch(function (error) {
                // 오류 발생 시 알림 표시
                alert("커리어 삭제 중 오류가 발생했습니다.");
                console.error("Error deleting career:", error);
            });
    }
}