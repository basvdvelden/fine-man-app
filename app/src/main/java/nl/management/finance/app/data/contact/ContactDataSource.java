package nl.management.finance.app.data.contact;


import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.AppDataSource;
import nl.management.finance.app.data.api.ContactApi;
import retrofit2.Response;

class ContactDataSource extends AppDataSource {
    private static final String TAG = "ContactDataSource";
    private final ContactApi mApi;
    private final UserContext mUserContext;

    @Inject
    public ContactDataSource(ContactApi api, UserContext userContext) {
        mApi = api;
        mUserContext = userContext;
    }

    public void createContact(ContactDto dto) {
        try {
            Response<Void> response = mApi.createContact(mUserContext.getUserId().toString(), dto)
                    .clone().execute();
            if (!response.isSuccessful()) {
                throw new IOException(getErrMsg(response));
            }
        } catch (IOException e) {
            Log.e(TAG, "io error creating contact:", e);
        }
    }

    public List<ContactDto> getContacts() {
        try {
            Response<List<ContactDto>> response = mApi.getContacts(mUserContext.getUserId().toString())
                    .clone().execute();
            if (!response.isSuccessful()) {
                throw new IOException(getErrMsg(response));
            }
            return response.body();
        } catch (IOException e) {
            Log.e(TAG, "io error getting contacts:", e);
        }
        return new ArrayList<>();
    }

    public void deleteContact(String iban) {
        try {
            Response<Void> response = mApi.deleteContact(mUserContext.getUserId().toString(), iban)
                    .clone().execute();
            if (!response.isSuccessful()) {
                throw new IOException(getErrMsg(response));
            }
        } catch (IOException e) {
            Log.e(TAG, "io error deleting contact:", e);
        }
    }
}
