package ro.tucn.energy_mgmt_devices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.tucn.energy_mgmt_devices.model.DeviceEntity;

import java.util.Optional;
import java.util.UUID;

/**
 * Repository that supports pre-implemented CRUD operations; adapted for user
 */
@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {

}