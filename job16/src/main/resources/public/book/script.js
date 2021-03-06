
function loadTable (books) {
    $('tbody').children().remove();
    books.forEach(function(book) {
        $('tbody').append(`
                    <tr>
                        <td>${book.bookId}</td>
                        <td>${book.genreName}</td>
                        <td>${book.authorsString}</td>
                        <td>${book.title}</td>
                        <td><a href="../review/list.html?id=${book.bookId}">Отзывы</a></td>
                        <td><a href="edit.html?id=${book.bookId}">Изменить</a></td>
                        <td><a class="delete" href="#" id="${book.bookId}">Удалить</a></td>
                    </tr>
                    `)
    });
    $('.delete').on('click', function() {
        deleteBook(this.id);
        return false;
    });
}

function deleteBook(id) {
    if (confirm(`Подтвердите удаление книги ID ${id}`)) {
        $.ajax(`/api/book/${id}`, {
            type: 'DELETE',
            success: function () {
                loadList();         // Обновление таблицы
            },
            error: function (jqXHR) {
                if ((jqXHR.status === 404) && (jqXHR.responseJSON)) {
                    alert(jqXHR.responseJSON.message);
                } else {
                    alert(`Ошибка, статус ${jqXHR.status}`)
                }
            }
        })
    }
}

function loadGenres() {
    $.get('/api/genre').done(function (response) {
        $('#genre-input').children().remove();
        response._embedded.genres.forEach(function (genre) {
            $('#genre-input').append(`
                <option value="${genre.genreName}">${genre.genreName}</option>
            `)
        });
    })
}

function loadAuthors() {
    $.get('/api/author').done(function (authors) {
        $('#checkboxes').children().remove();
        authors.forEach(function (author) {
            $('#checkboxes').append(`
                <input id="${author.authorId}" type="checkbox" value="${author.authorId}">
                <label class="lab-wide" for="${author.authorId}">${author.firstName} ${author.lastName}</label>
                <br>
            `)
        });
    })
}
