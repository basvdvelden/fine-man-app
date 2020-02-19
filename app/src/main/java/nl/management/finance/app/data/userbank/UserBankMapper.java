package nl.management.finance.app.data.userbank;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.management.finance.app.data.bank.BankMapper;
import nl.management.finance.app.data.user.UserMapper;

@Singleton
public class UserBankMapper {
    private final BankMapper bankMapper;
    private final UserMapper userMapper;

    @Inject
    public UserBankMapper(BankMapper bankMapper, UserMapper userMapper) {
        this.bankMapper = bankMapper;
        this.userMapper = userMapper;
    }

    public List<UserBank> toEntity(List<UserBankDto> dtos) {
        List<UserBank> result = new ArrayList<>();
        for (UserBankDto dto: dtos) {
            UserBank userBank = new UserBank();
            userBank.setUserId(dto.getUser().getUserId());
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

    public UserBankDto toDto(UserBank userBank) {
        UserBankDto dto = new UserBankDto();
        dto.setAccessToken(userBank.getAccessToken());
        dto.setRefreshToken(userBank.getRefreshToken());
        dto.setExpiresAt(userBank.getExpiresAt());
        dto.setBank(bankMapper.toDto(userBank.getBankId()));
        dto.setUser(userMapper.toDto(userBank.getUserId()));
        dto.setScopes(userBank.getScopes());
        dto.setTokenType(userBank.getTokenType());
        dto.setConsentCode(userBank.getConsentCode());
        return dto;
    }
}
