package es.angelkrasimirov.biblioteca.services;

import es.angelkrasimirov.biblioteca.models.Author;
import es.angelkrasimirov.biblioteca.repositories.AuthorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import java.util.List;

@Service
public class AuthorService {

	@Autowired
	private AuthorRepository authorRepository;

	public List<Author> getAllAuthors() {
		return authorRepository.findAll();
	}

	public Author getAuthorById(Long id) {
		return authorRepository.findById(id).orElse(null);
	}

	public Author saveAuthor(Author author) {
		return authorRepository.save(author);
	}

	public Author updateAuthor(Long id, Author author) throws NoResourceFoundException {
		Author existingAuthor = getAuthorById(id);
		if (existingAuthor == null) {
			throw new NoResourceFoundException(HttpMethod.PUT, "No author found with id " + id);
		}

		author.setId(id);

		return saveAuthor(author);
	}

	public void deleteAuthor(Long id) throws NoResourceFoundException {
		if (!authorRepository.existsById(id)) {
			throw new NoResourceFoundException(HttpMethod.DELETE, "No author found with id " + id);
		}
		authorRepository.deleteById(id);
	}
}
