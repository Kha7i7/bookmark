// 添加书签
function addBookmark() {
    const formData = {
        title: $('#title').val(),
        url: $('#url').val(),
        description: $('#description').val(),
        tags: $('#tags').val()
    };

    $.post('/bookmark/add', formData, function(result) {
        alert(result);
        if (result === '添加成功！') {
            $('#addModal').modal('hide');
            location.reload();
        }
    });
}

// 编辑书签
function editBookmark(id) {
    $.getJSON('/bookmark/api/detail/' + id)
        .done(function(response) {
            if (!response.success) {
                alert('加载失败：' + response.message);
                if (response.message === '未登录') {
                    window.location.href = '/user/login';
                }
                return;
            }

            $('#edit-id').val(response.id);
            $('#edit-title').val(response.title);
            $('#edit-url').val(response.url);
            $('#edit-description').val(response.description || '');
            $('#edit-tags').val(response.tags || '');

            $('#editModal').modal('show');
        })
        .fail(function(xhr) {
            alert('网络错误：' + xhr.status);
        });
}

// 保存编辑
function saveEdit() {
    const formData = {
        id: $('#edit-id').val(),
        title: $('#edit-title').val(),
        url: $('#edit-url').val(),
        description: $('#edit-description').val(),
        tags: $('#edit-tags').val()
    };

    $.post('/bookmark/edit', formData, function(result) {
        alert(result);
        if (result === '修改成功！') {
            $('#editModal').modal('hide');
            location.reload();
        }
    });
}

// 删除书签
function deleteBookmark(id) {
    if (confirm('确定要删除此书签吗？')) {
        $.post('/bookmark/delete/' + id, function(result) {
            alert(result);
            if (result === '删除成功！') {
                location.reload();
            }
        });
    }
}

// 分页设置
function changePageSize() {
    const pageSize = $('#pageSizeSelect').val();
    const url = new URL(window.location.href);
    url.searchParams.set('pageSize', pageSize);
    url.searchParams.set('pageNum', '1');
    window.location.href = url.toString();
}