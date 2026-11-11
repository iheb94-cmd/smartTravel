package com.example.SmartTravel.repository;

import com.example.SmartTravel.entity.Vol;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface VolRepository extends JpaRepository<Vol, Long> {

    @Query("""
            select v from Vol v
            where lower(v.numeroVol) like lower(concat('%', :q, '%'))
               or lower(v.villeDepart) like lower(concat('%', :q, '%'))
               or lower(v.villeArrivee) like lower(concat('%', :q, '%'))
               or lower(v.compagnie) like lower(concat('%', :q, '%'))
            order by v.dateDepart
            """)
    List<Vol> search(@Param("q") String q);
}