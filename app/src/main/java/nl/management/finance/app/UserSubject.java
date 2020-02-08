package nl.management.finance.app;

import android.util.Log;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import nl.authentication.management.app.AppOptional;
import nl.management.finance.app.data.user.User;
import nl.management.finance.app.di.App;

@Singleton
public class UserSubject {
    private static final String TAG = "UserSubject";
    private final BehaviorSubject<AppOptional<User>> userSubject = BehaviorSubject.create();
    private final BehaviorSubject<AppOptional<String>> pinCodeSubject = BehaviorSubject.create();

    @Inject
    public UserSubject() {

    }

    public BehaviorSubject<AppOptional<User>> getUser() {
        return userSubject;
    }

    public BehaviorSubject<AppOptional<String>> getPin() {
        return pinCodeSubject;
    }

    public void setUser(User user) {
        Log.i(TAG, "user =" + user);
        AppOptional<User> optUser = new AppOptional<>(user);
        this.userSubject.onNext(optUser);
    }

    public void setPin(String pin) {
        Log.i(TAG, "pin was updated");
        pinCodeSubject.onNext(new AppOptional<>(pin));
    }
}
