package com.edevare.backend.service.impl;

import com.edevare.backend.exceptions.SubjectNotFoundException;
import com.edevare.backend.exceptions.TeacherNotFoundException;
import com.edevare.backend.model.OfferClass;
import com.edevare.backend.repository.OfferClassRepository;
import com.edevare.backend.repository.SubjectRepository;
import com.edevare.backend.repository.TeacherProfileRepository;
import com.edevare.backend.service.OfferClassService;
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
    public OfferClassServiceImpl(OfferClassRepository offerClassRepository, TeacherProfileRepository teacherProfileRepository, SubjectRepository subjectRepository) {
        this.teacherProfileRepository = teacherProfileRepository;
        this.subjectRepository = subjectRepository;
        this.offerClassRepository = offerClassRepository;
    }

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

    @Override
    public List<OfferResponseDTO> getAllOfferClasses() throws SubjectNotFoundException {

        return offerClassRepository.getAllOfferClasses()
                .stream()
                .map(this::mapToDTO)
                .toList();


    }

    @Override
    public List<OfferResponseDTO> getOffersBySubject(Long idSubject) throws SubjectNotFoundException {
        return offerClassRepository.findBySubject_IdSubject(idSubject).stream()
                .map(this::mapToDTO)
                .toList();
    }

    @Override
    public List<OfferResponseDTO> getOffersByTeacher(Long teacherId) throws SubjectNotFoundException, TeacherNotFoundException {
        return offerClassRepository.findByTeacher_Id(teacherId)
                .stream()
                .map(this::mapToDTO)
                .toList();
    }

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

    protected OfferResponseDTO mapToDTO(OfferClass offerClass) {
        return new OfferResponseDTO(
                offerClass.getIdOfferClass(),
                offerClass.getSubject().getName(),
                offerClass.getSubject().getAcademicLevel(),
                offerClass.getTitleClass(),
                offerClass.getTeacher().getId(),
                offerClass.getPriceClass().doubleValue(),
                offerClass.getTeacher().getModality(),
                offerClass.getDescription()
        );
    }
}
