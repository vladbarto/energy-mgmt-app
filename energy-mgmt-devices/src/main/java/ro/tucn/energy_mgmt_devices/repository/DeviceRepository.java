package ro.tucn.energy_mgmt_devices.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ro.tucn.energy_mgmt_devices.model.DeviceEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Repository that supports pre-implemented CRUD operations; adapted for user
 */
@Repository
public interface DeviceRepository extends JpaRepository<DeviceEntity, UUID> {
}