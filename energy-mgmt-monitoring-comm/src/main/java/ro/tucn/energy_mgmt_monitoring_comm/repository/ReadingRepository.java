package ro.tucn.energy_mgmt_monitoring_comm.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ro.tucn.energy_mgmt_monitoring_comm.model.ReadingEntity;

import java.util.UUID;

@Repository
public interface ReadingRepository extends JpaRepository<ReadingEntity, UUID> {
}
