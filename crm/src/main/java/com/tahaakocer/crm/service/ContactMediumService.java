package com.tahaakocer.crm.service;

import com.tahaakocer.commondto.order.AddressDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.crm.exception.GeneralException;
import com.tahaakocer.crm.mapper.ContactMediumMapper;
import com.tahaakocer.crm.model.ContactMedium;
import com.tahaakocer.crm.model.ContactMediumCharacteristic;
import com.tahaakocer.crm.model.PartnerUser;
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

    protected List<ContactMedium> createContactMediumWithOrder(
            OrderRequestDto orderRequestDto,
            PartyRole partyRole) {
        //ADDRESS
        ContactMedium contactMediumAddress = createAddressForCustomer(orderRequestDto, partyRole);
        ContactMedium savedAddress = this.saveContactMedium(contactMediumAddress);
        log.info("Contact Medium created: " + savedAddress.getType());
        //PHone
        ContactMedium contactMediumPhone = createPhone(
                orderRequestDto.getBaseOrder().getEngagedParty().getPhoneNumber(), partyRole);
        ContactMedium savedPhone = this.saveContactMedium(contactMediumPhone);
        log.info("Contact Medium created: " + savedPhone.getType());

        //EMAIL
        ContactMedium contactMediumEmail = createEmail(
                orderRequestDto.getBaseOrder().getEngagedParty().getEmail(), partyRole);
        ContactMedium savedEmail = this.saveContactMedium(contactMediumEmail);
        log.info("Contact Medium created: " + savedEmail.getType());

        return List.of(savedAddress, savedPhone, savedEmail);
    }
    protected List<ContactMedium> createContactMediumWithPartnerUser(PartnerUser partnerUser, PartyRole partyRole) {
        //PHONE
        ContactMedium contactMediumPhone = createPhone(
               partnerUser.getPhoneNumber(), partyRole);
        ContactMedium savedPhone = this.saveContactMedium(contactMediumPhone);
        log.info("Contact Medium created: " + savedPhone.getType());
        //EMAİL
        ContactMedium contactMediumEmail = createEmail(
               partnerUser.getEmail(), partyRole);
        ContactMedium savedEmail = this.saveContactMedium(contactMediumEmail);
        log.info("Contact Medium created: " + savedEmail.getType());

        return List.of(savedPhone, savedEmail);
    }

    private ContactMedium createEmail(String email, PartyRole partyRole) {
        ContactMedium contactMedium = new ContactMedium();
        contactMedium.setPartyRole(partyRole);
        contactMedium.setType("EMAIL");
        ContactMediumCharacteristic emailCharacteristic = new ContactMediumCharacteristic();
        emailCharacteristic.setEmail(email);
        emailCharacteristic.setContactMedium(contactMedium);
        contactMedium.setContactMediumCharacteristic(emailCharacteristic);
        return contactMedium;
    }
    private ContactMedium createPhone(Long phoneNum, PartyRole partyRole)
    {
        ContactMedium contactMedium = new ContactMedium();
        contactMedium.setPartyRole(partyRole);
        contactMedium.setType("PHONE");
        ContactMediumCharacteristic phoneCharacteristic = new ContactMediumCharacteristic();
        phoneCharacteristic.setPhoneNumber(phoneNum);
        phoneCharacteristic.setContactMedium(contactMedium);
        contactMedium.setContactMediumCharacteristic(phoneCharacteristic);
        return contactMedium;
    }
    private ContactMedium createAddressForCustomer(OrderRequestDto orderRequestDto, PartyRole partyRole) {
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
