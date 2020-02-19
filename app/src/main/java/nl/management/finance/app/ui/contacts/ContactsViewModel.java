package nl.management.finance.app.ui.contacts;

import android.util.Log;

import java.nio.channels.ShutdownChannelGroupException;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import nl.management.finance.app.data.contact.Contact;
import nl.management.finance.app.data.contact.ContactRepository;
import nl.management.finance.app.mapper.ContactMapper;

@Singleton
public class ContactsViewModel extends ViewModel {
    private static final String TAG = "ContactsViewModel";

    private final ContactRepository mRepository;
    private final ContactMapper mMapper;
    private final Observer mObserver;
    private MutableLiveData<List<ContactView>> mContacts = new MutableLiveData<>();
    private MutableLiveData<ContactView> mSelectedContact = new MutableLiveData<>();

    @Inject
    public ContactsViewModel(ContactRepository repository, ContactMapper mapper) {
        mRepository = repository;
        mMapper = mapper;
        mObserver = (Observer<List<Contact>>) contacts -> mContacts.setValue(mapper.toView(contacts));
        mRepository.getContacts().observeForever(mObserver);
    }

    public LiveData<List<ContactView>> getContacts() {
        return mContacts;
    }

    public LiveData<ContactView> getSelectedContact() {
        return mSelectedContact;
    }

    void selectContact(ContactView contact) {
        mSelectedContact.setValue(contact);
    }

    void deleteContact(ContactView contact) {
        Completable.fromAction(() -> {
            mRepository.deleteContact(mMapper.toDomain(contact));
        }).subscribeOn(Schedulers.io()).subscribe();
    }

    @Override
    public void onCleared() {
        mRepository.getContacts().removeObserver(mObserver);
    }

    void createContact(ContactView contact) {
        Completable.fromAction(() -> mRepository.save(mMapper.toDomain(contact)))
                .subscribeOn(Schedulers.io()).subscribe();
    }

    public void createContactIfNotExists(String iban, String name) {
        Completable.fromAction(() -> {
            if (mRepository.getByIban(iban) == null) {
                Log.i(TAG, "Creating new contact...");
                ContactView view = new ContactView();
                view.setName(name);
                view.setIban(iban);
                mRepository.save(mMapper.toDomain(view));
            }
        }).subscribeOn(Schedulers.io()).subscribe();
    }
}
