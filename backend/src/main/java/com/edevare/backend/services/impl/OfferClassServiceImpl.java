package com.edevare.backend.services.impl;

import com.edevare.backend.exceptions.SubjectNotFoundException;
import com.edevare.backend.exceptions.TeacherNotFoundException;
import com.edevare.backend.model.OfferClass;
import com.edevare.backend.repository.OfferClassRepository;
import com.edevare.backend.repository.SubjectRepository;
import com.edevare.backend.repository.TeacherProfileRepository;
import com.edevare.backend.services.OfferClassService;
import com.edevare.shared.entitiesDTO.OfferRequestDTO;
import com.edevare.shared.entitiesDTO.OfferResponseDTO;
import jakarta.transaction.Transactional;

import java.math.BigDecimal;
import java.util.List;

public class OfferClassServiceImpl implements OfferClassService {

    //Dependencias
    private final TeacherProfileRepository teacherProfileRepository;
    private final SubjectRepository subjectRepository;
    private final OfferClassRepository offerClassRepository;

    //Inyeccion de dependencias
    public OfferClassServiceImpl(OfferClassRepository offerClassRepository,TeacherProfileRepository teacherProfileRepository, SubjectRepository subjectRepository) {
        this.teacherProfileRepository = teacherProfileRepository;
        this.subjectRepository = subjectRepository;
        this.offerClassRepository = offerClassRepository;
    }

    /**
     * Crea una nueva oferta de clase validando que el profesor y la materia existan.
     *
     * @param request DTO con la información necesaria para crear la oferta (IDs y descripción).
     * @return OfferResponseDTO con los datos de la oferta creada.
     * @throws TeacherNotFoundException Si el profesor no se encuentra en la base de datos.
     * @throws SubjectNotFoundException Si la materia no se encuentra en la base de datos.
     */
    @Override
    @Transactional
    public OfferResponseDTO createOffer(OfferRequestDTO request) throws TeacherNotFoundException, SubjectNotFoundException {

        var teacher = teacherProfileRepository.findById(request.getIdTeacher())
                .orElseThrow(() -> new TeacherNotFoundException("Teacher does not exist"));

        var subject = subjectRepository.findById(request.getIdSubject())
                .orElseThrow(() -> new SubjectNotFoundException("Subject does not exist"));


        OfferClass newClass = new OfferClass();
        newClass.setSubject(subject);
        newClass.setTeacher(teacher);
        newClass.setDescription(request.getDescription());
        newClass.setTitleClass(request.getTitle());
        newClass.setPriceClass(BigDecimal.valueOf(request.getPrice()));

        OfferClass savedClass = offerClassRepository.save(newClass);

        return mapToDTO(savedClass);


    }

    /**
     * Obtiene todas las ofertas de clases disponibles en el sistema.
     *
     * @return Lista de OfferResponseDTO con todas las ofertas.
     */
    @Override
    public List<OfferResponseDTO> getAllOfferClasses() throws SubjectNotFoundException {

        return offerClassRepository.getAllOfferClasses()
                .stream()
                .map(this::mapToDTO)
                .toList();


    }

    /**
     * Busca ofertas de clases filtradas por materia.
     *
     * @param idSubject ID de la materia a buscar.
     * @return Lista de ofertas asociadas a esa materia.
     */
    @Override
    public List<OfferResponseDTO> getOffersBySubject(Long idSubject) throws SubjectNotFoundException {
        return offerClassRepository.findBySubject_IdSubject(idSubject).stream()
                .map(this::mapToDTO)
                .toList();
    }

    /**
     * Busca ofertas de clases filtradas por profesor.
     *
     * @param teacherId ID del profesor.
     * @return Lista de ofertas creadas por ese profesor.
     */
    @Override
    public List<OfferResponseDTO> getOffersByTeacher(Long teacherId) throws SubjectNotFoundException, TeacherNotFoundException {
        return offerClassRepository.findByTeacher_Id(teacherId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    /**
     * Elimina una oferta de clase por su ID.
     *
     * @param idOffer el ID de la oferta a eliminar.
     */
    @Override
    public void deleteOffer(Long idOffer) {
        offerClassRepository.deleteById(idOffer);
    }


    //Buscador por nivel academico y nombre de materia (nombre no tiene porque ser exacto)
    @Override
    public List<OfferResponseDTO> searchBySubjectAndLevel(String subjectName, String academicLevel) {
        String nameToSearch = (subjectName != null) ? subjectName : "";
        String levelToSearch = (academicLevel != null) ? academicLevel : "";

        if (nameToSearch.isBlank() && levelToSearch.isBlank()) return List.of();

        return offerClassRepository
                .searchBySubjectAndLevel(nameToSearch, levelToSearch)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

    /**
     * Convierte una entidad OfferClass a su representación DTO OfferResponseDTO.
     *
     * @param offerClass Entidad a convertir.
     * @return DTO con los datos de la entidad.
     */
    protected OfferResponseDTO mapToDTO(OfferClass offerClass) {
        return new OfferResponseDTO(
                offerClass.getIdOfferClass(),
                offerClass.getSubject().getName(),
                offerClass.getTitleClass(),
                offerClass.getSubject().getAcademicLevel(),
                offerClass.getTeacher().getId(),
                offerClass.getPriceClass().doubleValue(),
                offerClass.getTeacher().getModality(),
                offerClass.getDescription()
        );
    }
}
