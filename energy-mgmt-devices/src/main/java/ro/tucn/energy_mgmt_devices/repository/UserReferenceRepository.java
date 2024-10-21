package ro.tucn.energy_mgmt_devices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.tucn.energy_mgmt_devices.model.UserReferenceEntity;

import java.util.UUID;

@Repository
public interface UserReferenceRepository extends JpaRepository<UserReferenceEntity, UUID> {

}
