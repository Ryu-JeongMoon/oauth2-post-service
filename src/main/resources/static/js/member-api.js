async function requestLogin() {
  const emailInput = $('#emailInput').val();
  const passwordInput = $('#passwordInput').val();

  if (emailInput.length < 7 || passwordInput.length < 4) {
    Swal.fire({
      icon: 'warning',
      title: '입력 값에 이상이 있습니다',
      text: '이메일은 7자 이상, 비밀번호는 4자 이상 입력하세요',
    });
    return;
  }

  const data = {
    email: emailInput,
    password: passwordInput,
  };
  const post_data = JSON.stringify(data);

  await $.post('/login', post_data,
    () => Swal.fire({
      icon: 'success',
      title: '로그인 되었습니다',
      text: '홈페이지로 이동합니다',
    }).then(() => {
      setTimeout(() => location.href = '/', 100);
    }),
    (request, status, error) => {
      const response = request.responseJSON;
      console.log(response);

      const errors = response.errors;
      console.log(errors);

      Swal.fire({
        icon: 'error',
        title: '로그인할 수 없습니다',
        html: `${errors[0].defaultMessage}<br/>입력값 => ${errors[0].code} : ${errors[0].rejectedValue}`,
      });
    },
  );
}

async function requestEdit() {
  if ($('#password').val().length > 0 && $('#password').val().length < 4) {
    Swal.fire({
      icon: 'warning',
      title: '수정할 수 없습니다',
      text: '비밀번호는 4자 이상 입력하거나\n변경하지 않으려면 입력을 비워야 합니다',
    });
    return;
  }

  const data = {
    id: $('#id').val(),
    nickname: $('#nickname').val(),
    password: $('#password').val(),
    role: $('#role').val(),
    status: $('#status').val(),
  };
  const patch_data = JSON.stringify(data);

  await $.patch(
    '/members/edit-page', patch_data,
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

async function deleteMember(delete_data, failText) {
  await $.delete(
    '/members/edit-page', delete_data,
    () => {
      Swal.fire({
        icon: 'success',
        title: '삭제 되었습니다',
        text: '목록 페이지로 이동합니다',
      }).then(() => {
        setTimeout(() => history.back(), 100);
      });
    },
    () => {
      Swal.fire({
        icon: 'error',
        title: '삭제할 수 없습니다',
        text: failText,
      }).then(() => {
        setTimeout(() => history.back(), 100);
      });
    },
  );
}

async function deleteByOwner() {
  const { value: passwordValue } = await Swal.fire({
    icon: 'warning',
    title: '비밀번호를 입력해주세요',
    input: 'password',
    showCancelButton: true,
    inputPlaceHolder: '비밀번호를 입력해주세요',
    inputAttributes: {
      minlength: 4,
      maxLength: 255,
      autocapitalize: 'off',
      autocorrect: 'off',
    },
    inputValidator: (value) => {
      return new Promise((resolve) => {
        if (value.length < 4)
          resolve('비밀번호는 4자 이상 입력해야 합니다');
        else
          resolve();
      });
    },
  });

  if (!passwordValue)
    return;

  const data = {
    id: $('#id').val(),
    password: passwordValue,
  };
  let delete_data = JSON.stringify(data);

  await deleteMember(delete_data, '비밀번호가 맞지 않습니다');
}

async function deleteByAdmin() {
  const data = {
    id: $('#id').val(),
    password: null,
  };
  const delete_data = JSON.stringify(data);

  await deleteMember(delete_data, '요청 권한이 없습니다');
}

function moveToHomePage() {
  location.href = '/';
}

function moveToDetailPage(memberId) {
  location.href = `/members/${memberId}`;
}

function moveToEditPage(memberId) {
  location.href = `/members/edit-page?id=${memberId}`;
}