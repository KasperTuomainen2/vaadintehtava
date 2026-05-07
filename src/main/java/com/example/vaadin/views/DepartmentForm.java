package com.example.vaadin.views;

import com.example.vaadin.entity.Department;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;

public class DepartmentForm extends FormLayout {

    TextField nimi = new TextField("Osaston nimi");
    TextField kustannuspaikka = new TextField("Kustannuspaikka");
    TextField rakennus = new TextField("Rakennus");
    NumberField kerros = new NumberField("Kerros");
    NumberField budjetti = new NumberField("Budjetti");

    Button save = new Button("Tallenna");
    Button delete = new Button("Poista");
    Button close = new Button("Peruuta");

    Binder<Department> binder = new BeanValidationBinder<>(Department.class);

    public DepartmentForm() {
        addClassName("department-form");
        binder.forField(kerros)
                .withConverter(
                        doubleValue -> doubleValue != null ? doubleValue.intValue() : null, // UI -> Entity
                        intValue -> intValue != null ? intValue.doubleValue() : null       // Entity -> UI
                )
                .bind(Department::getKerros, Department::setKerros);
        binder.bindInstanceFields(this);

        add(nimi, kustannuspaikka, rakennus, kerros, budjetti, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);
        close.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        save.addClickListener(event -> {
            if (binder.isValid()) {
                fireEvent(new SaveEvent(this, binder.getBean()));
            }
        });
        delete.addClickListener(event -> fireEvent(new DeleteEvent(this, binder.getBean())));
        close.addClickListener(event -> fireEvent(new CloseEvent(this)));

        return new HorizontalLayout(save, delete, close);
    }

    public void setDepartment(Department department) {
        binder.setBean(department);
    }

    // Tapahtumatyypit (Save, Delete, Close) – voit kopioida nää EmployeeFormista ja vaihtaa tyypin Departmentiksi
    public static abstract class DepartmentFormEvent extends ComponentEvent<DepartmentForm> {
        private Department department;
        protected DepartmentFormEvent(DepartmentForm source, Department department) {
            super(source, false);
            this.department = department;
        }
        public Department getDepartment() { return department; }
    }

    public static class SaveEvent extends DepartmentFormEvent {
        SaveEvent(DepartmentForm source, Department department) { super(source, department); }
    }

    public static class DeleteEvent extends DepartmentFormEvent {
        DeleteEvent(DepartmentForm source, Department department) { super(source, department); }
    }

    public static class CloseEvent extends DepartmentFormEvent {
        CloseEvent(DepartmentForm source) { super(source, null); }
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