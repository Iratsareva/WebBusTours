package demo.repos;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public abstract class AbstractRepository<T> {

    @PersistenceContext
    protected EntityManager entityManager;


    @Transactional
    public T create(T entity){
        entityManager.persist(entity);
        return entity;
    }

    @Transactional
    public T findById(Class<T> entityClass, String id){
        return entityManager.find(entityClass, id);
    }

    @Transactional
    public T update (T entity){
        return entityManager.merge(entity);
    }

    @Transactional
    public void delete (T entity){
        entityManager.remove(entity);
    }

    @Transactional
    public List<T> getAll(Class<T> entityClass) {
        TypedQuery<T> query = entityManager.createQuery("from " + entityClass.getName(), entityClass);
        return query.getResultList();
    }
}
