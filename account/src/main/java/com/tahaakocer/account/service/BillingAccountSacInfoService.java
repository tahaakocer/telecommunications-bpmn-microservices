package com.tahaakocer.account.service;

import com.tahaakocer.account.exception.GeneralException;
import com.tahaakocer.account.mapper.BillingAccountSacInfoMapper;
import com.tahaakocer.account.model.Account;
import com.tahaakocer.account.model.BillingAccount;
import com.tahaakocer.account.model.BillingAccountSacInfo;
import com.tahaakocer.account.repository.BillingAccountSacInfoRepository;
import com.tahaakocer.commondto.order.OrderRequestDto;
import com.tahaakocer.commondto.order.SacInfoDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class BillingAccountSacInfoService {
    private final BillingAccountSacInfoRepository billingAccountSacInfoRepository;
    private final BillingAccountSacInfoMapper billingAccountSacInfoMapper;

    public BillingAccountSacInfoService(BillingAccountSacInfoRepository billingAccountSacInfoRepository,
                                        BillingAccountSacInfoMapper billingAccountSacInfoMapper) {
        this.billingAccountSacInfoRepository = billingAccountSacInfoRepository;
        this.billingAccountSacInfoMapper = billingAccountSacInfoMapper;
    }

    protected BillingAccountSacInfo createBillingAccountSacInfoByOrder(OrderRequestDto orderRequestDto, Account account) {
        SacInfoDto sacInfoDto = orderRequestDto.getBaseOrder().getEngagedParty().getAddress().getSacInfo();
        BillingAccountSacInfo billingAccountSacInfo = new BillingAccountSacInfo();
        this.billingAccountSacInfoMapper.sacInfoDtoToBillingAccountSacInfoEntity(billingAccountSacInfo,sacInfoDto);
        log.info("BillingAccountSacInfo mapped: " + billingAccountSacInfo);
        billingAccountSacInfo.setAccount(account);
        return this.saveBillingAccountSacInfo(billingAccountSacInfo);
    }
    private BillingAccountSacInfo saveBillingAccountSacInfo(BillingAccountSacInfo billingAccountSacInfo) {
        try{
            BillingAccountSacInfo saved = this.billingAccountSacInfoRepository.save(billingAccountSacInfo);
            log.info("Saving billingAccountSacInfo " + saved.getId());
            return saved;

        } catch (Exception e) {
            log.error("Error occurred while saving billingAccountSacInfo: {}", e.getMessage());
            throw new GeneralException("Error occurred while saving billingAccountSacInfo: " + e.getMessage());
        }
    }
}
