package demo.repos;

import demo.models.Role;
import demo.models.UserRoles;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


import java.util.Optional;

@Repository
public interface UserRoleRepository  {
    Optional<Role> findRoleByName(UserRoles role);
}
