package ru.otus.job06.repository.impl;

import org.springframework.stereotype.Repository;
import ru.otus.job06.model.Book;
import ru.otus.job06.repository.BookRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Book> getBookList() {
        TypedQuery<Book> query = em.createQuery("select b from Book b " +
                " join fetch b.genre " +
                " left join fetch b.reviews "
                , Book.class);
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
