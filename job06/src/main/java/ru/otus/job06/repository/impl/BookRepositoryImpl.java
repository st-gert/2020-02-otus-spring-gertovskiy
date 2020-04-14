package ru.otus.job06.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job06.model.Book;
import ru.otus.job06.repository.BookRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> getBookList() {
        TypedQuery<Book> query = em.createQuery("select b from Book b " +
                " join fetch b.genre " +
                " left join fetch b.reviews " +
                " order by b.genre.genreName, b.title", Book.class);
        return query.getResultList();
    }

    @Override
    public List<Book> getBookListByGenre(String genre) {
        TypedQuery<Book> query = em.createQuery("select b from Book b " +
                " join fetch b.genre " +
                " left join fetch b.reviews " +
                " where lower(b.genre.genreName) = :genre " +
                " order by b.genre.genreName, b.title", Book.class);
        query.setParameter("genre", genre.toLowerCase());
        return query.getResultList();
    }

    @Override
    public List<Book> getBookListByAuthor(String authorLastName) {
        TypedQuery<Book> query = em.createQuery("select b from Book b " +
                " join fetch b.authors a join fetch b.genre " +
                " where lower(a.lastName) = :lastName " +
                " order by b.genre.genreName, b.title", Book.class);
        query.setParameter("lastName", authorLastName.toLowerCase());
        return query.getResultList();
    }

    @Override
    public Book getBookById(Long bookId) {
        return em.find(Book.class, bookId);
    }

    @Override
    public long addBook(Book book) {
        em.persist(book);
        return book.getBookId();
    }

    @Override
    public void updateBook(Book book) {
        em.merge(book);
    }

    @Override
    public void deleteBook(Book book) {
        Book deletedBook = em.find(Book.class, book.getBookId());
        em.remove(deletedBook);
    }

}
