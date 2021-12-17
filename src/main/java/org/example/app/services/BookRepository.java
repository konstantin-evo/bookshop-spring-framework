package org.example.app.services;

import org.apache.log4j.Logger;
import org.example.web.dto.Book;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@Repository
public class BookRepository implements ProjectRepository<Book>, ApplicationContextAware {

    private final Logger logger = Logger.getLogger(BookRepository.class);
    private final NamedParameterJdbcTemplate jdbcTemplate;

    private ApplicationContext context;

    public BookRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public List<Book> retrieveAll() {
        List<Book> books = jdbcTemplate.query("SELECT * FROM books", (ResultSet res, int rowNum)->{
            Book book = new Book();
            book.setId(res.getInt("id"));
            book.setAuthor(res.getString("author"));
            book.setTitle(res.getString("title"));
            book.setSize(res.getInt("size"));
            return book;
        });
        return new ArrayList<>(books);
    }

    @Override
    public void store(Book book) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", book.getAuthor());
        parameterSource.addValue("title", book.getTitle());
        parameterSource.addValue("size", book.getSize());
        jdbcTemplate.update("INSERT INTO books(author,title,size) VALUES(:author, :title, :size)", parameterSource);
        logger.info("store new book: " + book);
    }

    @Override
    public boolean removeItemById(Integer bookIdToRemove) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("id", bookIdToRemove);
        jdbcTemplate.update("DELETE FROM books WHERE id = :id", parameterSource);
        logger.info("remove book by id completed");
        return true;
    }

    @Override
    public boolean removeBookByAuthor(String author) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("author", author);
        jdbcTemplate.update("DELETE FROM books WHERE author = :author", parameterSource);
        logger.info("remove book by author completed");
        return true;
    }

    @Override
    public boolean removeBookByTitle(String title) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("title", title);
        jdbcTemplate.update("DELETE FROM books WHERE title = :title", parameterSource);
        logger.info("remove book by title completed");
        return true;
    }

    @Override
    public boolean removeBookBySize(Integer size) {
        MapSqlParameterSource parameterSource = new MapSqlParameterSource();
        parameterSource.addValue("size", size);
        jdbcTemplate.update("DELETE FROM books WHERE size = :size", parameterSource);
        logger.info("remove book by size completed");
        return true;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.context = applicationContext;
    }
}
