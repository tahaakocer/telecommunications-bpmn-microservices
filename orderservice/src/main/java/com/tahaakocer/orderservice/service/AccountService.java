package com.tahaakocer.orderservice.service;

import com.tahaakocer.orderservice.dto.AccountDto;
import com.tahaakocer.orderservice.exception.GeneralException;
import com.tahaakocer.orderservice.mapper.AccountMapper;
import com.tahaakocer.orderservice.model.mongo.Account;
import com.tahaakocer.orderservice.repository.mongo.AccountRepository;
import com.tahaakocer.orderservice.utils.KeycloakUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@Slf4j
public class AccountService {
    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    public AccountService(AccountRepository accountRepository, AccountMapper accountMapper) {
        this.accountRepository = accountRepository;
        this.accountMapper = accountMapper;
    }

    public AccountDto createAccount(AccountDto accountDto) {
        try {
            Account account = this.accountMapper.dtoToEntity(accountDto);
            account.setCreateDate(LocalDateTime.now());
            account.setCreatedBy(KeycloakUtil.getKeycloakUsername());
            account.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
            account.setUpdateDate(account.getCreateDate());
            account = this.accountRepository.save(account);
            log.info("Account oluşturuldu.");
            return this.accountMapper.entityToDto(account);
        } catch (Exception e) {
            log.error("Account oluşturulurken hata oluştu: {}", e.getMessage(), e);
            throw new GeneralException("Account oluşturulurken hata oluştu: " + e.getMessage(), e);
        }
    }

    public AccountDto get(UUID id, String accountCode, UUID orderRequestId) {
        if (id != null)
            return getAccount(id);
         else if (accountCode != null)
            return getAccount(accountCode);
         else if (orderRequestId != null)
            return getAccountByOrderRequestId(orderRequestId);
         else
            throw new GeneralException("Parametre eksik.");
    }

    private AccountDto getAccount(UUID id) {
        try {
            Account account = this.accountRepository.findById(id).orElseThrow(
                    () -> new GeneralException("Account bulunamadı."));
            log.info("Account bulundu.");
            return this.accountMapper.entityToDto(account);
        } catch (Exception e) {
            log.error("Account bulunurken hata oluştu: {}", e.getMessage(), e);
            throw new GeneralException("Account bulunurken hata oluştu: " + e.getMessage(), e);
        }
    }

    private AccountDto getAccount(String accountCode) {
        try {
            Account account = this.accountRepository.findByAccountCode(accountCode).orElseThrow(
                    () -> new GeneralException("Account bulunamadı."));
            log.info("Account bulundu.");
            return this.accountMapper.entityToDto(account);
        } catch (Exception e) {
            log.error("Account bulunurken hata oluştu: {}", e.getMessage(), e);
            throw new GeneralException("Account bulunurken hata oluştu: " + e.getMessage(), e);
        }
    }

    private AccountDto getAccountByOrderRequestId(UUID orderRequestId) {
        try {
            Account account = this.accountRepository.findByOrderRequestRef_Id(orderRequestId).orElseThrow(
                    () -> new GeneralException("Account bulunamadı."));
            log.info("Account bulundu.");
            return this.accountMapper.entityToDto(account);
        } catch (Exception e) {
            log.error("Account bulunurken hata oluştu: {}", e.getMessage(), e);
            throw new GeneralException("Account bulunurken hata oluştu: " + e.getMessage(), e);
        }
    }

    public AccountDto updateAccount(AccountDto accountDto) {
        try {
            Account accountEntity = this.accountRepository.findById(accountDto.getId()).orElseThrow(
                    () -> new GeneralException("Account bulunamadı."));
            this.accountMapper.updateEntityFromDto(accountEntity, accountDto);
            accountEntity.setLastModifiedBy(KeycloakUtil.getKeycloakUsername());
            accountEntity.setUpdateDate(LocalDateTime.now());
            accountEntity.setId(accountEntity.getId());
            Account accountSaved = this.accountRepository.save(accountEntity);
            log.info("Account güncellendi.");
            return this.accountMapper.entityToDto(accountSaved);
        } catch (Exception e) {
            log.error("Account güncellenirken hata oluştu: {}", e.getMessage(), e);
            throw new GeneralException("Account güncellenirken hata oluştu: " + e.getMessage(), e);
        }
    }

    public void deleteAccount(UUID id) {
        try {
            Account account = this.accountRepository.findById(id).orElseThrow(
                    () -> new GeneralException("Account bulunamadı."));
            this.accountRepository.deleteById(account.getId());
            log.info("Account silindi.");
        } catch (Exception e) {
            log.error("Account silinirken hata oluştu: {}", e.getMessage(), e);
            throw new GeneralException("Account silinirken hata oluştu: " + e.getMessage(), e);
        }
    }
}
