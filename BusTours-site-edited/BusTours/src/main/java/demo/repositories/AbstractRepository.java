package demo.repositories;

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

    private Class<T> entityClass;
    public AbstractRepository(Class<T> entityClass) {
        this.entityClass = entityClass;
    }

    @Transactional
    public T create(T entity){
        entityManager.persist(entity);
        return entity;
    }

    public T findById(String id){
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

    public List<T> findAll() {
        TypedQuery<T> query = entityManager.createQuery("from " + entityClass.getName(), entityClass);
        return query.getResultList();
    }
}
