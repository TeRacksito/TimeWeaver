package es.angelkrasimirov.biblioteca.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "books")
public class Book {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@NotNull(message = "Title is required")
	private String title;

	@ManyToOne
	@JoinColumn(name = "author_id", nullable = false)
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private Author author;

	@ManyToMany
	@JoinTable(name = "books_genres",
			joinColumns = @JoinColumn(name = "book_id"),
			inverseJoinColumns = @JoinColumn(name = "genre_id"))
	@JsonProperty(access = JsonProperty.Access.READ_ONLY)
	private List<BookGenre> genres = new ArrayList<>();

	@NotNull(message = "Unique code is required")
	@Column(unique = true)
	private String uniqueCode;

	@OneToMany(mappedBy = "book", cascade = CascadeType.ALL)
	@JsonIgnore
	private List<BookCopy> copies = new ArrayList<>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Author getAuthor() {
		return author;
	}

	public void setAuthor(Author author) {
		this.author = author;
	}

	public String getUniqueCode() {
		return uniqueCode;
	}

	public void setUniqueCode(String uniqueCode) {
		this.uniqueCode = uniqueCode;
	}

	public List<BookCopy> getCopies() {
		return copies;
	}

	public void setCopies(List<BookCopy> copies) {
		this.copies = copies;
	}

	public void addCopy(BookCopy copy) {
		this.copies.add(copy);
		copy.setBook(this);
	}

	public void removeCopy(BookCopy copy) {
		this.copies.remove(copy);
		copy.setBook(null);
	}

	public List<BookGenre> getGenres() {
		return genres;
	}

	public void setGenres(List<BookGenre> genres) {
		this.genres = genres;
	}

	public void addGenre(BookGenre genre) {
		this.genres.add(genre);
	}

	public void removeGenre(BookGenre genre) {
		this.genres.remove(genre);
	}
}