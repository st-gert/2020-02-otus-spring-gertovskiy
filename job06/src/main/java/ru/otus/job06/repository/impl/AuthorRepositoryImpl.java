package ru.otus.job06.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job06.exception.ApplDbConstraintException;
import ru.otus.job06.model.Author;
import ru.otus.job06.repository.AuthorRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class AuthorRepositoryImpl implements AuthorRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public List<Author> getAuthorList() {
        TypedQuery<Author> query = em.createQuery("select a from Author a", Author.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Author> getAuthorById(Long id) {
        return Optional.ofNullable( em.find(Author.class, id) );
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Author> getAuthorByName(Author author) {
        TypedQuery<Author> query = em.createQuery(
                "select a from Author a where lower(a.firstName) = :firstName and lower(a.lastName) = :lastName",
                Author.class);
        query.setParameter("firstName", author.getFirstName().toLowerCase());
        query.setParameter("lastName", author.getLastName().toLowerCase());
        try {
            return Optional.ofNullable( query.getSingleResult() );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public Long addAuthor(Author author) {
        em.persist(author);
        return author.getAuthorId();
    }

    @Transactional
    @Override
    public int updateAuthor(Author author) throws ApplDbConstraintException {
        Query query = em.createQuery(
                "update Author a set a.firstName = :firstName, a.lastName = :lastName " +
                        " where a.authorId = :authorId");
        query.setParameter("authorId", author.getAuthorId());
        query.setParameter("firstName", author.getFirstName());
        query.setParameter("lastName", author.getLastName());
        try {
            return query.executeUpdate();
        } catch (PersistenceException e) {
            throw new ApplDbConstraintException("Операция запрещена, нарушается целостность данных");
        }
    }

    @Transactional
    @Override
    public int deleteAuthor(Long authorId) throws ApplDbConstraintException {
        Query query = em.createQuery("delete from Author a where a.authorId = :authorId");
        query.setParameter("authorId", authorId);
        try {
            return query.executeUpdate();
        } catch (PersistenceException e) {
            throw new ApplDbConstraintException("Операция запрещена, нарушается целостность данных");
        }
    }

}
