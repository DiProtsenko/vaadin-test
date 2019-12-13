package dev.protsenko.vaadin.web;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NativeButtonRenderer;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import dev.protsenko.vaadin.entities.Contact;
import dev.protsenko.vaadin.repositories.ContactRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.PostConstruct;
import java.util.List;

@Route("contacts")
public class ContactList extends AppLayout {

    VerticalLayout layout;
    Grid<Contact> grid;
    RouterLink linkCreate;

    @Autowired
    ContactRepository contactRepository;

    public ContactList() {
        layout = new VerticalLayout();
        grid = new Grid<>();
        linkCreate = new RouterLink("Создать контакт", ManageContact.class, 0);
        layout.add(linkCreate);
        layout.add(grid);
        addToNavbar(new H3("Список контактов"));
        setContent(layout);
    }

    @PostConstruct
    public void fillGrid() {
        List<Contact> contacts = contactRepository.findAll();
        if (!contacts.isEmpty()) {

            //Выведем столбцы в нужном порядке
            grid.addColumn(Contact::getFirstName).setHeader("Имя");
            grid.addColumn(Contact::getSecondName).setHeader("Фамилия");
            grid.addColumn(Contact::getFatherName).setHeader("Отчество");
            grid.addColumn(Contact::getNumberPhone).setHeader("Номер");
            grid.addColumn(Contact::getEmail).setHeader("E-mail");
            //Добавим кнопку удаления и редактирования
            grid.addColumn(new NativeButtonRenderer<>("Редактировать", contact -> {
                UI.getCurrent().navigate(ManageContact.class, contact.getId());
            }));
            grid.addColumn(new NativeButtonRenderer<>("Удалить", contact -> {
                Dialog dialog = new Dialog();
                Button confirm = new Button("Удалить");
                Button cancel = new Button("Отмена");
                dialog.add("Вы уверены что хотите удалить контакт?");
                dialog.add(confirm);
                dialog.add(cancel);

                confirm.addClickListener(clickEvent -> {
                    contactRepository.delete(contact);
                    dialog.close();
                    Notification notification = new Notification("Контакт удален", 1000);
                    notification.setPosition(Notification.Position.MIDDLE);
                    notification.open();

                    grid.setItems(contactRepository.findAll());

                });

                cancel.addClickListener(clickEvent -> {
                    dialog.close();
                });

                dialog.open();

            }));

            grid.setItems(contacts);
        }
    }


}
