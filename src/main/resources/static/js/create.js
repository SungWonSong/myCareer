function create() {
    const title = document.getElementById('title').value;
    const content = $('#summernote').summernote('code');

    const data = {
        title: title,
        content: content
    };

    axios.post('/career/create', data, {
        headers: {
            'Content-Type': 'application/json',
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    })
        .then(function (response) {
            // 요청이 성공적으로 처리되었을 때를 대비한 처리
            alert("성공적으로 생성되었습니다.");
            console.log(response.data.id);
            window.location.href = '/';
        })
        .catch(function (error) {
            // 요청이 실패했을 때를 대비한 처리
            alert("생성에 실패했습니다.");
            console.error(error);
        });
}

function createCancel() {
    // 메인 페이지로 이동
    window.location.href = '/';
}




