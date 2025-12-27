package com.edevare.backend.service;

import com.edevare.backend.exceptions.SubjectNotFoundException;
import com.edevare.backend.exceptions.TeacherNotFoundException;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.*;

class OfferClassServiceImplTest {

    //Clase a usar
    private OfferClassServiceImpl offerClassService;

    //Dependencias a simular
    @Mock
    private TeacherProfileRepository teacherProfileRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private OfferClassRepository offerClassRepository;

    private TeacherProfile teacherProfile;
    private Subject subject;
    private OfferClass offer;
    private OfferRequestDTO requestDTO;


    /**
     * Configuración inicial que se ejecuta antes de cada test.
     * Inicializa los mocks y crea los objetos de prueba por defecto (Teacher, Subject, OfferClass, DTO).
     */
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        offerClassService = new OfferClassServiceImpl(offerClassRepository, teacherProfileRepository, subjectRepository);

        teacherProfile = new TeacherProfile();
        teacherProfile.setId(1L);
        teacherProfile.setHourlyRate(BigDecimal.valueOf(20));
        teacherProfile.setModality(TeacherModality.HYBRID);
        teacherProfile.setHourlyRate(BigDecimal.valueOf(20));

        subject = new Subject();
        subject.setIdSubject(1L);
        subject.setName("Matematicas");
        subject.setAcademicLevel("Bachillerato");

        // CORRECCIÓN: Inicializar 'offer' ANTES de usarlo en 'requestDTO'
        offer = new OfferClass();
        offer.setIdOfferClass(1L);
        offer.setTitleClass("Clase de prueba");
        offer.setDescription("Descripción de prueba");
        offer.setTeacher(teacherProfile);
        offer.setSubject(subject);
        offer.setPriceClass(BigDecimal.valueOf(20));

        requestDTO = new OfferRequestDTO(
                teacherProfile.getId(),
                subject.getIdSubject(),
                offer.getTitleClass(),
                teacherProfile.getHourlyRate().doubleValue(),
                offer.getDescription()
        );

    }


    /**
     * Verifica que se pueda crear una oferta de clase exitosamente cuando el profesor y la materia existen.
     * Comprueba que el DTO de respuesta contenga los datos correctos y que se llame al repositorio.
     */
    @Test
    void createOfferClassSucceed() {


        OfferClass savedOfferClass = new OfferClass();
        savedOfferClass.setIdOfferClass(100L); // para ID simulando de la oferta
        savedOfferClass.setTeacher(teacherProfile);
        savedOfferClass.setSubject(subject);
        savedOfferClass.setState(OfferClassState.ACTIVE);
        savedOfferClass.setPriceClass(BigDecimal.valueOf(23));
        savedOfferClass.setTitleClass(offer.getTitleClass());
        savedOfferClass.setDescription(offer.getDescription());


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
        assertEquals(requestDTO.getIdTeacher(), response.getTeacherId());
        assertEquals(savedOfferClass.getIdOfferClass(), response.getId()); // Comparar con el ID de la oferta guardada
        assertEquals(requestDTO.getDescription(), response.getDescription());

        // 5. VERIFY: Verificar que se llamaron los mocks
        verify(teacherProfileRepository).findById(anyLong());
        verify(subjectRepository).findById(anyLong());
    }

    /**
     * Verifica que se lance una excepción TeacherNotFoundException si se intenta crear una oferta
     * con un ID de profesor que no existe en la base de datos.
     */
    @Test
    void createOfferClassTeacherNotFound() {


        when(teacherProfileRepository.findById(requestDTO.getIdTeacher()))
                .thenReturn(Optional.empty());

        assertThrows(TeacherNotFoundException.class, () ->
                offerClassService.createOffer(requestDTO));
    }

    /**
     * Verifica que se lance una excepción SubjectNotFoundException si se intenta crear una oferta
     * con un ID de materia que no existe en la base de datos.
     */
    @Test
    void createOfferClassSignatureNotFound() {


        // Simulamos que el profesor SÍ existe
        when(teacherProfileRepository.findById(requestDTO.getIdTeacher()))
                .thenReturn(Optional.of(new TeacherProfile()));

        // Simulamos que la materia NO existe
        when(subjectRepository.findById(requestDTO.getIdSubject()))
                .thenReturn(Optional.empty());


        assertThrows(SubjectNotFoundException.class, () ->
                offerClassService.createOffer(requestDTO));

    }

    /**
     * Verifica que el metodo getAllOfferClasses devuelva una lista de ofertas correctamente mapeadas
     * cuando existen datos en el repositorio.
     */
    @Test
    void getAllOfferClassesShouldReturnList() {


        when(offerClassRepository.getAllOfferClasses()).thenReturn(List.of(offer));

        List<OfferResponseDTO> res = offerClassService.getAllOfferClasses();

        assertNotNull(res);
        assertFalse(res.isEmpty());
        assertEquals(1, res.size());
        assertEquals(offer.getTitleClass(), res.get(0).getTitle());
        verify(offerClassRepository).getAllOfferClasses();
    }

    /**
     * Verifica que el metodo getOffersBySubject devuelva una lista de ofertas filtradas por el ID de la materia.
     */
    @Test
    void getOffersBySubjectShouldReturnList() {


        when(offerClassRepository.findBySubject_IdSubject(subject.getIdSubject()))
                .thenReturn(List.of(offer));

        List<OfferResponseDTO> res = offerClassService.getOffersBySubject(subject.getIdSubject());

        assertNotNull(res);
        assertFalse(res.isEmpty());
        verify(offerClassRepository).findBySubject_IdSubject(subject.getIdSubject());


    }

    /**
     * Verifica que el metodo getOffersByTeacher devuelva una lista de ofertas filtradas por el ID del profesor.
     */
    @Test
    void getOffersByTeacherShouldReturnList() {


        when(offerClassRepository.findByTeacher_Id(teacherProfile.getId()))
                .thenReturn(List.of(offer));
        List<OfferResponseDTO> res = offerClassService.getOffersByTeacher(teacherProfile.getId());


        assertNotNull(res);
        assertFalse(res.isEmpty());
        verify(offerClassRepository).findByTeacher_Id(teacherProfile.getId());

    }

    /**
     * Verifica que el metodo deleteOffer llame al metodo deleteById del repositorio con el ID correcto.
     */
    @Test
    void deleteOfferShouldCallRepository() {
        Long idToDelete = 1L;

        offerClassService.deleteOffer(idToDelete);

        verify(offerClassRepository).deleteById(idToDelete);
    }

    /**
     * Verifica que getOffersBySubject devuelva una lista vacía (y no null) cuando no se encuentran
     * ofertas para la materia especificada.
     */
    @Test
    void getOffersBySubjectShouldReturnEmptyList() {
        Long subjectId = 99L;

        // CORRECCIÓN: Mockear y llamar al método específico 'findBySubject...'
        when(offerClassRepository.findBySubject_IdSubject(subjectId)).thenReturn(List.of());

        List<OfferResponseDTO> result = offerClassService.getOffersBySubject(subjectId);

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(offerClassRepository).findBySubject_IdSubject(subjectId);

    }


    /**
     * Verifica que searchBySubjectAndLevel devuelva resultados correctos para diferentes combinaciones
     * de nombre de materia y nivel académico válidos.
     */
    @ParameterizedTest(name = "Busqueda con Subject: {0}, Level: {1}")
    @CsvSource({
            "Matemáticas, Bachillerato",
            "'',Bachillerato",
            "Matemáticas , ''"
    })
    void searchBySubjectAndLevel_ShouldReturnResults(String subjectName, String academicLevel) {

        when(offerClassRepository.searchBySubjectAndLevel(subjectName, academicLevel))
                .thenReturn(List.of(offer));

        List<OfferResponseDTO> res = offerClassService.searchBySubjectAndLevel(subjectName, academicLevel);

        assertNotNull(res);
        assertFalse(res.isEmpty());

        verify(offerClassRepository).searchBySubjectAndLevel(subjectName, academicLevel);

    }


    /**
     * Prueba el comportamiento del metodo searchBySubjectAndLevel cuando se envían cadenas vacías como parámetros.
     * Verifica que retorne una lista vacía y que no haya interacciones con el repositorio.
     */
    @Test
    void searchBySubjectAndLevelAllEmpty() {
        String subjectTest = "";
        String levelTest = "";
        when(offerClassRepository.searchBySubjectAndLevel(subjectTest, levelTest))
                .thenReturn(List.of(offer));

        List<OfferResponseDTO> res = offerClassService.searchBySubjectAndLevel(subjectTest, levelTest);

        assertTrue(res.isEmpty());
        assertNotNull(res);
        verifyNoInteractions(offerClassRepository);

    }

    /**
     * Metodo de prueba para verificar el comportamiento del metodo searchBySubjectAndLevel cuando
     * los parámetros subjectName o academicLevel son nulos.
     * Garantiza que el resultado sea una lista vacía y que no se produzcan interacciones con el repositorio.
     *
     */

    @Test
    void searchBySubjectAndLevelNull() {

        List<OfferResponseDTO> res = offerClassService.searchBySubjectAndLevel(null, null);

        assertTrue(res.isEmpty());
        verifyNoInteractions(offerClassRepository);
    }

}