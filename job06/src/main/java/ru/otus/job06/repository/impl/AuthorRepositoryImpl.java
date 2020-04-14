package ru.otus.job06.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job06.exception.ApplDbConstraintException;
import ru.otus.job06.model.Author;
import ru.otus.job06.repository.AuthorRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class AuthorRepositoryImpl implements AuthorRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Author> getAuthorList() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Override
    public Author getAuthorById(long id) {
        return em.find(Author.class, id);
    }

    // Используется в BookService для заполнения Book
    @Override
    public Author getAuthorByName(Author author) {
        TypedQuery<Author> query = em.createQuery(
                "select a from Author a where lower(a.firstName) = :firstName and lower(a.lastName) = :lastName",
                Author.class);
        query.setParameter("firstName", author.getFirstName().toLowerCase());
        query.setParameter("lastName", author.getLastName().toLowerCase());
        try {
            return query.getSingleResult();
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public long addAuthor(Author author) {
        em.persist(author);
        return author.getAuthorId();
    }

    @Override
    public void updateAuthor(Author author) throws ApplDbConstraintException {
        em.merge(author);
    }

    @Override
    public void deleteAuthor(Author author) throws ApplDbConstraintException {
        Author deletedAuthor = em.find(Author.class, author.getAuthorId());
        em.remove(deletedAuthor);
    }

}
