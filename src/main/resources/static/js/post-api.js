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
  if (!$('#openedAt').val() || !$('#closedAt').val()) {
    Swal.fire({
      icon: 'warning',
      title: '작성할 수 없습니다',
      text: '게시일과 종료일은 반드시 선택해야 합니다',
    });
    return;
  }

  if (Date.parse($('#closedAt').val()) < new Date().getTime()) {
    Swal.fire({
      icon: 'warning',
      title: '작성할 수 없습니다',
      text: '종료일은 현재 시간 이후로 선택해야 합니다',
    });
    return;
  }

  if (Date.parse($('#closedAt').val()) < Date.parse($('#openedAt').val())) {
    Swal.fire({
      icon: 'warning',
      title: '작성할 수 없습니다',
      text: '종료일은 게시일 이후로 선택 가능합니다',
    });
    return;
  }

  const data = {
    memberId: $('#memberId').val(),
    title: $('#title').val(),
    content: $('textarea#content').val(),
    openedAt: $('#openedAt').val(),
    closedAt: $('#closedAt').val(),
  };

  let post_data = JSON.stringify(data);

  await $.post(
    '/posts',
    post_data,
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
  if ($('#closedAt') && Date.parse($('#closedAt').val()) < new Date().getTime()) {
    Swal.fire({
      icon: 'warning',
      title: '수정할 수 없습니다',
      text: '종료일은 현재 시간 이후로 선택해야 합니다',
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
    `/posts/edit-page/${postId}`,
    patch_data,
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
    `/posts/${postId}`,
    null,
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
    `/posts/${postId}`,
    null,
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

function moveToPage(queryParams) {
  const queryString = setQueryStringParams(queryParams);
  location.href = `/posts?${queryString}`;
}

function moveToPageByPageNumber(pageNumber) {
  const queryParams = { page: pageNumber };

  moveToPage(queryParams);
}

function search() {
  const column = $('#column').val().toLowerCase();
  const keyword = $('#keyword').val();
  const queryParams = {};
  queryParams[column] = keyword;

  moveToPage(queryParams);
}

function setQueryStringParams(queryParams) {
  if (!location.search) return formToQueryString(queryParams);

  new URLSearchParams(location.search).forEach((param, column) => {
    if (!queryParams[column]) queryParams[column] = param;
  });
  return formToQueryString(queryParams);
}

function formToQueryString(queryParams) {
  return Object.entries(queryParams)
    .map((k) => `${encodeURIComponent(k[0])}=${encodeURIComponent(k[1])}`)
    .join('&');
}

window.onload = function () {
  document.querySelector('#keyword')?.addEventListener('keydown', (e) => {
    if (e.key === 'Enter') search();
  });

  document.querySelector('#search-button')?.addEventListener('click', () => {
    search();
  });

  document.querySelector('#write-button')?.addEventListener('click', () => {
    moveToWritePage();
  });

  const totalPage = parseInt(document.querySelector('#page-list')?.getAttribute('data-total-page'));

  document.querySelectorAll('[class*=page-link]').forEach((q) =>
    q.addEventListener('click', () => {
      let pageNumber = q.getAttribute('data-page');

      if (pageNumber > 0 && pageNumber <= totalPage) moveToPageByPageNumber(pageNumber);
    }),
  );
};
