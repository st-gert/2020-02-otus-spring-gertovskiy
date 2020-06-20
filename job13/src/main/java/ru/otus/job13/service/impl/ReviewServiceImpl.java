package ru.otus.job13.service.impl;

import org.springframework.security.acls.domain.BasePermission;
import org.springframework.security.acls.domain.ObjectIdentityImpl;
import org.springframework.security.acls.domain.PrincipalSid;
import org.springframework.security.acls.model.MutableAcl;
import org.springframework.security.acls.model.MutableAclService;
import org.springframework.security.acls.model.ObjectIdentity;
import org.springframework.security.acls.model.Sid;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.job13.exception.ApplDbNoDataFoundException;
import ru.otus.job13.model.Book;
import ru.otus.job13.model.Review;
import ru.otus.job13.model.dto.ReviewDto;
import ru.otus.job13.model.security.UserEntity;
import ru.otus.job13.repository.BookRepository;
import ru.otus.job13.repository.ReviewRepository;
import ru.otus.job13.repository.UserRepository;
import ru.otus.job13.service.ReviewService;

import java.util.Optional;

@Service
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository repository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;
    private final MutableAclService aclService;

    public ReviewServiceImpl(ReviewRepository repository, BookRepository bookRepository,
                             UserRepository userRepository, MutableAclService aclService) {
        this.repository = repository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
        this.aclService = aclService;
    }

    @Override
    public ReviewDto getReviewById(Long reviewId) {
        Review review = repository.getById(reviewId);
        if (review == null) {
            throw new ApplDbNoDataFoundException();
        }
        return new ReviewDto(review);
    }

    @Override
    @Transactional
    public void addReview(long bookId, String opinion, String username) {
        Optional<Book> optionalBook = bookRepository.findById(bookId);
        Optional<UserEntity> optionalUser = userRepository.findByUsername(username);
        if (!optionalBook.isPresent() || !optionalUser.isPresent()) {
            throw new ApplDbNoDataFoundException();
        }
        UserEntity user = optionalUser.get();
        Review review = new Review(null, optionalBook.get(), opinion);
        review.setUser(user);
        Review addedReview = repository.saveAndFlush(review); // Методы сохранения здесь и в update должны быть разные!
        // Добавление прав ACL
        Sid sid = new PrincipalSid(SecurityContextHolder.getContext().getAuthentication());
        ObjectIdentity oid = new ObjectIdentityImpl(Review.class, addedReview.getId());
        MutableAcl acl = aclService.createAcl(oid);
        acl.setOwner(sid);
        acl.insertAce(acl.getEntries().size(), BasePermission.WRITE, sid, true);
        acl.insertAce(acl.getEntries().size(), BasePermission.DELETE, sid, true);
        aclService.updateAcl(acl);
    }

    @Override
    @Transactional
    public void updateReview(long reviewId, String opinion) {
        Optional<Review> optionalReview = repository.findById(reviewId);
        Review review = optionalReview.orElseThrow(ApplDbNoDataFoundException::new);
        review.setOpinion(opinion);
        repository.save(review);
    }

    @Override
    @Transactional
    public void deleteReview(long reviewId) {
        if (!repository.existsById(reviewId)) {
            throw new ApplDbNoDataFoundException();
        }
        repository.deleteById(reviewId);
        // Удаление прав ACL
        ObjectIdentity oid = new ObjectIdentityImpl(Review.class, reviewId);
        aclService.deleteAcl(oid, true);
    }

}
