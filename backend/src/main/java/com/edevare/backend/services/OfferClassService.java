package com.edevare.backend.services;



import com.edevare.shared.entitiesDTO.OfferRequestDTO;
import com.edevare.shared.entitiesDTO.OfferResponseDTO;

import java.util.List;


public interface OfferClassService {


    OfferResponseDTO createOffer(OfferRequestDTO request);

    List<OfferResponseDTO> getAllOfferClasses();

    List<OfferResponseDTO> getOffersBySubject(Long idSubject);

    List<OfferResponseDTO> getOffersByTeacher(Long teacherId);

    void deleteOffer(Long idOffer);

    //Busca ofertas donde el nombre de la materia contenga el texto especificado
    // y el nivel academico coincida(si se especifica)
    List<OfferResponseDTO> searchBySubjectAndLevel(
            String name,
            String academicLevel
    );
}
