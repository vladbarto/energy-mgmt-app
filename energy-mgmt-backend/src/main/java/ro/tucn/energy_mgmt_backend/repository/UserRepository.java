package ro.tucn.energy_mgmt_backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.tucn.energy_mgmt_backend.model.UserEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository that supports pre-implemented CRUD operations; adapted for user
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity, UUID> {

    /**
     * Method that finds a user by given username
     * @param Username - a given username from the client
     * @return either UserEntity (fetched from DB) if successful, or null if failure
     */
    Optional<UserEntity> findByUsername(String Username);
}