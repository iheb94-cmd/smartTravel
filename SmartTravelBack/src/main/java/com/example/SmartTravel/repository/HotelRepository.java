
package com.example.SmartTravel.repository;

import com.example.SmartTravel.entity.Hotel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HotelRepository extends JpaRepository<Hotel, Long> {

    @Query("""
            SELECT h FROM Hotel h
            WHERE LOWER(h.nom) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(h.pays) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(h.ville) LIKE LOWER(CONCAT('%', :q, '%'))
               OR LOWER(h.typeHebergement) LIKE LOWER(CONCAT('%', :q, '%'))
            ORDER BY h.nom
            """)
    List<Hotel> search(@Param("q") String q);
}
