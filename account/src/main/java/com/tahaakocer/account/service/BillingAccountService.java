package com.tahaakocer.account.service;

import com.tahaakocer.account.exception.GeneralException;
import com.tahaakocer.account.model.Account;
import com.tahaakocer.account.model.BillingAccount;
import com.tahaakocer.account.model.BillingSystem;
import com.tahaakocer.account.repository.BillingAccountRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BillingAccountService {
    private final BillingAccountRepository billingAccountRepository;

    public BillingAccountService(BillingAccountRepository billingAccountRepository) {
        this.billingAccountRepository = billingAccountRepository;
    }

    protected BillingAccount createBillingAccountByAccount(Account account) {
        BillingAccount billingAccount = new BillingAccount();
        BillingSystem billingSystem = new BillingSystem();
        billingAccount.setBillingSystem(billingSystem);
        billingAccount.setBillCycle("TUMAY");
        billingAccount.setAccount(account);
        log.info("BillingSystem created: " + billingSystem);
        return this.saveBillingAccount(billingAccount);



    }
    private BillingAccount saveBillingAccount(BillingAccount billingAccountSacInfo) {
        try{
            BillingSystem billingSystem = billingAccountSacInfo.getBillingSystem();
            billingSystem.setBillingAccount(billingAccountSacInfo);
            BillingAccount saved = this.billingAccountRepository.save(billingAccountSacInfo);
            log.info("Saving billingAccount " + saved.getId());
            return saved;

        } catch (Exception e) {
            log.error("Error occurred while saving billingAccount: {}", e.getMessage());
            throw new GeneralException("Error occurred while saving billingAccount: " + e.getMessage());
        }
    }
}
