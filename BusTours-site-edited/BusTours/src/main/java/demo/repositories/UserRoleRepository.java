package demo.repositories;

import demo.models.Role;
import demo.models.UserRoles;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface UserRoleRepository  {
    Optional<Role> findRoleByName(UserRoles role);
}