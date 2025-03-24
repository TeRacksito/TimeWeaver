package es.angelkrasimirov.biblioteca.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name = "user_reviews")
public class UserReview {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "Review is required")
	private String review;

	@NotNull(message = "Rating is required")
	@Max(value = 5, message = "Rating must be between 1 and 5")
	@Min(value = 1, message = "Rating must be between 1 and 5")
	private Integer rating;

	@ManyToOne
	@JoinColumn(name = "book_id")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Book book;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private User user;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getReview() {
		return review;
	}

	public void setReview(String review) {
		this.review = review;
	}

	public int getRating() {
		return rating;
	}

	public void setRating(int rating) {
		this.rating = rating;
	}

	public Book getBook() {
		return book;
	}

	public void setBook(Book book) {
		this.book = book;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}
}
