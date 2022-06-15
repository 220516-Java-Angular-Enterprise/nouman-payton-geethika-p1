package com.revature.ers.daos;

import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> {
    void save(T obj);

    void update(T obj);

    void delete(String id);

    T getById(String id) throws SQLException;

    List<T> getAll();
}
