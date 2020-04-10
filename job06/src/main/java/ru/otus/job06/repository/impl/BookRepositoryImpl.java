package ru.otus.job06.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job06.exception.ApplDbConstraintException;
import ru.otus.job06.model.Book;
import ru.otus.job06.repository.BookRepository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class BookRepositoryImpl implements BookRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public List<Book> getBookList() {
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.genre " +
                " order by b.genre.genreName, b.title", Book.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getBookListByGenre(String genre) {
        TypedQuery<Book> query = em.createQuery("select b from Book b join fetch b.genre " +
                " where lower(b.genre.genreName) = :genre " +
                " order by b.genre.genreName, b.title", Book.class);
        query.setParameter("genre", genre.toLowerCase());
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<Book> getBookListByAuthor(String authorLastName) {
        TypedQuery<Book> query = em.createQuery("select b from Book b " +
                " join fetch b.authors a join fetch b.genre " +
                " where lower(a.lastName) = :lastName " +
                " order by b.genre.genreName, b.title", Book.class);
        query.setParameter("lastName", authorLastName.toLowerCase());
        return query.getResultStream()
                .distinct()
                .collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Book> getBookById(Long bookId) {
        return Optional.ofNullable( em.find(Book.class, bookId) );
    }

    @Transactional
    @Override
    public Long addBook(Book book) {
        em.persist(book);
        return book.getBookId();
    }

    @Transactional
    @Override
    public Optional<Book> updateBook(Book book) {
        try {
            if (em.find(Book.class, book.getBookId()) != null) {
                return Optional.of(em.merge(book));
            } else {
                return Optional.empty();
            }
        } catch (PersistenceException e) {
            throw new ApplDbConstraintException("Операция запрещена, нарушается целостность данных");
        }
    }

    @Transactional
    @Override
    public int deleteBook(Long bookId) {
        Query query = em.createQuery("delete from Book b where b.bookId = :bookId");
        query.setParameter("bookId", bookId);
        try {
            return query.executeUpdate();
        } catch (PersistenceException e) {
            throw new ApplDbConstraintException("Операция запрещена, нарушается целостность данных");
        }
    }

}
