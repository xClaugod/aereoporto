package com.aereoporto.repository;

import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import com.aereoporto.model.Airplane;

public interface AirplaneRepository extends JpaRepository<Airplane, String>, JpaSpecificationExecutor<Airplane> {
    Optional<Airplane> findById(String id);

    void deleteById(String id);
}
