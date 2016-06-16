/*
var Mini = {};
Mini.tagList = [];
Mini.imageList = [];
Mini.imageIndexListList = [];
*/

$(function() {
  var SC_LOADING = 0;
  var SC_COMPLETE = 1;
  var SC_ERROR = 2;

  var currentTagI = -1;

  var resetStatus = function(sc) {
    $('ul#status li').addClass('hidden');
    switch (sc) {
    case SC_LOADING:
      $('ul#status li:nth-child(1)').removeClass('hidden');
      break;
    case SC_COMPLETE:
      $('ul#status li:nth-child(2)').removeClass('hidden');
      break;
    case SC_ERROR:
      $('ul#status li:nth-child(3)').removeClass('hidden');
      break;
    }
    $('ul#status').removeClass('hidden');
  };

  var getNextCount = function(startI, lsLen) {
    var nextCount = lsLen - startI;
    if (nextCount > 5) {
      nextCount = 5;
    }
    return nextCount;
  };

  var addImages = function(tagId) {
    var groupEl = $('#right .display[data-id="' + tagId + '"]');

    // create new group element
    if (groupEl.length === 0) {
      groupEl = $('<ul class="display hidden" data-id="' + tagId + '"></div>');
      $('#right div:nth-of-type(1)').before(groupEl);
    }

    var nextI = groupEl.children().length;
    var imageIndexList = Mini.imageIndexListList[tagId];
    for (var i = 0, count = getNextCount(nextI, imageIndexList.length); i != count; i++, nextI++) {
      var imageId = imageIndexList[nextI];
      groupEl.append($('<li><img src="./image/' + Mini.imageList[imageId] + '"></li>'));
    }
    groupEl.removeClass('hidden');

    // next button
    var nextCount = getNextCount(nextI, imageIndexList.length);
    if (0 < nextCount && nextCount <= 5) {
      $('#right div:nth-of-type(1) a').text('次の ' + nextCount + ' 件');
      $('#right div:nth-of-type(1)').removeClass('hidden');
    } else {
      $('#right div:nth-of-type(1) a').text('');
      $('#right div:nth-of-type(1)').addClass('hidden');
    }
  };

  // set event
  try {
    // menu items
    $('nav#tags').on('click', 'a', function(ev) {
      var el = $(this);
      currentTagI = el.attr('data-id');
      $('nav#tags a.selected').removeClass('selected');
      $('ul.display').addClass('hidden');
      addImages(currentTagI);
      el.addClass('selected');
      $('ul#status').addClass('hidden');
    });

    // make menu items
    $.each(Mini.tagList, function(i, tag) {
      $('nav#tags').append($('<a data-id="' + i + '">' + tag + '</a>'));
    });
    
    // reset button
    $('#left nav:nth-of-type(2) a').on('click', function(ev) {
        $('#right div').addClass('hidden');
        $('#right ul.display').remove();
        $('#tags a.selected').removeClass('selected');
      resetStatus(SC_COMPLETE);
    });
    // more button
    $('#right div:nth-of-type(1) a').on('click', function(ev) {
      addImages(currentTagI);
    });

    resetStatus(SC_COMPLETE);
  } catch (e) {
    resetStatus(SC_ERROR);
    console.log(e);
  }
});
