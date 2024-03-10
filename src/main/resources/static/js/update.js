function update() {
    // 업데이트할 커리어 정보를 포함하는 객체 생성
    const title = document.getElementById('title').value;
    const content = $('#summernote').summernote('code');

    const data = {
        title: title,
        content: content
    };
    const careerId = document.getElementById("update").getAttribute("data-update-id");

    axios.put(`/career/${careerId}`, data, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    })
    .then(function(response) {
        // 요청이 성공적으로 처리되었을 때를 대비한 처리
        alert("성공적으로 업데이트되었습니다.");
        console.log(response.data.id);
        window.location.href = '/';
    })
    .catch(function(error) {
        // 요청이 실패했을 때를 대비한 처리
        alert("업데이트에 실패했습니다.");
        console.error(error);
    });
}


function updateCancel() {
    const careerId = document.getElementById("updateCancel").getAttribute("data-update-id");
    window.location.href = '/career/' + careerId;

}