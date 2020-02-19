package nl.management.finance.app.data.userbank;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.inject.Inject;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.UserSubject;
import nl.management.finance.app.data.Result;
import nl.management.finance.app.data.api.BankAuthNotifier;
import nl.management.finance.app.data.user.Authentication;

public class UserBankRepository {
    private static final String TAG = "UserBankRepository";

    private final UserBankDao userBankDao;
    private final UserBankDataSource dataSource;
    private final UserBankMapper mapper;
    private final UserContext userContext;
    private final BankAuthNotifier bankAuthNotifier;

    @Inject
    public UserBankRepository(UserBankDao userBankDao, UserBankDataSource dataSource, UserBankMapper mapper,
                              UserContext userContext, BankAuthNotifier bankAuthNotifier, UserSubject userSubject) {
        this.userBankDao = userBankDao;
        this.dataSource = dataSource;
        this.mapper = mapper;
        this.userContext = userContext;
        this.bankAuthNotifier = bankAuthNotifier;

        // When bank authentication info is refreshed we also update it in local and remote database.
        this.bankAuthNotifier.getUpdatedAuthentication().subscribe(authentication -> {
            updateBankAuthInfo(authentication.getTokenType(), authentication.getExpiresAt(),
                    authentication.getAccessToken(), authentication.getRefreshToken(), true);
        });

        // Set bank authentication only if a current bank is set.
        userSubject.getBankSelectedSubject().subscribe(o -> Completable.fromAction(() -> {
            Authentication auth = userBankDao.getAuthByUserIdAndBankId(
                    userContext.getUserId().toString(), userContext.getBankId());
            this.bankAuthNotifier.updateBankAuthInfo(auth.getTokenType(),
                    auth.getExpiresAt(), auth.getAccessToken(),
                    auth.getRefreshToken());
        }).subscribeOn(Schedulers.io())
                .subscribe());
    }

    public Result<List<UserBank>> getUserBanksRemotely() {
        Result<List<UserBankDto>> result = dataSource.getUserBanks();
        if (result instanceof Result.Success) {
            return new Result.Success<>(mapper.toEntity(((Result.Success<List<UserBankDto>>) result).getData()));
        }
        return new Result.Error(((Result.Error) result).getError());
    }

    public void createUserBank(UserBank userBank, boolean createRemote) {
        this.userBankDao.upsertUserBanks(Collections.singletonList(userBank));
        if (createRemote) {
            this.dataSource.createUserBank(mapper.toDto(userBank));
        }
    }

    public void updateBankAuthInfo(String tokenType, Long expiresAt, String accessToken, String refreshToken,
                                   boolean updateEntity) {
        this.bankAuthNotifier.updateBankAuthInfo(tokenType, expiresAt, accessToken, refreshToken);

        if (updateEntity) {
            Completable.fromAction(() -> {
                UserBank userBank = new UserBank();
                userBank.setUserId(userContext.getUserId().toString());
                userBank.setBankId(userContext.getBankId());
                userBank.setTokenType(tokenType);
                userBank.setExpiresAt(expiresAt);
                userBank.setAccessToken(accessToken);
                userBank.setRefreshToken(refreshToken);
                dataSource.updateUserBank(mapper.toDto(userBank));
                userBankDao.updateUserBank(userBank);
            }).subscribeOn(Schedulers.io()).subscribe();
        }
    }

}
