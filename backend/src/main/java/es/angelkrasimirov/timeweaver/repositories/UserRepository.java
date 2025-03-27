package es.angelkrasimirov.timeweaver.repositories;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import es.angelkrasimirov.timeweaver.models.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

	Optional<User> findByUsername(String username);

	@Query(value = "SELECT u.* FROM users u " +
			"LEFT JOIN book_loan_history blh ON u.id = blh.user_id " +
			"WHERE (blh.loan_date BETWEEN CURDATE() - INTERVAL 1 YEAR AND " +
			"CURDATE() + INTERVAL 1 DAY - INTERVAL 1 MICROSECOND) " +
			"GROUP BY u.id ORDER BY COUNT(blh.id) DESC", nativeQuery = true)
	List<User> findAllUsersOrderedByLoans();
}
