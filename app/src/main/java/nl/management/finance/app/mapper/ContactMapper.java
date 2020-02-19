package nl.management.finance.app.mapper;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import nl.management.finance.app.UserContext;
import nl.management.finance.app.data.contact.Contact;
import nl.management.finance.app.data.contact.ContactDto;
import nl.management.finance.app.ui.contacts.ContactView;

public class ContactMapper {
    private final UserContext userContext;

    @Inject
    public ContactMapper(UserContext userContext) {
        this.userContext = userContext;
    }

    public List<ContactView> toView(List<Contact> contacts) {
        List<ContactView> result = new ArrayList<>();
        for (Contact contact: contacts) {
            ContactView view = new ContactView();
            view.setId(contact.getId());
            view.setIban(contact.getIban());
            view.setName(contact.getName());
            result.add(view);
        }
        return result;
    }

    public Contact toDomain(ContactView view) {
        return new Contact(view.getId(), userContext.getUserId().toString(), view.getName(), view.getIban());
    }

    public List<Contact> toDomain(List<ContactDto> dtos) {
        List<Contact> contacts = new ArrayList<>();
        for (ContactDto dto: dtos) {
            contacts.add(new Contact(userContext.getUserId().toString(), dto.getName(), dto.getIban()));
        }
        return contacts;
    }

    public ContactDto toDto(Contact contact) {
        ContactDto dto = new ContactDto();
        dto.setIban(contact.getIban());
        dto.setName(contact.getName());

        return dto;
    }
}
