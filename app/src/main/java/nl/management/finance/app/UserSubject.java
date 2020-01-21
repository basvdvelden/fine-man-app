package nl.management.finance.app;

import java.util.Optional;

import javax.inject.Inject;
import javax.inject.Singleton;

import io.reactivex.rxjava3.subjects.BehaviorSubject;
import nl.management.finance.app.data.user.User;

@Singleton
public class UserSubject {
    private final BehaviorSubject<AppOptional<User>> userSubject = BehaviorSubject.create();

    @Inject
    public UserSubject() {

    }

    public BehaviorSubject<AppOptional<User>> get() {
        return userSubject;
    }

    public void setUser(User user) {
        AppOptional<User> optUser = new AppOptional<>(user);
        this.userSubject.onNext(optUser);
    }
}
