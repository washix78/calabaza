Calabaza.serverUrl = window.location.origin + "/calabaza";
Calabaza.imagesPath = './mini/image/';
Calabaza.main = null;
Calabaza.confContent = null;
Calabaza.editContent = null;

Calabaza.tagIndexListMap = {};
$.each(Calabaza.imageTagListMap, function(image, tagList) {
  var tagIndexList = [];
  $.each(tagList, function(i, tag) {
    tagIndexList.push(Calabaza.tagList.indexOf(tag));
  });
  Calabaza.tagIndexListMap[image] = tagIndexList;
});

Calabaza.Main = function() {
  var pEl = $('#main');

  pEl.find('header:nth-child(1) .float_wrapper .right a:nth-child(1)').on('click', function(ev) {
    Calabaza.confContent.show();
  });

  pEl.find('#image_menu').on('click','li .display[data-id]', function(ev) {
    var imageId = $(this).attr('data-id');
    Calabaza.editContent.show(imageId);
  });

  this.rebuild = function() {
    pEl.find('ul.tags').empty();
    pEl.find('ul#image_menu li').each(function(i, el) {
      var imageId = $(el).find('.display').attr('data-id');
      var tagIndexList = Calabaza.tagIndexListMap[Calabaza.imageList[imageId]].concat();
      $.each(tagIndexList, function(li, tagI) {
        var tag = Calabaza.tagList[tagI];
        if (tag !== null) {
          $(el).find('ul.tags').append($('<li>' + Calabaza.encMap[tag] + '</li>'));
        }
      });
    });
  };
};

Calabaza.ConfContent = function() {
  var pEl = $('#conf_content');
  var displayCount = pEl.find('select').val();
  var hasNoTag = pEl.find('.table .row:nth-child(2) ul.group li:nth-child(2) input[type="checkbox"]').prop('checked');
  var tagIndexList = null;
  if (!pEl.find('.table .row:nth-child(2) ul.group li:nth-child(1) input[type="checkbox"]').prop('checked')) {
    tagIndexList = [];
    pEl.find('.tags span').each(function(i, el) {
      if ($(el).hasClass('selected')) {
        tagIndexList.push(parseInt($(el).attr('data-id')));
      }
    });
  }

  // checkbox (display all)
  pEl.find('.table .row:nth-child(2) ul.group li:nth-child(1) input[type="checkbox"]').on('change', function(ev) {
    if ($(this).prop('checked')) {
      pEl.find('.table .row:nth-child(2) ul.group li:nth-child(2)').addClass('hidden');
      pEl.find('.tags').addClass('hidden');
    } else {
      pEl.find('.table .row:nth-child(2) ul.group li:nth-child(2)').removeClass('hidden');
      pEl.find('.tags').removeClass('hidden');
    }
  });

  // tag in table
  pEl.find('.tags').on('click', '.cell span', function(ev) {
    if ($(this).hasClass('selected')) {
      $(this).removeClass('selected');
    } else {
      $(this).addClass('selected');
    }
  });

  pEl.find('footer .buttons > button:nth-child(1)').on('click', function(ev) {
    /*
     * { displayCount : 20, displayTagList : [] }
     */
    var data = {};
    var list = null;
    data['displayCount'] = pEl.find('select').val();
    if (pEl.find('.table .row:nth-child(2) ul.group li:nth-child(1) input[type="checkbox"]').prop('checked') === false) {
      list = [];
      if (pEl.find('.table .row:nth-child(2) ul.group li:nth-child(2) input[type="checkbox"]').prop('checked') === true) {
        list.push(null);
      }
      pEl.find('.tags .cell span.selected').each(function(i, el) {
        var tagI = $(el).attr('data-id');
        list.push(Calabaza.tagList[tagI]);
      });
    }

    $.ajax({
      'url' : Calabaza.serverUrl + '/api/page',
      'type' : 'PUT',
      'data' : JSON.stringify({ 'displayCount' : pEl.find('select').val(), 'displayTagList' : list }),
      'dataType' : 'text'
    }).done(function(data) {
      window.location.href = Calabaza.serverUrl + '/index.jsp';
    }).fail(function(data) {
      console.log(data);
      alert('error');
    });
  });

  pEl.find('footer .buttons > button:nth-child(2)').on('click', function(ev) {
    pEl.addClass('hidden');
    $('#overlay').addClass('hidden');
  });

  var rebuild = function() {
    pEl.find('.tags').empty();
    var tagList = Calabaza.tagList;
    var encMap = Calabaza.encMap;
    var cellCount = 0;
    var rowEl = null;
    $.each(tagList, function(i, tag) {
      if (tag === null) {
        return;
      }
      if (cellCount % 2 === 0) {
        // <div class="row">
        rowEl = $('<div class="row"></div>');
        pEl.find('.tags').append(rowEl);
      }
      // <div class="cell"><span data-id="0">TAG 1</span></div>
      // <div class="cell"><span data-id="8" class="selected">TAG 1</span></div>
      var el = (tagIndexList === null || tagIndexList.indexOf(i) === -1) ?
          $('<div class="cell"><span data-id="' + i + '">' + encMap[tag] + '</span></div>') :
          $('<div class="cell"><span data-id="' + i + '" class="selected">' + encMap[tag] + '</span></div>');
      rowEl.append(el);
      cellCount += 1;
    });
    if (cellCount % 2 === 1) {
      rowEl.append($('<div class="cell"></div>'));
    }
  }

  this.show = function() {
    // build tag table
    rebuild();

    // select box (display count)
    pEl.find('select').val(displayCount);

    // check box
    if (tagIndexList === null) {
      // all display
      pEl.find('.table .row:nth-child(2) ul.group li:nth-child(1) input[type="checkbox"]').prop({ 'checked' : true });
      pEl.find('.table .row:nth-child(2) ul.group li:nth-child(2) input[type="checkbox"]').prop({ 'checked' : true });
      pEl.find('.table .row:nth-child(2) ul.group li:nth-child(2)').addClass('hidden');
      pEl.find('.tags').addClass('hidden');
    } else {
      pEl.find('.table .row:nth-child(2) ul.group li:nth-child(1) input[type="checkbox"]').prop({ 'checked' : false });
      if (hasNoTag) {
        pEl.find('.table .row:nth-child(2) ul.group li:nth-child(2) input[type="checkbox"]').prop({ 'checked' : true });
      } else {
        pEl.find('.table .row:nth-child(2) ul.group li:nth-child(2) input[type="checkbox"]').prop({ 'checked' : false });
      }
      pEl.find('.table .row:nth-child(2) ul.group li:nth-child(2)').removeClass('hidden');
      pEl.find('.tags').removeClass('hidden');
    }

    pEl.removeClass('hidden');
    $('#overlay').removeClass('hidden');
  };
};

Calabaza.EditContent = function() {
  var pEl = $('#edit_content');
  var currentImageId = null;
  var tagIndexList = null;

  // check box
  pEl.find('.tags').on('change', 'input[type="checkbox"]', function(ev) {
    if ($(this).prop('checked')) {
      $(this).parent('div.cell').addClass('selected');
    } else {
      $(this).parent('div.cell').removeClass('selected');
    }
    tagIndexList = [];
    pEl.find('.tags .cell.selected').each(function(i, el) {
        tagIndexList.push(parseInt($(el).attr('data-id'), 10));
    });
  });

  var isValidTag = function(tag) {
    return !(/^(\s|　)*$/.test(tag));
  };

  // text box
  pEl.find('.tags').on('change', 'input[type="text"]', function(ev) {
    var el = $(this);
    var tagI = el.parent('div.cell').attr('data-id');
    var oldTag = Calabaza.tagList[tagI];
    var newTag = el.val();
    if (newTag === undefined || newTag === null || isValidTag(newTag) === false) {
      el.val(oldTag);
      return;
    }
    if (Calabaza.tagList.indexOf(newTag) !== -1) {
        alert('既に存在するタグです。');
        el.val(oldTag);
        return;
    }

    // update
    $.ajax({
      'url' : Calabaza.serverUrl + '/api/tag',
      'type' : 'PUT',
      'data' : JSON.stringify({ 'old' : oldTag, 'new' : newTag }),
      'dataType' : 'json',
      'success' : function(data) {
        var tag = data['tag'];
        Calabaza.tagList[tagI] = tag;
        Calabaza.encMap[tag] = data['enc'];
        el.val(tag);
        el[0].blur();
        alert('タグ「' + oldTag + '」を「' + tag + '」に変更しました。');
      },
      'error' : function(data) {
        console.log(data);
        alert(data.responseJSON['message']);
        el.val(oldTag);
      }
    });
  });

  // delete button
  pEl.find('.tags').on('click', 'button', function(ev) {
    var tagI = $(this).parent('div.cell').attr('data-id');
    var deleteTag = Calabaza.tagList[tagI];
    if (confirm('タグ「' + deleteTag + '」を削除してよいですか？') === false) {
      return;
    }

    $.ajax({
      'url' : Calabaza.serverUrl + '/api/tag',
      'type' : 'DELETE',
      'data' : JSON.stringify({ 'tag' : deleteTag }),
      'dataType' : 'text'
    }).done(function(data) {
      Calabaza.tagList[tagI] = null;
      rebuild();
    }).fail(function(data) {
      console.log(data);
      alert('error');
    });
  });

  var closeContent = function() {
    pEl.addClass('hidden');
    $('#overlay').addClass('hidden');
  };

  // new tag add button
  pEl.find('ul.group button').on('click', function(ev) {
    var newTag = pEl.find('ul.group input').val();
    if (newTag === undefined || newTag === null || isValidTag(newTag) === false) {
      pEl.find('ul.group input').val('');
      return;
    }
    if (Calabaza.tagList.indexOf(newTag) !== -1) {
        alert('既に存在するタグです。');
        el.val(oldTag);
        return;
    }

    $.ajax({
      'url' : Calabaza.serverUrl + '/api/tag',
      'data' : JSON.stringify({ 'tag' : newTag }),
      'type' : 'POST',
      'dataType' : 'json'
    }).done(function(data) {
      pEl.find('ul.group input').val('');
      var tag = data['tag'];
      Calabaza.tagList.push(tag);
      Calabaza.encMap[tag] = data['enc'];
      rebuild();
    }).fail(function(data) {
      console.log(data);
      alert(data.responseJSON['message']);
    });
  });

  // save
  pEl.find('footer .buttons > button:nth-child(1)').on('click', function(ev) {
    var elList = $('.tags input[type="checkbox"]:checked');
    if (elList.length < 1 || 3 < elList.length) {
      alert('付けられるタグは１個から３個です。');
      return;
    }

    var newTagList = [];
    var tagList = Calabaza.tagList;
    elList.each(function(i, el) {
      var tagI = $(el).parent('.cell').attr('data-id');
      newTagList.push(tagList[tagI]);
    });

    $.ajax({
      'url' : Calabaza.serverUrl + '/api/image',
      'type' : 'PUT',
      'data' : JSON.stringify({ 'image' : Calabaza.imageList[currentImageId], 'tagList' : newTagList }),
      'dataType' : 'json'
    }).done(function(data) {
      var imageId = data['image'];
      Calabaza.imageTagListMap[imageId] = data['tagList'];
      
      tagIndexList = [];
      $.each(data['tagList'], function(i, tag) {
          tagIndexList.push(Calabaza.tagList.indexOf(tag));
      });
      Calabaza.tagIndexListMap[imageId] = tagIndexList.concat();

      closeContent();
      Calabaza.main.rebuild();
    }).fail(function(data) {
      console.log(data);
      alert('error');
    });
  });

  //cancel
  pEl.find('footer .buttons > button:nth-child(2)').on('click', function(ev) {
    closeContent();
    Calabaza.main.rebuild();
  });

  var rebuild = function() {
    pEl.find('.tags').empty();
    var tagList = Calabaza.tagList;
    var imageTagList = Calabaza.imageTagListMap[Calabaza.imageList[currentImageId]];
    var cellCount = 0;
    var rowEl = null;
    $.each(tagList, function(i, tag) {
      if (tag === null) {
        return;
      }
      if (cellCount % 2 === 0) {
        rowEl = $('<div class="row"></div>');
        pEl.find('.tags').append(rowEl);
      }
      // <div class="cell"><input type="checkbox"><input type="text" value="TAG 2"><button>DELE</button></div>
      // <div class="cell selected" data-id="3"><input type="checkbox" checked><input type="text" value="TAG 1"><button>DELE</button></div>
      var el = null;
      if (tagIndexList.indexOf(i) === -1) {
        el = $('<div class="cell" data-id="' + i + '"><input type="checkbox"><input type="text" value="' + tag + '"><button>削除</button></div>');
      } else {
        el = $('<div class="cell selected" data-id="' + i + '"><input type="checkbox" checked><input type="text" value="' + tag + '"><button>削除</button></div>')
      }
      /*
      var el = (tagIndexList.indexOf(i) === -1) ?
          $('<div class="cell" data-id="' + i + '"><input type="checkbox"><input type="text" value="' + tag + '"><button>削除</button></div>') :
          $('<div class="cell selected" data-id="' + i + '"><input type="checkbox" checked><input type="text" value="' + tag + '"><button>削除</button></div>');*/
      rowEl.append(el);
      cellCount += 1;
    });
    if (cellCount % 2 === 1) {
      rowEl.append($('<div class="cell"></div>'));
    }

    // mark selected
    $.each(tagIndexList, function(i, tagI) {
      if (Calabaza.tagList[tagI] !== null) {
        pEl.find('.tags .cell[data-id="' + tagI + '"]').addClass('selected');
      }
    });
  };

  this.show = function(imageId) {
    currentImageId = parseInt(imageId);
    var image = Calabaza.imageList[imageId];
    tagIndexList = Calabaza.tagIndexListMap[image].concat();

    // clear text box
    pEl.find('ul.group input').val('');
    // rewrite image path
    pEl.find('.table .row .cell:nth-child(1) img').attr({ 'src' : Calabaza.imagesPath + image });
    // rebuild tags table
    rebuild();

    pEl.removeClass('hidden');
    $('#overlay').removeClass('hidden');
  }
};

$(function() {
  Calabaza.main = new Calabaza.Main();
  Calabaza.confContent = new Calabaza.ConfContent();
  Calabaza.editContent = new Calabaza.EditContent();
});
