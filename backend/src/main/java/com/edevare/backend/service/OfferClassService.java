package com.edevare.backend.service;

import com.edevare.backend.model.OfferClass;
import com.edevare.shared.entitiesDTO.OfferRequestDTO;
import com.edevare.shared.entitiesDTO.OfferResponseDTO;

import java.util.List;

public interface OfferClassService {


    OfferResponseDTO createOffer(OfferRequestDTO request);

    List<OfferResponseDTO> getAllOfferClasses();

    List<OfferResponseDTO> getOffersBySubject(Long idSubject);

    List<OfferResponseDTO> getOffersByTeacher(Long teacherId);

    void deleteOffer(Long idOffer);
}
