function requestEdit() {
  const api_with_id = '/members/edit-page';
  const data = {
    id: $('#id').val(),
    nickname: $('#nickname').val(),
    password: $('#password').val(),
    role: $('#role').val(),
    status: $('#status').val(),
  };

  const patch_data = JSON.stringify(data);

  $.patch(
    api_with_id, patch_data,
    () => {
      Swal.fire({
        icon: 'success',
        title: '수정 되었습니다',
        text: '상세 페이지로 이동합니다',
      }).then(() => {
        setTimeout(() => history.back(), 100);
      });

    },
    () => {
      Swal.fire({
        icon: 'error',
        title: '수정할 수 없습니다',
        text: '요청 권한이 없습니다',
      }).then(() => {
        setTimeout(() => history.back(), 100);
      });
    },
  );
}

function moveToEditPage(memberId) {
  location.href = '/members/' + memberId;
}

function requestLogin() {
  const api_with_id = '/members';

  const data = {
    email: $('#emailInput').val(),
    password: $('#passwordInput').val(),
  };

  const patch_data = JSON.stringify(data);

  $.post(api_with_id, patch_data,
    () => alert('성공'), () => alert('로그인 실패~~!'),
  );
}