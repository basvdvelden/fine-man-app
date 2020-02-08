package nl.management.finance.app.data.user;

import java.util.List;

import nl.management.finance.app.data.userbank.UserBankDto;

public class UserDto {
    private List<UserBankDto> userBanks;

    public List<UserBankDto> getUserBanks() {
        return userBanks;
    }

    public void setUserBanks(List<UserBankDto> userBanks) {
        this.userBanks = userBanks;
    }
}
