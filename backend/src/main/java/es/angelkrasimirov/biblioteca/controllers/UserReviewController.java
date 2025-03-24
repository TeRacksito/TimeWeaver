package es.angelkrasimirov.biblioteca.controllers;

import es.angelkrasimirov.biblioteca.models.Book;
import es.angelkrasimirov.biblioteca.models.UserReview;
import es.angelkrasimirov.biblioteca.services.UserReviewService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class UserReviewController {

	@Autowired
	private UserReviewService userReviewService;

	@GetMapping("/user-reviews/{userReviewId}")
	public ResponseEntity<UserReview> getUserReviewById(@PathVariable Long userReviewId) {
		UserReview userReview = userReviewService.getUserReviewById(userReviewId);

		if (userReview == null) {
			return ResponseEntity.notFound().build();
		}

		return ResponseEntity.ok(userReview);
	}

	@GetMapping("/books/{bookId}/user-reviews")
	public ResponseEntity<List<UserReview>> getUserReviewsByBook(@PathVariable Long bookId) {
		return ResponseEntity.ok(userReviewService.getUserReviewsByBook(bookId));
	}

	@GetMapping("/users/{userId}/user-reviews")
	public ResponseEntity<List<UserReview>> getUserReviewsByUser(@PathVariable Long userId) {
		return ResponseEntity.ok(userReviewService.getUserReviewsByUser(userId));
	}

	@PreAuthorize("hasRole('USER') and #userId == authentication.principal.id")
	@PostMapping("/books/{bookId}/users/{userId}/user-reviews")
	public ResponseEntity<UserReview> createUserReview(
			@PathVariable Long bookId,
			@PathVariable Long userId,
			@Valid @RequestBody UserReview userReview
	) {
		try {

			return ResponseEntity.status(HttpStatus.CREATED).body(userReviewService.associateUserToBook(bookId, userId, userReview));
		} catch (NoResourceFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasRole('USER') and #userId == authentication.principal.id")
	@PutMapping("/user-reviews/{userReviewId}")
	public ResponseEntity<UserReview> updateUserReview(
			@PathVariable Long userReviewId,
			@Valid @RequestBody UserReview userReview
	) {
		try {
			return ResponseEntity.ok(userReviewService.updateUserReview(userReviewId, userReview));
		} catch (NoResourceFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PreAuthorize("hasRole('USER') and #userId == authentication.principal.id")
	@DeleteMapping("/user-reviews/{userReviewId}")
	public ResponseEntity<Void> deleteUserReview(
			@PathVariable Long userReviewId
	) {
		try {
			userReviewService.deleteUserReview(userReviewId);
			return ResponseEntity.ok().build();
		} catch (NoResourceFoundException e) {
			return ResponseEntity.noContent().build();
		}
	}
}
