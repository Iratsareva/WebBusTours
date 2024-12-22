package demo.repos.impl;

import demo.models.Role;
import demo.models.Trip;
import demo.models.UserRoles;
import demo.repos.AbstractRepository;
import demo.repos.UserRoleRepository;
import jakarta.persistence.TypedQuery;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class UserRoleRepositoryImpl extends AbstractRepository<UserRoles> implements UserRoleRepository {

    public Optional<Role> findRoleByName(UserRoles role){
        String jpql = "SELECT r FROM Role r WHERE r.name = :role";
        TypedQuery<Role> query = entityManager.createQuery(jpql, Role.class);
        query.setParameter("role", role);

        return query.getResultStream().findFirst();


    }
}
