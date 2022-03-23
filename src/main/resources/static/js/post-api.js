function moveToDetailPage(postId) {
  location.href = `/posts/${postId}`;
}

function moveToEditPage(postId) {
  location.href = `/posts/edit-page/${postId}`;
}