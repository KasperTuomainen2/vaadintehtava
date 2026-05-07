package com.example.vaadin.views;

import com.example.vaadin.entity.Contract;
import com.example.vaadin.entity.Employee;
import com.example.vaadin.entity.Project;
import com.example.vaadin.repository.ContractRepository;
import com.example.vaadin.repository.DepartmentRepository;
import com.example.vaadin.repository.EmployeeRepository;
import com.example.vaadin.repository.ProjectRepository;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;


@Route(value = "projects", layout = MainLayout.class)
@PageTitle("Projektit")
public class ProjectView extends VerticalLayout {
    private final ProjectRepository repository;
    Grid<Project> grid = new Grid<>(Project.class);
    ProjectForm form;

    public ProjectView(ProjectRepository repository) {
        this.repository = repository;
        setSizeFull();

        configureGrid();
        configureForm();

        add(new Button("Lisää projekti", e -> addProject()) {{
            addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }}, getContent());

        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("nimi", "alkamisPaiva", "paattymisPaiva", "budjetti");
        grid.addColumn(Project::getKuvaus).setHeader("Kuvaus");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));

        grid.asSingleSelect().addValueChangeListener(event -> editProject(event.getValue()));
    }

    private void configureForm() {
        form = new ProjectForm();
        form.setWidth("25em");
        form.addSaveListener(this::saveProject);
        form.addDeleteListener(this::deleteProject);
        form.addCloseListener(e -> closeEditor());
    }

    private void saveProject(ProjectForm.SaveEvent event) {
        repository.save(event.getProject());
        updateList();
        closeEditor();
    }

    private void deleteProject(ProjectForm.DeleteEvent event) {
        repository.delete(event.getProject());
        updateList();
        closeEditor();
    }

    public void editProject(Project project) {
        if (project == null) {
            closeEditor();
        } else {
            form.setProject(project);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setProject(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addProject() {
        grid.asSingleSelect().clear();
        editProject(new Project());
    }

    private void updateList() {
        grid.setItems(repository.findAll());
    }

    // Tämä yhdistää Gridin ja Formin asettelun
    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassName("content");
        content.setSizeFull();
        return content;
    }

}