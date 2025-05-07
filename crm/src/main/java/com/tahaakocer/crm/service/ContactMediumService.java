package com.tahaakocer.crm.service;

import com.tahaakocer.commondto.order.AddressDto;
import com.tahaakocer.commondto.order.EngagedPartyDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.crm.dto.ContactMediumDto;
import com.tahaakocer.crm.exception.GeneralException;
import com.tahaakocer.crm.mapper.ContactMediumMapper;
import com.tahaakocer.crm.model.ContactMedium;
import com.tahaakocer.crm.model.ContactMediumCharacteristic;
import com.tahaakocer.crm.model.PartyRole;
import com.tahaakocer.crm.repository.ContactMediumCharacteristicRepository;
import com.tahaakocer.crm.repository.ContactMediumRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ContactMediumService {

    private final ContactMediumRepository contactMediumRepository;
    private final ContactMediumMapper contactMediumMapper;
    private final ContactMediumCharacteristicRepository contactMediumCharacteristicRepository;

    public ContactMediumService(ContactMediumRepository contactMediumRepository,
                                ContactMediumMapper contactMediumMapper,
                                ContactMediumCharacteristicRepository contactMediumCharacteristicRepository) {
        this.contactMediumRepository = contactMediumRepository;
        this.contactMediumMapper = contactMediumMapper;
        this.contactMediumCharacteristicRepository = contactMediumCharacteristicRepository;
    }

    public List<ContactMedium> createContactMediumWithOrderAndPartyRole(
            OrderRequestDto orderRequestDto,
            PartyRole partyRole) {
        //ADDRESS
        ContactMedium contactMediumAddress = createAddressContactMedium(orderRequestDto, partyRole);
        ContactMedium savedAddress = this.saveContactMedium(contactMediumAddress);
        log.info("Contact Medium created: " + savedAddress.getType());
        //PHone
        ContactMedium contactMediumPhone = createPhoneContactMedium(orderRequestDto, partyRole);
        ContactMedium savedPhone = this.saveContactMedium(contactMediumPhone);
        log.info("Contact Medium created: " + savedPhone.getType());

        //EMAIL
        ContactMedium contactMediumEmail = createEmailContactMedium(orderRequestDto, partyRole);
        ContactMedium savedEmail = this.saveContactMedium(contactMediumEmail);
        log.info("Contact Medium created: " + savedEmail.getType());

        return List.of(savedAddress, savedPhone, savedEmail);
    }

    private ContactMedium createEmailContactMedium(OrderRequestDto orderRequestDto, PartyRole partyRole) {
        ContactMedium contactMedium = new ContactMedium();
        contactMedium.setPartyRole(partyRole);
        contactMedium.setType("EMAIL");
        ContactMediumCharacteristic emailCharacteristic = new ContactMediumCharacteristic();
        emailCharacteristic.setEmail(orderRequestDto.getBaseOrder().getEngagedParty().getEmail());
        emailCharacteristic.setContactMedium(contactMedium);
        contactMedium.setContactMediumCharacteristic(emailCharacteristic);
        return contactMedium;
    }
    private ContactMedium createPhoneContactMedium(OrderRequestDto orderRequestDto,PartyRole partyRole)
    {
        ContactMedium contactMedium = new ContactMedium();
        contactMedium.setPartyRole(partyRole);
        contactMedium.setType("PHONE");
        ContactMediumCharacteristic phoneCharacteristic = new ContactMediumCharacteristic();
        phoneCharacteristic.setPhoneNumber(orderRequestDto.getBaseOrder().getEngagedParty().getPhoneNumber());
        phoneCharacteristic.setContactMedium(contactMedium);
        contactMedium.setContactMediumCharacteristic(phoneCharacteristic);
        return contactMedium;
    }
    private ContactMedium createAddressContactMedium(OrderRequestDto orderRequestDto, PartyRole partyRole) {
        ContactMedium contactMedium = new ContactMedium();
        contactMedium.setPartyRole(partyRole);
        contactMedium.setType("ADDRESS");
        AddressDto addressDto = orderRequestDto.getBaseOrder().getEngagedParty().getAddress();
        ContactMediumCharacteristic addressCharacteristic =
                this.contactMediumMapper.AddressDtoToContactMediumCharacteristicEntity(addressDto);
        addressCharacteristic.setContactMedium(contactMedium);
        contactMedium.setContactMediumCharacteristic(addressCharacteristic);
        return contactMedium;
    }

    private ContactMedium saveContactMedium(ContactMedium contactMedium) {
        try {
            ContactMediumCharacteristic characteristic = contactMedium.getContactMediumCharacteristic();
            characteristic.setContactMedium(contactMedium);
            ContactMedium saved = contactMediumRepository.save(contactMedium);
            log.info("Saving contactMedium " + saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Error occurred while saving contactMedium: {}", e.getMessage());
            throw new GeneralException("Failed to save contactMedium");
        }
    }

}
