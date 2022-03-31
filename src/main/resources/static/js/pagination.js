;(function(global, $) {
  'use strict';

  if (!$) {
    throw new Error('jQuery 라이브러리를 호출해야 사용 가능합니다.');
  }

  $(function() {
    $('#dataTable').DataTable({
      'info': false,
      'paging': false,
      'ordering': false,
      'searching': false,
      'autoWidth': false,
      'responsive': true,
      'processing': true,
    });
  });

  $('.dataTables_length').addClass('bs-select');
})(window, window.jQuery);