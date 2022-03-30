function moveToDetailPage(postId) {
  location.href = `/posts/${postId}`;
}

function moveToEditPage(postId) {
  location.href = `/posts/edit-page/${postId}`;
}

function moveToWritePage() {
  location.href = '/posts/write-page';
}

function moveToListPage() {
  location.href = '/posts';
}

function moveToPostByNicknamePage(nickname) {
  location.href = `/posts?nickname=${nickname}`;
}

async function writePost() {
  const data = {
    memberId: $('#memberId').val(),
    title: $('#title').val(),
    content: $('textarea#content').val(),
    openedAt: $('#openedAt').val(),
    closedAt: $('#closedAt').val(),
  };

  let post_data = JSON.stringify(data);

  await $.post(
    '/posts', post_data,
    () => {
      Swal.fire({
        icon: 'success',
        title: '작성 되었습니다',
        text: '이전 페이지로 이동합니다',
      }).then(() => {
        setTimeout(() => history.back(), 100);
      });
    },
    () => {
      Swal.fire({
        icon: 'error',
        title: '작성할 수 없습니다',
        text: '요청 권한이 없습니다',
      }).then(() => {
        setTimeout(() => history.back(), 100);
      });
    },
  );
}

async function editPost() {
  if (!$('#closedAt') && $('#closedAt').val() < new Date()) {
    Swal.fire({
      icon: 'warning',
      title: '수정할 수 없습니다',
      text: '종료일은 현재보다 앞설 수 없습니다',
    });
    return;
  }

  const data = {
    title: $('#title').val(),
    content: $('textarea#content').val(),
    openedAt: $('#openedAt').val(),
    closedAt: $('#closedAt').val(),
  };
  const patch_data = JSON.stringify(data);

  const postId = $('#id').val();
  await $.patch(
    `/posts/edit-page/${postId}`, patch_data,
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

async function closePost(postId) {
  await $.delete(
    `/posts/${postId}`, null,
    () => {
      Swal.fire({
        icon: 'success',
        title: '비활성화 되었습니다',
        text: '목록으로 이동합니다',
      }).then(() => {
        setTimeout(() => moveToListPage(), 100);
      });
    },
    () => {
      Swal.fire({
        icon: 'error',
        title: '비활성화할 수 없습니다',
        text: '상세 페이지로 이동합니다',
      }).then(() => {
        setTimeout(() => history.back(), 100);
      });
    },
  );
}

async function reopenPost(postId) {
  await $.patch(
    `/posts/${postId}`, null,
    () => {
      Swal.fire({
        icon: 'success',
        title: '활성화 되었습니다',
        text: '목록으로 이동합니다',
      }).then(() => {
        setTimeout(() => moveToListPage(), 100);
      });
    },
    () => {
      Swal.fire({
        icon: 'error',
        title: '활성화할 수 없습니다',
        text: '상세 페이지로 이동합니다',
      }).then(() => {
        setTimeout(() => history.back(), 100);
      });
    },
  );
}