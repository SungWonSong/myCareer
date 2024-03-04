function create(title, content) {
    // 새로운 커리어 정보를 포함하는 객체 생성
    const newCareer = {
        title: title,
        content: content
    };

    axios.post('/career/create', newCareer, {
        headers: {
            'Authorization': `Bearer ${localStorage.getItem('token')}`
        }
    })
    .then(function(response) {
        // 요청이 성공적으로 처리되었을 때를 대비한 처리
        alert("성공적으로 생성되었습니다.");
        window.location.href = '/';
    })
    .catch(function(error) {
        // 요청이 실패했을 때를 대비한 처리
        alert("생성에 실패했습니다.");
        console.error(error);
    });
}

function createCancel() {
    // 메인 페이지로 이동
    window.location.href = '/';
}

document.querySelector('.buttonCreate').addEventListener('click', function(e) {
    e.preventDefault(); // 폼 제출을 막기 위함

    // 제목과 내용 가져오기
    const title = document.querySelector('input[name="input"]').value;
    const content = $('#summernote').summernote('code');

    // createCareer 함수 호출
    createCareer(title, content);
});


document.querySelector('.buttonCancel').addEventListener('click', function(e) {
    e.preventDefault(); // 폼 제출을 막기 위함

    // createCancel 함수 호출
    createCancel();
});