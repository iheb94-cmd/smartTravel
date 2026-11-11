
package com.example.SmartTravel.repository;

import com.example.SmartTravel.entity.Offre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OffreRepository extends JpaRepository<Offre, Long> {

    List<Offre> findByAgent_Email(String email);

}
