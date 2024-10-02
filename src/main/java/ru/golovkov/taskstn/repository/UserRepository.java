package ru.golovkov.taskstn.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.golovkov.taskstn.model.entity.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query("SELECT u FROM User u WHERE " +
            "u.fio LIKE CONCAT(:word, ' %') " +
            "OR u.fio LIKE CONCAT('% ', :word, ' %') " +
            "OR u.email LIKE %:word%")
    List<User> findBySingleWord(@Param("word") String word, Pageable pageable);

    @Query("SELECT u FROM User u WHERE " +
            "(u.fio LIKE CONCAT(:word1, ' %') " +
            "AND u.email LIKE %:word2%) " +
            "OR (u.fio LIKE CONCAT('% ', :word1, ' %') " +
            "AND u.email LIKE %:word2%)")
    List<User> findByTwoWords(@Param("word1") String word1, @Param("word2") String word2, Pageable pageable);

    Optional<User> findByEmail(String email);

    List<User> findAllByEmailIn(List<String> emails);
}
