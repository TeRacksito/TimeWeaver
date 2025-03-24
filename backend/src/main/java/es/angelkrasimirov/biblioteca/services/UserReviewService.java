package es.angelkrasimirov.biblioteca.services;

import es.angelkrasimirov.biblioteca.models.Book;
import es.angelkrasimirov.biblioteca.models.User;
import es.angelkrasimirov.biblioteca.models.UserReview;
import es.angelkrasimirov.biblioteca.repositories.UserReviewRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Service
public class UserReviewService {

	@Autowired
	private UserReviewRepository userReviewRepository;

	@Autowired
	private UserService userService;

	@Autowired
	private BookService bookService;

	public UserReview getUserReviewById(Long id) {
		return userReviewRepository.findById(id).orElse(null);
	}

	public List<UserReview> getUserReviewsByBook(Long bookId) {
		return userReviewRepository.findByBookId(bookId);
	}

	public List<UserReview> getUserReviewsByUser(Long userId) {
		return userReviewRepository.findByUserId(userId);
	}

	public UserReview saveUserReview(UserReview userReview) {
		return userReviewRepository.save(userReview);
	}

	public UserReview associateUserToBook(Long bookId, Long userId, UserReview userReview) throws NoResourceFoundException {
		User user = userService.getUserById(userId);

		if (user == null) {
			throw new NoResourceFoundException(HttpMethod.POST, "No user found with id " + userId);
		}

		Book book = bookService.getBookById(bookId);

		if (book == null) {
			throw new NoResourceFoundException(HttpMethod.POST, "No book found with id " + bookId);
		}

		userReview.setUser(user);
		userReview.setBook(book);

		return saveUserReview(userReview);

	}

	public UserReview updateUserReview(Long userReviewId, @Valid UserReview userReview) throws NoResourceFoundException {
		UserReview existingUserReview = getUserReviewById(userReviewId);

		if (existingUserReview == null) {
			throw new NoResourceFoundException(HttpMethod.PUT, "No user review found with id " + userReviewId);
		}

		existingUserReview.setRating(userReview.getRating());
		existingUserReview.setReview(userReview.getReview());

		return saveUserReview(existingUserReview);
	}

	public void deleteUserReview(Long userReviewId) throws NoResourceFoundException {
		if (!userReviewRepository.existsById(userReviewId)) {
			throw new NoResourceFoundException(HttpMethod.DELETE, "No user review found with id " + userReviewId);
		}
		userReviewRepository.deleteById(userReviewId);
	}
}
