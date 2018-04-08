package fi.lokaali.LokaaliBackend;

/**
 * Created by possumunnki on 1.4.2018.
 */
public interface Repository<T,ID> {
    public T saveEntity(T entity);
    public void delete(ID id) throws IllegalArgumentException;
    public Iterable<T> findAll();
    public T findOne(ID id);
}
