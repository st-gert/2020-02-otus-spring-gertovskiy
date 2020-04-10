package ru.otus.job06.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job06.exception.ApplDbConstraintException;
import ru.otus.job06.model.Genre;
import ru.otus.job06.repository.GenreRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.PersistenceException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;
import java.util.Optional;

@Repository
public class GenreRepositoryImpl implements GenreRepository {

    @PersistenceContext
    private EntityManager em;

    @Transactional(readOnly = true)
    @Override
    public List<Genre> getGenreList() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Genre> getGenreById(Long id) {
        return Optional.ofNullable( em.find(Genre.class, id) );
    }

    @Transactional(readOnly = true)
    @Override
    public Optional<Genre> getGenreByName(String genreName) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where lower(g.genreName) = :genreName",
                Genre.class);
        query.setParameter("genreName", genreName.toLowerCase());
        try {
            return Optional.ofNullable( query.getSingleResult() );
        } catch (NoResultException e) {
            return Optional.empty();
        }
    }

    @Transactional
    @Override
    public long addGenre(Genre genre) {
        em.persist(genre);
        return genre.getGenreId();
    }

    @Transactional
    @Override
    public int updateGenre(Genre genre) {
        Query query = em.createQuery("update Genre g set g.genreName = :genreName where g.genreId = :genreId");
        query.setParameter("genreName", genre.getGenreName());
        query.setParameter("genreId", genre.getGenreId());
        try {
            return query.executeUpdate();
        } catch (PersistenceException e) {
            throw new ApplDbConstraintException("Операция запрещена, нарушается целостность данных");
        }
    }

    @Transactional
    @Override
    public int deleteGenre(Long genreId) {
        Query query = em.createQuery("delete from Genre g where g.genreId = :genreId");
        query.setParameter("genreId", genreId);
        try {
            return query.executeUpdate();
        } catch (PersistenceException e) {
            throw new ApplDbConstraintException("Операция запрещена, нарушается целостность данных");
        }
    }
}
