package nl.management.finance.app.data.contact;

import android.util.Log;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.mapper.ContactMapper;

@Singleton
public class ContactRepository {
    private static final String TAG = "ContactRepository";
    private final ContactDao mDao;
    private final ContactDataSource mDataSource;
    private final ContactMapper mMapper;
    private final UserContext mUserContext;
    private LiveData<List<Contact>> mContacts;

    @Inject
    public ContactRepository(ContactDao dao, ContactDataSource dataSource, ContactMapper mapper,
                             UserContext userContext) {
        mDao = dao;
        mDataSource = dataSource;
        mMapper = mapper;
        mUserContext = userContext;
        mContacts = mDao.getContacts(userContext.getUserId().toString());
    }

    public LiveData<List<Contact>> getContacts() {
        try {
            return mContacts;
        } finally {
            Completable.fromAction(this::refreshContacts)
                    .subscribeOn(Schedulers.io()).subscribe();
        }
    }

    private void refreshContacts() {
        mDao.insert(mMapper.toDomain(mDataSource.getContacts()));
    }

    public void deleteContact(Contact contact) {
        mDao.delete(contact);
        mDataSource.deleteContact(contact.getIban());
    }

    public Contact getByIban(String iban) {
        return mDao.getByIban(iban);
    }

    public void save(Contact contact) {
        mDao.insert(contact);
        mDataSource.createContact(mMapper.toDto(contact));
    }
}
