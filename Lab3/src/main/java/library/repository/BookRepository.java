package library.repository;

import library.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BookRepository extends JpaRepository<Book, Integer> {

    @Query("SELECT b FROM Book b JOIN FETCH b.author ORDER BY b.title")
    List<Book> findAllWithAuthor();

    @Query("SELECT b FROM Book b JOIN FETCH b.author WHERE b.author.id = :authorId ORDER BY b.title")
    List<Book> findByAuthorId(@Param("authorId") Integer authorId);
}
