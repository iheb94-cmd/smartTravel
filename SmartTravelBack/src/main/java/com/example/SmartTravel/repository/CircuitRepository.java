
package com.example.SmartTravel.repository;

import com.example.SmartTravel.entity.Circuit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CircuitRepository extends JpaRepository<Circuit, Long> {

    @Query("""
        SELECT c FROM Circuit c
        WHERE LOWER(c.titre) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(c.pays) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(c.villeDepart) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(c.typeCircuit) LIKE LOWER(CONCAT('%', :q, '%'))
        OR LOWER(c.destinations) LIKE LOWER(CONCAT('%', :q, '%'))
        ORDER BY c.titre ASC
    """)
    List<Circuit> search(@Param("q") String q);
}
