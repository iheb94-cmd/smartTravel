package com.example.SmartTravel.service;

import com.example.SmartTravel.entity.Offer;
import com.example.SmartTravel.repository.OfferRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfferService {

    @Autowired
    private OfferRepository offerRepository;

    public List<Offer> getAllOffers() {
        return offerRepository.findAll();
    }

    public Optional<Offer> getOfferById(Long id) {
        return offerRepository.findById(id);
    }

    public Offer createOffer(Offer offer) {
        return offerRepository.save(offer);
    }

    public Offer updateOffer(Long id, Offer offerDetails) {
        return offerRepository.findById(id).map(existingOffer -> {
            existingOffer.setType(offerDetails.getType());
            existingOffer.setTitle(offerDetails.getTitle());
            existingOffer.setDatetime(offerDetails.getDatetime());
            existingOffer.setPrice(offerDetails.getPrice());
            return offerRepository.save(existingOffer);
        }).orElseThrow(() -> new RuntimeException("Offre non trouvée avec l'id : " + id));
    }

    public boolean deleteOffer(Long id) {
        if (offerRepository.existsById(id)) {
            offerRepository.deleteById(id);
            return true;
        }
        return false;
    }
}