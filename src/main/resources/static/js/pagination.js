;(function(global, $) {
  'use strict';

  if (!$) {
    throw new Error('jQuery 라이브러리를 호출해야 사용 가능합니다.');
  }

  $(function() {
    $('#dataTable').DataTable({
      'paging': true,
      'searching': true,
      'info': true,
      'autoWidth': false,
      'responsive': true,
      'ordering': true,
      'orderFixed': {
        'pre': [2, 'desc'],
      },
      'processing': true,
      'pagingType': 'first_last_numbers',
      'lengthMenu': [100, 50, 25, 10],
    });
  });

  $('.dataTables_length').addClass('bs-select');
})(window, window.jQuery);