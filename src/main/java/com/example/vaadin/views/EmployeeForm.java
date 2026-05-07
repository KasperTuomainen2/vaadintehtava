package com.example.vaadin.views;

import com.example.vaadin.entity.Contract;
import com.example.vaadin.entity.Department;
import com.example.vaadin.entity.Employee;
import com.example.vaadin.entity.Project;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

import java.util.List;

public class EmployeeForm extends FormLayout {

    TextField etunimi = new TextField("Etunimi");
    TextField sukunimi = new TextField("Sukunimi");
    EmailField sahkoposti = new EmailField("Sähköposti");
    TextField titteli = new TextField("Titteli");
    TextField puhelin = new TextField("Puhelinnumero");

    ComboBox<Department> department = new ComboBox<>("Osasto");
//    MultiSelectComboBox<Project> projects = new MultiSelectComboBox<>("Projektit");

    MultiSelectComboBox<Project> projects = new MultiSelectComboBox<>("Projektit");

    // Sopimuksen tiedot (Räätälöidään tässä samalla)
    DatePicker alkamisPaiva = new DatePicker("Sopimus alkaa");
    NumberField palkka = new NumberField("Palkka (€)");
    TextField sopimustyyppi = new TextField("Sopimustyyppi");
    NumberField viikkotunnit = new NumberField("Viikkotunnit");

    Binder<Employee> binder = new BeanValidationBinder<>(Employee.class);

    // Napit
    Button save = new Button("Tallenna");
    Button delete = new Button("Poista");
    Button close = new Button("Peruuta");


    public EmployeeForm(List<Department> departments, List<Project> allProjects) {
        binder.forField(projects)
                .bind(Employee::getProjects, Employee::setProjects);
        binder.forField(alkamisPaiva)
                .bind(emp -> emp.getContract() != null ? emp.getContract().getAlkamisPaiva() : null,
                        (emp, value) -> {
                            if (value == null) return;
                            if (emp.getContract() == null) emp.setContract(new Contract());
                            emp.getContract().setAlkamisPaiva(value);
                            emp.getContract().setEmployee(emp);
                        });

        binder.forField(palkka)
                .bind(emp -> emp.getContract() != null ? emp.getContract().getPalkka() : null,
                        (emp, value) -> {
                            if (value == null) return;
                            if (emp.getContract() == null) {
                                emp.setContract(new Contract());
                            }
                            emp.getContract().setPalkka(value);
                            emp.getContract().setEmployee(emp);
                        });
        binder.forField(sopimustyyppi)
                .bind(emp -> emp.getContract() != null ? emp.getContract().getSopimustyyppi() : null,
                        (emp, value) -> {
                            if (value == null || value.trim().isEmpty()) return;
                            if (emp.getContract() == null) emp.setContract(new Contract());
                            emp.getContract().setSopimustyyppi(value);
                            emp.getContract().setEmployee(emp);
                        });
        binder.forField(viikkotunnit)
                .bind(emp -> emp.getContract() != null ? emp.getContract().getViikkotunnit() : null,
                        (emp, value) -> {
                            if (value == null) return;
                            if (emp.getContract() == null) emp.setContract(new Contract());
                            emp.getContract().setViikkotunnit(value);
                            emp.getContract().setEmployee(emp);
                        });
        binder.bindInstanceFields(this);

        department.setItems(departments);
        department.setItemLabelGenerator(Department::getNimi);

        projects.setItems(allProjects);
        projects.setItemLabelGenerator(Project::getNimi);

        add(etunimi, sukunimi, sahkoposti, titteli, puhelin,
                department,
                projects,
                alkamisPaiva, palkka, sopimustyyppi, viikkotunnit,
                createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickShortcut(Key.ENTER);
        close.addClickShortcut(Key.ESCAPE);

        // Napien toiminnallisuus (lähetetään tapahtumia, joita EmployeeView kuuntelee)
        save.addClickListener(event -> validateAndSave());
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    private void validateAndSave() {
        if (binder.isValid()) {
            fireEvent(new SaveEvent(this, binder.getBean()));
        }
    }

    public void setEmployee(Employee employee) {
        binder.setBean(employee);
        if (employee != null && employee.getContract() != null) {
            binder.readBean(employee);
        }
    }

    // --- Tapahtumien käsittely (Events) ---
    public static abstract class EmployeeFormEvent extends ComponentEvent<EmployeeForm> {
        private Employee employee;
        protected EmployeeFormEvent(EmployeeForm source, Employee employee) {
            super(source, false);
            this.employee = employee;
        }
        public Employee getEmployee() { return employee; }
    }

    public static class SaveEvent extends EmployeeFormEvent {
        SaveEvent(EmployeeForm source, Employee employee) { super(source, employee); }
    }

    public static class DeleteEvent extends EmployeeFormEvent {
        DeleteEvent(EmployeeForm source, Employee employee) { super(source, employee); }
    }

    public static class CloseEvent extends EmployeeFormEvent {
        CloseEvent(EmployeeForm source) { super(source, null); }
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }
}
