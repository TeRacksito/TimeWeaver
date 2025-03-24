package es.angelkrasimirov.biblioteca.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;

@Entity
@Table(name = "book_loan_history")
public class BookLoanHistory {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@ManyToOne
	@JoinColumn(name = "book_id")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Book book;

	@ManyToOne
	@JoinColumn(name = "user_id")
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private User user;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime loanDate;

	@NotNull(message = "Due date is required")
	private LocalDateTime dueDate;

	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private LocalDateTime actualReturnDate;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public LocalDateTime getLoanDate() {
		return loanDate;
	}

	public void setLoanDate(LocalDateTime loanDate) {
		this.loanDate = loanDate;
	}

	public LocalDateTime getDueDate() {
		return dueDate;
	}

	public void setDueDate(LocalDateTime dueDate) {
		this.dueDate = dueDate;
	}

	public LocalDateTime getActualReturnDate() {
		return actualReturnDate;
	}

	public void setActualReturnDate(LocalDateTime actualReturnDate) {
		this.actualReturnDate = actualReturnDate;
	}
}
