package com.tahaakocer.orderservice.controller;

import com.tahaakocer.commondto.order.CharacteristicDto;
import com.tahaakocer.orderservice.dto.request.CharacteristicCreateRequest;
import com.tahaakocer.orderservice.dto.response.CharacteristicGetAllResponse;
import com.tahaakocer.orderservice.dto.response.GeneralResponse;
import com.tahaakocer.orderservice.mapper.CharacteristicMapper;
import com.tahaakocer.orderservice.service.CharacteristicService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/characteristic")
public class CharacteristicController {
    private final CharacteristicService characteristicService;
    private final CharacteristicMapper characteristicMapper;

    public CharacteristicController(CharacteristicService characteristicService, CharacteristicMapper characteristicMapper) {
        this.characteristicService = characteristicService;
        this.characteristicMapper = characteristicMapper;
    }

    @PostMapping("/create")
    public ResponseEntity<GeneralResponse<CharacteristicDto>> createCharacteristic(@RequestBody CharacteristicCreateRequest characteristicCreateRequest) {
        CharacteristicDto characteristicDto = characteristicMapper.characteristicCreateRequestToDto(characteristicCreateRequest);
        CharacteristicDto characteristic = characteristicService.createCharacteristic(characteristicDto);
        return ResponseEntity.ok
                (GeneralResponse.<CharacteristicDto>builder()
                        .code(200)
                        .message("Characteristic created successfully")
                        .data(characteristic)
                        .build()
                );
    }
    @GetMapping("/get-by-code")
    public ResponseEntity<GeneralResponse<CharacteristicDto>> getCharacteristicByCode(@RequestParam String code) {
        CharacteristicDto characteristic = characteristicService.getCharacteristicByCode(code);
        return ResponseEntity.ok
                (GeneralResponse.<CharacteristicDto>builder()
                        .code(200)
                        .message("Characteristic found successfully")
                        .data(characteristic)
                        .build()
                );
    }
    @GetMapping("/get-by-id")
    public ResponseEntity<GeneralResponse<CharacteristicDto>> getCharacteristicById(@RequestParam String id) {
        CharacteristicDto characteristic = characteristicService.getCharacteristicById(UUID.fromString(id));
        return ResponseEntity.ok
                (GeneralResponse.<CharacteristicDto>builder()
                        .code(200)
                        .message("Characteristic found successfully")
                        .data(characteristic)
                        .build()
                );
    }
    @GetMapping("/get-all")
    public ResponseEntity<GeneralResponse<List<CharacteristicGetAllResponse>>> getAllCharacteristics() {
        List<CharacteristicDto> characteristics = characteristicService.getAllCharacteristics();
        return ResponseEntity.ok(GeneralResponse.<List<CharacteristicGetAllResponse>>builder()
                .data(this.characteristicMapper.characteristicDtoListToGetAllResponseList(characteristics))
                        .code(200)
                        .message("Characteristics found successfully")
                .build()
                );
    }
    @DeleteMapping("/delete-by-id")
    public ResponseEntity<GeneralResponse<Void>> deleteCharacteristicById(@RequestParam String id) {
        characteristicService.deleteCharacteristicById(UUID.fromString(id));
        return ResponseEntity.ok
                (GeneralResponse.<Void>builder()
                        .code(200)
                        .message("Characteristic deleted successfully")
                        .data(null)
                        .build()
                );
    }
    @DeleteMapping("/delete-by-code")
    public ResponseEntity<GeneralResponse<Void>> deleteCharacteristicByCode(@RequestParam String code) {
        characteristicService.deleteCharacteristicByCode(code);
        return ResponseEntity.ok
                (GeneralResponse.<Void>builder()
                        .code(200)
                        .message("Characteristic deleted successfully")
                        .data(null)
                        .build()
                );
    }

}
