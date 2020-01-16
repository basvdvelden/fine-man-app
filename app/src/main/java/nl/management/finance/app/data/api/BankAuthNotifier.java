package nl.management.finance.app.data.api;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import io.reactivex.rxjava3.subjects.PublishSubject;
import nl.management.finance.app.data.user.Authentication;

@Singleton
public class BankAuthNotifier {
    private static final String TAG = "BankAuthNotifier";
    private BehaviorSubject<Authentication> bankAuthInfo = BehaviorSubject.create();
    private PublishSubject<Authentication> newAuthInfo = PublishSubject.create();

    @Inject
    public BankAuthNotifier() {
    }

    public void updateBankAuthInfo(String tokenType, Long expiresAt, String accessToken, String refreshToken) {
        Authentication auth = new Authentication(accessToken, expiresAt, refreshToken, tokenType);
        this.bankAuthInfo.onNext(auth);
    }

    public BehaviorSubject<Authentication> getAuthentication() {
        return this.bankAuthInfo;
    }

    public void updateAuthentication(Authentication auth) {
        newAuthInfo.onNext(auth);
    }

    public PublishSubject<Authentication> getUpdatedAuthentication() {
        return newAuthInfo;
    }

}
