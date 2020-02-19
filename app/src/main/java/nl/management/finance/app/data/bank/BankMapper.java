package nl.management.finance.app.data.bank;

import javax.inject.Inject;
import javax.inject.Singleton;

import nl.management.finance.app.BuildConfig;

@Singleton
public class BankMapper {
    @Inject
    public BankMapper() { }

    public BankDto toDto(int bankId) {
        BankDto dto = new BankDto();
        dto.setId(bankId);
        dto.setName(getBankName(bankId));
        return dto;
    }

    private String getBankName(int bankId) {
        switch (bankId) {
            case BuildConfig
                    .RABO_BANK_ID:
                return BuildConfig.RABO_BANK_NAME;
            default:
                throw new IllegalStateException("Couldn't find bank name for id: " + bankId);
        }
    }
}
