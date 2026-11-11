
package com.example.SmartTravel.service;

import com.example.SmartTravel.dto.HotelDto;
import com.example.SmartTravel.entity.Hotel;
import com.example.SmartTravel.entity.User;
import com.example.SmartTravel.exception.NotFoundException;
import com.example.SmartTravel.mapper.HotelMapper;
import com.example.SmartTravel.repository.HotelRepository;
import com.example.SmartTravel.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HotelService {

    private final HotelRepository hotelRepository;
    private final UserRepository userRepository;

    // Afficher tous les hôtels ou rechercher
    public List<HotelDto> findAll(String q) {

        List<Hotel> hotels = (q == null || q.isBlank())
                ? hotelRepository.findAll(Sort.by("nom"))
                : hotelRepository.search(q.trim());

        return hotels.stream()
                .map(HotelMapper::toDto)
                .toList();
    }

    // Afficher un hôtel par son ID
    public HotelDto findById(Long id) {
        return HotelMapper.toDto(getOrThrow(id));
    }

    // Ajouter un hôtel
    public HotelDto create(HotelDto dto, String agentEmail) {

        User agent = userRepository.findByEmail(agentEmail)
                .orElseThrow(() ->
                        new NotFoundException("Agent introuvable"));

        Hotel hotel = new Hotel();

        HotelMapper.copyToEntity(dto, hotel);

        // Associer l'hôtel à l'agent connecté
        hotel.setAgent(agent);

        return HotelMapper.toDto(
                hotelRepository.save(hotel)
        );
    }

    // Modifier un hôtel
    public HotelDto update(Long id, HotelDto dto) {

        Hotel hotel = getOrThrow(id);

        HotelMapper.copyToEntity(dto, hotel);

        // L'agent d'origine reste inchangé
        return HotelMapper.toDto(
                hotelRepository.save(hotel)
        );
    }

    // Supprimer un hôtel
    public void delete(Long id) {
        hotelRepository.delete(getOrThrow(id));
    }

    // Vérifier l'existence de l'hôtel
    private Hotel getOrThrow(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Hôtel introuvable"));
    }
}
