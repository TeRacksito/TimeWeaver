package es.angelkrasimirov.biblioteca.repositories;

import es.angelkrasimirov.biblioteca.models.UserReview;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserReviewRepository extends JpaRepository<UserReview, Long> {
	List<UserReview> findByBookId(Long bookId);

	List<UserReview> findByUserId(Long userId);
}
