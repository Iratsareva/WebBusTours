package demo.repositories.impl;

import demo.models.Role;
import demo.models.UserRoles;
import demo.repositories.AbstractRepository;
import demo.repositories.UserRoleRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRoleRepositoryImpl extends AbstractRepository<UserRoles> implements UserRoleRepository {
    @PersistenceContext
    private EntityManager entityManager;

    public UserRoleRepositoryImpl() {
        super(UserRoles.class);
    }

    public Optional<Role> findRoleByName(UserRoles role){
        String jpql = "SELECT r FROM Role r WHERE r.name = :role";
        TypedQuery<Role> query = entityManager.createQuery(jpql, Role.class);
        query.setParameter("role", role);
        return query.getResultStream().findFirst();
    }
}
