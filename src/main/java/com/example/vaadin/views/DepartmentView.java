package com.example.vaadin.views;

import com.example.vaadin.entity.Department;
import com.example.vaadin.repository.DepartmentRepository;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

@PermitAll
@PageTitle("Osastot")
@Route(value = "departments", layout = MainLayout.class)
public class DepartmentView extends VerticalLayout {

    private final DepartmentRepository repository;
    Grid<Department> grid = new Grid<>(Department.class);
    DepartmentForm form;

    public DepartmentView(DepartmentRepository repository) {
        this.repository = repository;
        addClassName("department-view");
        setSizeFull();

        configureGrid();
        configureForm();

        add(getToolbar(), getContent());

        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("nimi", "kustannuspaikka", "rakennus", "kerros", "budjetti");
        grid.asSingleSelect().addValueChangeListener(event -> editDepartment(event.getValue()));
    }

    private void configureForm() {
        form = new DepartmentForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveDepartment);
        form.addDeleteListener(this::deleteDepartment);
        form.addCloseListener(e -> closeEditor());
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
    }

    private HorizontalLayout getToolbar() {
        Button addBtn = new Button("Lisää osasto");
        addBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addBtn.addClickListener(e -> addDepartment());
        return new HorizontalLayout(addBtn);
    }

    private void saveDepartment(DepartmentForm.SaveEvent event) {
        repository.save(event.getDepartment());
        updateList();
        closeEditor();
    }

    private void deleteDepartment(DepartmentForm.DeleteEvent event) {
        repository.delete(event.getDepartment());
        updateList();
        closeEditor();
    }

    private void editDepartment(Department department) {
        if (department == null) {
            closeEditor();
        } else {
            form.setDepartment(department);
            form.setVisible(true);
        }
    }

    private void addDepartment() {
        grid.asSingleSelect().clear();
        editDepartment(new Department());
    }

    private void closeEditor() {
        form.setDepartment(null);
        form.setVisible(false);
    }

    private void updateList() {
        grid.setItems(repository.findAll());
    }
}