package nl.management.finance.app.data.bank;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.management.finance.app.data.userbank.UserBank;
import nl.management.finance.app.data.userbank.UserBankDom;
import nl.management.finance.app.data.userbank.UserBankDto;

public class BankMapper {
    @Inject
    public BankMapper() {}

    public List<UserBank> toUserBankEntities(List<UserBankDto> dtos) {
        List<UserBank> result = new ArrayList<>();
        for (UserBankDto dto: dtos) {
            UserBank userBank = new UserBank();
            userBank.setUserId(dto.getUserId());
            userBank.setBankId(dto.getBank().getId());
            userBank.setAccessToken(dto.getAccessToken());
            userBank.setExpiresAt(dto.getExpiresAt());
            userBank.setRefreshToken(dto.getRefreshToken());
            userBank.setTokenType(dto.getTokenType());
            userBank.setConsentCode(dto.getConsentCode());

            result.add(userBank);
        }
        return result;
    }

}
