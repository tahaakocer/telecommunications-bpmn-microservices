package com.tahaakocer.orderservice.service;

import com.tahaakocer.commondto.order.CharacteristicDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.mapper.CharacteristicMapper;
import com.tahaakocer.orderservice.model.mongo.Characteristic;
import com.tahaakocer.orderservice.repository.mongo.CharacteristicRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import com.tahaakocer.orderservice.utils.PUUID;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@Slf4j
public class CharacteristicService {
    private final CharacteristicRepository characteristicRepository;
    private final CharacteristicMapper characteristicMapper;

    public CharacteristicService(CharacteristicRepository characteristicRepository,
                                 CharacteristicMapper characteristicMapper) {
        this.characteristicRepository = characteristicRepository;
        this.characteristicMapper = characteristicMapper;
    }

    public CharacteristicDto createCharacteristic(CharacteristicDto characteristicDto) {
        if(characteristicRepository.existsByCode(characteristicDto.getCode())){
            log.error("Characteristic with code {} already exists", characteristicDto.getCode());
            throw new GeneralException("Characteristic with code " + characteristicDto.getCode() + " already exists");
        }
        else {
            Characteristic characteristic = characteristicMapper.dtoToCharacteristic(characteristicDto);
            characteristic.setId(PUUID.randomUUID());
            characteristic.setCreatedBy(KeycloakUtil.getKeycloakUsername());
            characteristic.setCreateDate(LocalDateTime.now());
            Characteristic saved = characteristicRepository.save(characteristic);
            log.info("Characteristic saved with id: {}", saved.getId());
            return characteristicMapper.characteristicToDto(saved);
        }
    }

    public void deleteCharacteristicByCode(String code) {
        Characteristic characteristic = characteristicRepository.findByCode(code).orElseThrow(
                () -> new GeneralException("Characteristic with code " + code + " not found")
        );
        log.info("Characteristic deleted with id: {}", characteristic.getId());
        characteristicRepository.delete(characteristic);
    }
    public void deleteCharacteristicById(UUID id){
        Characteristic characteristic = characteristicRepository.findById(id).orElseThrow(
                () -> new GeneralException("Characteristic with id " + id + " not found")
        );
        log.info("Characteristic deleted with id: {}", characteristic.getId());
        characteristicRepository.delete(characteristic);
    }
    public CharacteristicDto getCharacteristicByCode(String code) {
        Characteristic characteristic = characteristicRepository.findByCode(code).orElseThrow(
                () -> new GeneralException("Characteristic with code " + code + " not found")
        );
        log.info("Characteristic found with id: {}", characteristic.getId());
        return characteristicMapper.characteristicToDto(characteristic);
    }
    public CharacteristicDto getCharacteristicById(UUID id) {
        Characteristic characteristic = characteristicRepository.findById(id).orElseThrow(
                () -> new GeneralException("Characteristic with id " + id + " not found")
        );
        log.info("Characteristic found with id: {}", characteristic.getId());
        return characteristicMapper.characteristicToDto(characteristic);
    }

    public List<CharacteristicDto> getAllCharacteristics() {
        List<Characteristic> characteristics = characteristicRepository.findAll();
        log.info("Characteristics found with count: {}", characteristics.size());
        return characteristicMapper.characteristicsToDtos(characteristics);
    }
}
