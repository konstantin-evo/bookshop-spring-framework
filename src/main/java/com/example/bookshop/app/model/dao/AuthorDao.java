package com.example.bookshop.app.model.dao;

import com.example.bookshop.app.model.entity.Author;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Repository;

import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;


@Repository
public class AuthorDao {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public AuthorDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, JdbcTemplate jdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        this.jdbcTemplate = jdbcTemplate;
    }

    public List<Author> getAll() {
        List<Author> authors = jdbcTemplate.query("SELECT * FROM authors", (ResultSet rs, int rownum)->{
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setName(rs.getString("first_name"));
            author.setSurname(rs.getString("last_name"));
            return author;
        });
        return new ArrayList<>(authors);
    }

    public Author getAuthorByBookId(int id) {
        String sql = "SELECT a.id, a.last_name, a.first_name " +
                "FROM authors AS a JOIN books ON books.author_id = a.id " +
                "WHERE books.id = :id";
        SqlParameterSource namedParameters = new MapSqlParameterSource().addValue("id", id);
        return namedParameterJdbcTemplate.queryForObject(sql, namedParameters, (ResultSet rs, int rownum) -> {
            Author author = new Author();
            author.setId(rs.getInt("id"));
            author.setName(rs.getString("first_name"));
            author.setSurname(rs.getString("last_name"));
            return author;
        });
    }

}
