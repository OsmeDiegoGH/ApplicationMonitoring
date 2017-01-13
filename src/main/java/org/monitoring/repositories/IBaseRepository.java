package org.monitoring.repositories;

import java.util.List;
import org.bson.types.ObjectId;

public interface IBaseRepository<T> {
    public List<T> findAll();
    public T findById(String id);
    public T findById(ObjectId Id);
    public T insert(T record);
    public T updateById(String id, T record);
    public int deleteAll();
    public int deleteById(String id);
}
