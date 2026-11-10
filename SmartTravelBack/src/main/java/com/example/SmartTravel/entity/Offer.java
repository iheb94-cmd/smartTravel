package com.example.SmartTravel.entity;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Audited;

@Data
@Entity
@Table(name = "offers")
public class Offer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String type;
    private String title;
    private String datetime;
    private String price;
}
