package org.example.app.services;

import java.util.List;

public interface ProjectRepository<T> {
    List<T> retreiveAll();

    void store(T book);

    boolean removeItemById(Integer bookIdToRemove);

    boolean removeBookByAuthor(String author);

    boolean removeBookByTitle(String title);

    boolean removeBookBySize(Integer size);
}
