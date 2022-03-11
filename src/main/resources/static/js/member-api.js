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
    () => alert('성공'), () => alert('실패'),
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