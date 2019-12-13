package dev.protsenko.vaadin.web;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.BeforeEvent;
import com.vaadin.flow.router.HasUrlParameter;
import com.vaadin.flow.router.Route;
import dev.protsenko.vaadin.entities.Contact;
import dev.protsenko.vaadin.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

@Route("manageContact")
public class ManageContact extends AppLayout implements HasUrlParameter<Integer> {

    Integer id;
    FormLayout contactForm;
    TextField firstName;
    TextField secondName;
    TextField fatherName;
    TextField numberPhone;
    TextField email;
    Button saveContact;

    @Autowired
    ContactRepository contactRepository;

    public ManageContact() {
        //Создаем объекты для формы
        contactForm = new FormLayout();
        firstName = new TextField("Имя");
        secondName = new TextField("Фамилия");
        fatherName = new TextField("Отчество");
        numberPhone = new TextField("Номер телефона");
        email = new TextField("Электронная почта");
        saveContact = new Button("Сохранить");
        //Добавим все элементы на форму
        contactForm.add(firstName, secondName, fatherName, numberPhone, email, saveContact);
        setContent(contactForm);
    }

    @Override
    public void setParameter(BeforeEvent beforeEvent, Integer contactId) {
        id = contactId;
        if (!id.equals(0)) {
            addToNavbar(new H3("Редактирование контакта"));
        } else {
            addToNavbar(new H3("Создание контакта"));
        }
        fillForm();
    }


    public void fillForm() {

        if (!id.equals(0)) {
            Optional<Contact> contact = contactRepository.findById(id);
            contact.ifPresent(x -> {
                firstName.setValue(x.getFirstName());
                secondName.setValue(x.getSecondName());
                fatherName.setValue(x.getFatherName());
                numberPhone.setValue(x.getNumberPhone());
                email.setValue(x.getEmail());
            });
        }

        saveContact.addClickListener(clickEvent -> {
            //Создадим объект контакта получив значения с формы
            Contact contact = new Contact();
            if (!id.equals(0)) {
                contact.setId(id);
            }
            contact.setFirstName(firstName.getValue());
            contact.setSecondName(secondName.getValue());
            contact.setFatherName(fatherName.getValue());
            contact.setEmail(email.getValue());
            contact.setNumberPhone(numberPhone.getValue());
            contactRepository.save(contact);

            //Выведем уведомление пользователю и переведем его к списку контактов
            Notification notification = new Notification(id.equals(0) ? "Контакт успешно создан" : "Контакт был изменен", 1000);
            notification.setPosition(Notification.Position.MIDDLE);
            notification.addDetachListener(detachEvent -> {
                UI.getCurrent().navigate(ContactList.class);
            });
            contactForm.setEnabled(false);
            notification.open();
        });
    }
}
