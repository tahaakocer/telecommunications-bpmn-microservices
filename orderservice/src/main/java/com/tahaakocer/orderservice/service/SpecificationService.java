package com.tahaakocer.orderservice.service;

import com.tahaakocer.commondto.order.CharacteristicDto;
import com.tahaakocer.commondto.order.SpecificationDto;
import com.tahaakocer.orderservice.dto.request.SpecificationCreateRequest;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.mapper.SpecificationMapper;
import com.tahaakocer.orderservice.model.mongo.Specification;
import com.tahaakocer.orderservice.repository.mongo.SpecificationRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Slf4j
@Service
public class SpecificationService {
    private final SpecificationRepository specificationRepository;
    private final SpecificationMapper specificationMapper;
    private final CharacteristicService characteristicService;

    public SpecificationService(SpecificationRepository specificationRepository,
                                SpecificationMapper specificationMapper,
                                CharacteristicService characteristicService) {
        this.specificationRepository = specificationRepository;
        this.specificationMapper = specificationMapper;
        this.characteristicService = characteristicService;
    }

    public SpecificationDto createSpecification(SpecificationCreateRequest request) {
        if(this.specificationRepository.existsByCode(request.getCode()))
            throw new GeneralException("Specification already exists");
        List<CharacteristicDto> characteristicDtoList;
        try{
            characteristicDtoList = request.getCharacteristicCodeList()
                    .stream().map(this.characteristicService::getCharacteristicByCode).toList();
        }catch (Exception e){
            log.error("characteristic not found");
            throw new GeneralException("characteristic not found");
        }
        SpecificationDto specificationDto = SpecificationDto.builder().code(request.getCode()).characteristics(characteristicDtoList).build();
        Specification specification = this.specificationMapper.dtoToEntity(specificationDto);
        specification.setCreatedBy(KeycloakUtil.getKeycloakUsername());
        specification.setCreateDate(LocalDateTime.now());
        specification.setId(UUID.randomUUID());
        Specification saved = this.specificationRepository.save(specification);
        log.info("Specification saved with id: {}, name:  {}", saved.getId(),saved.getCode());
        return this.specificationMapper.entityToDto(saved);
    }

    public List<SpecificationDto> searchSpecification(String query) {
        List<Specification> specifications = this.specificationRepository.findBySpecificationsCodeContainingIgnoreCase(query);
        log.info("Specifications found with count: {}", specifications.size());
        return this.specificationMapper.entityToDtoList(specifications);
    }
    public SpecificationDto getSpecificationById(UUID id) {
        Specification specification = this.specificationRepository.findById(id).orElseThrow(
                () -> new GeneralException("Specification not found")
        );
        log.info("Specification found with id: {}", specification.getId());
        return this.specificationMapper.entityToDto(specification);
    }
    public SpecificationDto getSpecificationByCode(String name) {
        Specification specification = this.specificationRepository.findByCode(name).orElseThrow(
                () -> new GeneralException("Specification not found")
        );
        log.info("Specification found with name: {}", specification.getCode());
        return this.specificationMapper.entityToDto(specification);
    }

    public List<SpecificationDto> getAllSpecifications() {
        List<Specification> specifications = this.specificationRepository.findAll();
        log.info("Specifications found with count: {}", specifications.size());
        return this.specificationMapper.entityToDtoList(specifications);
    }

    public void deleteSpecificationById(UUID uuid) {
        Specification specification = this.specificationRepository.findById(uuid).orElseThrow(
                () -> new GeneralException("Specification not found")
        );
        log.info("Specification deleted with id: {}", specification.getId());
        this.specificationRepository.delete(specification);
    }

    public List<SpecificationDto> createSpecificationBatch(List<SpecificationCreateRequest> requests) {

        return requests.stream().map(this::createSpecification).toList();
    }
}
