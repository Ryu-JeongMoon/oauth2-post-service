/*! jquery.ajax.extends.js @ 2018, yamoo9.net */
;(function(global, $) {
  'use strict';

  if (!$) {
    throw new Error('jQuery 라이브러리를 호출해야 사용 가능합니다.');
  }

  $.post = function(api_url, post_data, success, fail) {
    $.ajax({
      method: 'POST',
      url: api_url,
      data: post_data,
      contentType: 'application/json',
    })
      .then(
        $.type(success) === 'function' ? success : function() {
        },
        $.type(fail) === 'function' ? fail : function() {
        },
      );
  };

  if (!$.put) {
    $.put = function(api_url, put_data, success, fail) {
      $.ajax({
        method: 'PUT',
        url: api_url,
        data: put_data,
        contentType: 'application/json',
      })
        .then(
          $.type(success) === 'function' ? success : function() {
          },
          $.type(fail) === 'function' ? fail : function() {
          },
        );
    };
  }


  if (!$.patch) {
    $.patch = function(api_url, patch_data, success, fail) {
      $.ajax({
        method: 'PATCH',
        url: api_url,
        data: patch_data,
        contentType: 'application/json',
      })
        .then(
          $.type(success) === 'function' ? success : function() {
          },
          $.type(fail) === 'function' ? fail : function() {
          },
        );
    };
  }


  if (!$.delete) {
    $.delete = function(api_url, delete_data, success, fail) {
      $.ajax({
        method: 'DELETE',
        url: api_url,
        data: delete_data,
        dataType: 'json',
        contentType: 'application/json',
      })
        .then(
          $.type(success) === 'function' ? success : function() {
          },
          $.type(fail) === 'function' ? fail : function() {
          },
        );
    };
  }

  if (!$.jsonp) {
    $.jsonp = function(api_address, success, fail) {
      $.ajax({
        url: api_address,
        dataType: 'jsonp',
      })
        .then(
          $.type(success) === 'function' ? success : function() {
          },
          $.type(fail) === 'function' ? fail : function() {
          },
        );
    };
  }

})(window, window.jQuery);