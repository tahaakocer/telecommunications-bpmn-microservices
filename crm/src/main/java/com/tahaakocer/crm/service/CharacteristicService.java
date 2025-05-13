package com.tahaakocer.crm.service;

import com.tahaakocer.crm.model.Characteristic;
import com.tahaakocer.crm.repository.CharacteristicRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CharacteristicService {
    private final CharacteristicRepository characteristicRepository;

    public CharacteristicService(CharacteristicRepository characteristicRepository) {
        this.characteristicRepository = characteristicRepository;
    }

    protected Characteristic saveCharacteristic(Characteristic characteristic) {
        try {
            Characteristic saved = this.characteristicRepository.save(characteristic);
            log.info("Saved characteristic: {}", characteristic);
            return saved;
        }
        catch (Exception e) {
            log.error("Error saving characteristic: {}", e.getMessage());
            throw new RuntimeException("Error saving characteristic", e);
        }
    }
}
