package com.edevare.backend.service;

import com.edevare.backend.exceptions.SubjectNotFoundException;
import com.edevare.backend.exceptions.TeacherExistException;
import com.edevare.backend.model.OfferClass;
import com.edevare.backend.model.Subject;
import com.edevare.backend.model.TeacherProfile;
import com.edevare.backend.repository.OfferClassRepository;
import com.edevare.backend.repository.SubjectRepository;
import com.edevare.backend.repository.TeacherProfileRepository;
import com.edevare.backend.service.impl.OfferClassServiceImpl;
import com.edevare.shared.entitiesDTO.OfferRequestDTO;
import com.edevare.shared.entitiesDTO.OfferResponseDTO;

import com.edevare.shared.enums.OfferClassState;
import com.edevare.shared.enums.TeacherModality;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class OfferClassServiceImplTest {

    //Clase a usar
    private OfferClassServiceImpl offerClassService;

    //Dependecias a simular
    @Mock
    private TeacherProfileRepository teacherProfileRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private OfferClassRepository offerClassRepository;

    private TeacherProfile teacherProfile;
    private Subject subject;
    private OfferClass offer;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        offerClassService = new OfferClassServiceImpl(offerClassRepository, teacherProfileRepository, subjectRepository);

        teacherProfile = new TeacherProfile();
        teacherProfile.setId(1L);
        teacherProfile.setHourlyRate(BigDecimal.valueOf(20));
        teacherProfile.setModality(TeacherModality.HYBRID);

        subject = new Subject();
        subject.setIdSubject(1L);
        subject.setName("Matematicas");
        subject.setAcademicLevel("Bachillerato");

    }


    //Test de creación de oferta de clase exitosa
    @Test
    void createOfferClassSucceed() {

        TeacherProfile teacherProfile = new TeacherProfile();
        teacherProfile.setId(10L);
        teacherProfile.setHourlyRate(BigDecimal.valueOf(20));
        teacherProfile.setModality(TeacherModality.HYBRID);

        Subject subject = new Subject();
        subject.setIdSubject(1L);
        subject.setName("Matematicas");
        subject.setAcademicLevel("Bachillerato");

        OfferClass savedOfferClass = new OfferClass();
        savedOfferClass.setIdOfferClass(subject.getIdSubject());
        savedOfferClass.setTeacher(teacherProfile);
        savedOfferClass.setSubject(subject);
        savedOfferClass.setState(OfferClassState.ACTIVE);
        savedOfferClass.setPriceClass(BigDecimal.valueOf(23));
        savedOfferClass.setTitleClass("Clase de matematicas");
        savedOfferClass.setDescription("Matematicas aplicadas");


        long idTeacher = teacherProfile.getId();
        long idSubject = subject.getIdSubject();
        String description = savedOfferClass.getDescription();

        //Preparar los datos que recibe
        OfferRequestDTO requestDTO = new OfferRequestDTO(
                idTeacher,
                idSubject,
                description

        );

        //Simular el comportamiento que haran los mocks
        when(teacherProfileRepository.findById(anyLong()))
                .thenReturn(Optional.of(teacherProfile));

        when(subjectRepository.findById(anyLong()))
                .thenReturn(Optional.of(subject));

        // Simular que el repositorio guarda y devuelve el objeto correctamente
        when(offerClassRepository.save(any(OfferClass.class)))
                .thenReturn(savedOfferClass);


        //Ejecutar el metodo a probar
        OfferResponseDTO response = offerClassService.createOffer(requestDTO);

        //Asserts para verificar los resultados
        assertNotNull(response);
        assertEquals(idTeacher, response.getTeacherId());
        assertEquals(idSubject, response.getId());
        assertEquals(description, response.getDescription());

        // 5. VERIFY: Verificar que se llamaron los mocks
        verify(teacherProfileRepository).findById(anyLong());
        verify(subjectRepository).findById(anyLong());
    }

    //Intento de crear oferta de clase sin existir profesor
    @Test
    void createOfferClassTeacherNotFound() {
        OfferRequestDTO requestDTO = new OfferRequestDTO(
                1L,
                1L,
                "Matematicas aplicadas"
        );

        when(teacherProfileRepository.findById(requestDTO.getIdTeacher()))
                .thenReturn(Optional.empty());

        assertThrows(TeacherExistException.class, () -> {
            offerClassService.createOffer(requestDTO);
        });
    }

    //Intento de crear oferta de clase sin existir materia
    @Test
    void createOfferClassSignatureNotFound() {

        OfferRequestDTO requestDTO = new OfferRequestDTO(
                1L,
                2L,
                "Fisica cuantica"

        );

        // Simulamos que el profesor SÍ existe
        when(teacherProfileRepository.findById(requestDTO.getIdTeacher()))
                .thenReturn(Optional.of(new TeacherProfile()));

        // Simulamos que la materia NO existe
        when(subjectRepository.findById(requestDTO.getIdSubject()))
                .thenReturn(Optional.empty());


        assertThrows(SubjectNotFoundException.class, () -> {
            offerClassService.createOffer(requestDTO);
        });

    }

    @Test
    void getAllOfferClassesShouldReturnList() {

        offer = new OfferClass();
        offer.setIdOfferClass(1L);
        offer.setTeacher(teacherProfile);
        offer.setSubject(subject);
        offer.setDescription("Clase test");

        when(offerClassRepository.findAll()).thenReturn(List.of(offer));

        List<OfferResponseDTO> res = offerClassService.getAllOfferClasses();

        assertNotNull(res);
        assertFalse(res.isEmpty());
        verify(offerClassRepository).findAll();
    }

    @Test
    void getOffersBySubjectShouldReturnList() {

        offer = new OfferClass();
        offer.setIdOfferClass(1L);
        offer.setTeacher(teacherProfile);
        offer.setSubject(subject);

        when(offerClassRepository.findBySubject_IdSubject(subject.getIdSubject()))
                .thenReturn(List.of(offer));

        List<OfferResponseDTO> res = offerClassService.getOffersBySubject(subject.getIdSubject());

        assertNotNull(res);
        assertFalse(res.isEmpty());
        verify(offerClassRepository).findBySubject_IdSubject(subject.getIdSubject());


    }

    @Test
    void getOffersByTeacherShouldReturnList() {
        offer = new OfferClass();
        offer.setIdOfferClass(1L);
        offer.setTeacher(teacherProfile);
        offer.setSubject(subject);

        when(offerClassRepository.findByTeacher_Id(teacherProfile.getId()))
                .thenReturn(List.of(offer));
        List<OfferResponseDTO> res = offerClassService.getOffersByTeacher(teacherProfile.getId());


        assertNotNull(res);
        assertFalse(res.isEmpty());
        verify(offerClassRepository).findByTeacher_Id(teacherProfile.getId());

    }

    @Test
    void deleteOfferShouldCallRepository() {
        Long idToDelete = 1L;

        offerClassService.deleteOffer(idToDelete);

        verify(offerClassRepository).deleteById(idToDelete);
    }

}