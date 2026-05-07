package com.example.vaadin.views;

import com.example.vaadin.entity.Contract;
import com.example.vaadin.entity.Department;
import com.example.vaadin.entity.Project;
import com.vaadin.flow.component.ComponentEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.NumberField;
import com.vaadin.flow.component.textfield.TextArea;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.BeanValidationBinder;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.shared.Registration;
import com.vaadin.flow.theme.lumo.LumoUtility;

public class ProjectForm extends FormLayout {
    TextField nimi = new TextField("Projektin nimi");
    TextArea kuvaus = new TextArea("Kuvaus");
    DatePicker alkamisPaiva = new DatePicker("Alkamispäivä");
    DatePicker paattymisPaiva = new DatePicker("Päättymispäivä");
    NumberField budjetti = new NumberField("Budjetti");

    Button save = new Button("Tallenna");
    Button delete = new Button("Poista");
    Button close = new Button("Peruuta");

    Binder<Project> binder = new BeanValidationBinder<>(Project.class);



    public ProjectForm() {
        binder.bindInstanceFields(this);
        add(nimi, kuvaus, alkamisPaiva, paattymisPaiva, budjetti, createButtonsLayout());
    }

    private HorizontalLayout createButtonsLayout() {
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_ERROR);

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

    public void setProject(Project project) {
        binder.setBean(project);
    }

    public static abstract class ProjectFormEvent extends ComponentEvent<ProjectForm> {
        private final Project project;

        protected ProjectFormEvent(ProjectForm source, Project project) {
            super(source, false);
            this.project = project;
        }

        public Project getProject() {
            return project;
        }
    }

    public static class SaveEvent extends ProjectFormEvent {
        SaveEvent(ProjectForm source, Project project) {
            super(source, project);
        }
    }

    public static class DeleteEvent extends ProjectFormEvent {
        DeleteEvent(ProjectForm source, Project project) {
            super(source, project);
        }
    }

    public static class CloseEvent extends ProjectFormEvent {
        CloseEvent(ProjectForm source) {
            super(source, null);
        }
    }

    public Registration addDeleteListener(ComponentEventListener<DeleteEvent> listener) {
        return addListener(DeleteEvent.class, listener);
    }

    public Registration addSaveListener(ComponentEventListener<SaveEvent> listener) {
        return addListener(SaveEvent.class, listener);
    }

    public Registration addCloseListener(ComponentEventListener<CloseEvent> listener) {
        return addListener(CloseEvent.class, listener);
    }

}