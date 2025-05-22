package com.tahaakocer.account.service;

import com.tahaakocer.account.exception.GeneralException;
import com.tahaakocer.account.mapper.ContactMediumMapper;
import com.tahaakocer.account.model.Account;
import com.tahaakocer.account.model.ContactMedium;
import com.tahaakocer.account.model.ContactMediumCharacteristic;
import com.tahaakocer.account.repository.ContactMediumRepository;
import com.tahaakocer.commondto.order.AddressDto;
import com.tahaakocer.commondto.order.OrderRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class ContactMediumService {
    private final ContactMediumRepository contactMediumRepository;
    private final ContactMediumMapper contactMediumMapper;

    public ContactMediumService(ContactMediumRepository contactMediumRepository,
                                ContactMediumMapper contactMediumMapper) {
        this.contactMediumRepository = contactMediumRepository;
        this.contactMediumMapper = contactMediumMapper;
    }
    protected List<ContactMedium> createContactMediumWithOrder(
            OrderRequestDto orderRequestDto,
            Account account) {
        //ADDRESS
        ContactMedium contactMediumAddress = createAddressForAccount(orderRequestDto, account);
        ContactMedium savedAddress = this.saveContactMedium(contactMediumAddress);
        log.info("Contact Medium created: " + savedAddress.getType());
        //PHone
        ContactMedium contactMediumPhone = createPhone(
                orderRequestDto.getBaseOrder().getEngagedParty().getPhoneNumber(), account);
        ContactMedium savedPhone = this.saveContactMedium(contactMediumPhone);
        log.info("Contact Medium created: " + savedPhone.getType());

        //EMAIL
        ContactMedium contactMediumEmail = createEmail(
                orderRequestDto.getBaseOrder().getEngagedParty().getEmail(), account);
        ContactMedium savedEmail = this.saveContactMedium(contactMediumEmail);
        log.info("Contact Medium created: " + savedEmail.getType());

        return List.of(savedAddress, savedPhone, savedEmail);
    }
    private ContactMedium createEmail(String email, Account account) {
        ContactMedium contactMedium = new ContactMedium();
        contactMedium.setAccount(account);
        contactMedium.setType("EMAIL");
        ContactMediumCharacteristic emailCharacteristic = new ContactMediumCharacteristic();
        emailCharacteristic.setEmail(email);
        contactMedium.setContactMediumCharacteristic(emailCharacteristic);
        return contactMedium;
    }
    private ContactMedium createPhone(Long phoneNum, Account account)
    {
        ContactMedium contactMedium = new ContactMedium();
        contactMedium.setAccount(account);
        contactMedium.setType("PHONE");
        ContactMediumCharacteristic phoneCharacteristic = new ContactMediumCharacteristic();
        phoneCharacteristic.setPhoneNumber(phoneNum);
        contactMedium.setContactMediumCharacteristic(phoneCharacteristic);
        return contactMedium;
    }
    private ContactMedium createAddressForAccount(OrderRequestDto orderRequestDto, Account account) {
        ContactMedium contactMedium = new ContactMedium();
        contactMedium.setAccount(account);
        contactMedium.setType("ADDRESS");
        AddressDto addressDto = orderRequestDto.getBaseOrder().getEngagedParty().getAddress();
        ContactMediumCharacteristic addressCharacteristic =
                this.contactMediumMapper.AddressDtoToContactMediumCharacteristicEntity(addressDto);
        contactMedium.setContactMediumCharacteristic(addressCharacteristic);
        return contactMedium;
    }
    private ContactMedium saveContactMedium(ContactMedium contactMedium) {
        try {
            ContactMedium saved = contactMediumRepository.save(contactMedium);
            log.info("Saving contactMedium " + saved.getId());
            return saved;
        } catch (Exception e) {
            log.error("Error occurred while saving contactMedium: {}", e.getMessage());
            throw new GeneralException("Failed to save contactMedium");
        }
    }
}
