package ro.tucn.energy_mgmt_monitoring_comm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.tucn.energy_mgmt_monitoring_comm.model.ReadingEntity;

import java.sql.Timestamp;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ReadingRepository extends JpaRepository<ReadingEntity, UUID> {

    List<ReadingEntity> findAllByDeviceIdAndTimestampBetween(UUID deviceId, Timestamp from, Timestamp to);
}
