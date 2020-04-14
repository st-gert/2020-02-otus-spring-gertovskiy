package ru.otus.job06.repository.impl;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job06.model.Genre;
import ru.otus.job06.repository.GenreRepository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
@Transactional
public class GenreRepositoryImpl implements GenreRepository {

    @PersistenceContext
    private EntityManager em;

    @Override
    public List<Genre> getGenreList() {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g", Genre.class);
        return query.getResultList();
    }

    @Override
    public Genre getGenreById(Long id) {
        return  em.find(Genre.class, id);
    }

    // Используется в BookService для заполнения Book
    @Override
    public Genre getGenreByName(String genreName) {
        TypedQuery<Genre> query = em.createQuery("select g from Genre g where lower(g.genreName) = :genreName",
                Genre.class);
        query.setParameter("genreName", genreName.toLowerCase());
        try {
            return query.getSingleResult() ;
        } catch (NoResultException e) {
            return null;
        }
    }

    @Override
    public long addGenre(Genre genre) {
        em.persist(genre);
        return genre.getGenreId();
    }

    @Override
    public void updateGenre(Genre genre) {
        em.merge(genre);
    }

    @Override
    public void deleteGenre(Genre genre) {
        Genre deletedGenre = em.find(Genre.class, genre.getGenreId());
        em.remove(deletedGenre);
    }
}
