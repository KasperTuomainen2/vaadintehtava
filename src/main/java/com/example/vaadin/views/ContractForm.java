package com.example.vaadin.views;

import com.example.vaadin.entity.Contract;
import com.example.vaadin.entity.Department;
import com.example.vaadin.entity.Employee;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class ContractForm extends FormLayout {

    DatePicker alkamisPaiva = new DatePicker("Alkamispäivä");
    NumberField palkka = new NumberField("Kuukausipalkka");
    TextField sopimustyyppi = new TextField("Sopimustyyppi");
    NumberField viikkotunnit = new NumberField("Viikkotunnit");
    ComboBox<Employee> employee = new ComboBox<>("Työntekijä");

    Button save = new Button("Tallenna");
    Button delete = new Button("Poista");
    Button close = new Button("Peruuta");

    Binder<Contract> binder = new BeanValidationBinder<>(Contract.class);

    public ContractForm(List<Employee> employees) {
        employee.setItems(employees);
        employee.setItemLabelGenerator(emp -> emp.getEtunimi() + " " + emp.getSukunimi());
        binder.forField(employee).bind(Contract::getEmployee, Contract::setEmployee);
        binder.bindInstanceFields(this);

        add(alkamisPaiva, palkka, sopimustyyppi, viikkotunnit, employee, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

        save.addClickListener(event -> {
            if (binder.isValid()) fireEvent(new SaveEvent(this, binder.getBean()));
        });
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    public void setContract(Contract contract) { binder.setBean(contract); }

    public static abstract class ContractFormEvent extends ComponentEvent<ContractForm> {
        private Contract contract;
        protected ContractFormEvent(ContractForm source, Contract contract) { super(source, false); this.contract = contract; }
        public Contract getContract() { return contract; }
    }

    public static class SaveEvent extends ContractFormEvent { SaveEvent(ContractForm source, Contract contract) { super(source, contract); } }
    public static class DeleteEvent extends ContractFormEvent { DeleteEvent(ContractForm source, Contract contract) { super(source, contract); } }
    public static class CloseEvent extends ContractFormEvent { CloseEvent(ContractForm source) { super(source, null); } }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) { return addListener(SaveEvent.class, listener); }
    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) { return addListener(DeleteEvent.class, listener); }
    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) { return addListener(CloseEvent.class, listener); }
}