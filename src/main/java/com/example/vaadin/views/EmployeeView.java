package com.example.vaadin.views;

import com.example.vaadin.entity.Department;
import com.example.vaadin.entity.Employee;
import com.example.vaadin.entity.Project;
import com.example.vaadin.repository.DepartmentRepository;
import com.example.vaadin.repository.ProjectRepository;
import com.example.vaadin.repository.EmployeeRepository;
import com.example.vaadin.service.EmployeeService;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.value.ValueChangeMode;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;

import java.util.stream.Collectors;

@PermitAll
@PageTitle("Työntekijät")
@Route(value = "", layout = MainLayout.class)
public class EmployeeView extends VerticalLayout {

    private final EmployeeService employeeService;

    private TextField filterText = new TextField();
    private ComboBox<Department> departmentFilter = new ComboBox<>();

    Grid<Employee> grid = new Grid<>(Employee.class);
    EmployeeForm form;

    public EmployeeView(EmployeeService employeeService) {
        this.employeeService = employeeService;
        addClassName("list-view");
        setSizeFull();

        configureGrid();
        configureForm();

        add(getToolbar(), getContent());

        updateList();
        closeEditor();
    }

    private Component getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.addClassNames("content");
        content.setSizeFull();
        return content;
    }

    private void configureForm() {
        form = new EmployeeForm(
                employeeService.findAllDepartments(),
                employeeService.findAllProjects()
        );
        form.setWidth("25em");

        form.addSaveListener(this::saveEmployee);
        form.addDeleteListener(this::deleteEmployee);
        form.addCloseListener(e -> closeEditor());
    }

    private void configureGrid() {
        grid.addClassNames("contact-grid");
        grid.setSizeFull();
        grid.setColumns("etunimi", "sukunimi", "sahkoposti");
        grid.addColumn(emp -> emp.getProjects() != null ?
                        emp.getProjects().stream()
                                .map(Project::getNimi)
                                .collect(Collectors.joining(", ")) : "-")
                .setHeader("Projektit");
        grid.addColumn(emp -> emp.getDepartment() != null ? emp.getDepartment().getNimi() : "-").setHeader("Osasto");
        grid.getColumns().forEach(col -> col.setAutoWidth(true));
        grid.addColumn(emp -> emp.getContract() != null ? emp.getContract().getAlkamisPaiva() : "-")
                .setHeader("Sopimus alkaa");
        grid.addColumn(emp -> emp.getContract() != null ? emp.getContract().getPalkka() + " €" : "-")
                .setHeader("Palkka");
        grid.addColumn(emp -> emp.getContract() != null ? emp.getContract().getSopimustyyppi() : "-")
                .setHeader("Sopimustyyppi");
        grid.addColumn(emp -> emp.getContract() != null ? emp.getContract().getViikkotunnit() : "-")
                .setHeader("Viikkotunnit");
        grid.asSingleSelect().addValueChangeListener(event -> editEmployee(event.getValue()));
    }

    private HorizontalLayout getToolbar() {
        filterText.setPlaceholder("Hae nimellä tai sähköpostilla...");
        filterText.setClearButtonVisible(true);
        filterText.setValueChangeMode(ValueChangeMode.LAZY);
        filterText.addValueChangeListener(e -> updateList());

        departmentFilter.setPlaceholder("Suodata osastolla...");
        departmentFilter.setItems(employeeService.findAllDepartments());
        departmentFilter.setItemLabelGenerator(Department::getNimi);
        departmentFilter.addValueChangeListener(e -> updateList());

        Button addEmployeeButton = new Button("Lisää työntekijä");
        addEmployeeButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addEmployeeButton.addClickListener(click -> addEmployee());

        var toolbar = new HorizontalLayout(filterText, departmentFilter, addEmployeeButton);
        toolbar.addClassName("toolbar");
        return toolbar;
    }

    private void saveEmployee(EmployeeForm.SaveEvent event) {
        employeeService.saveEmployee(event.getEmployee());
        updateList();
        closeEditor();
    }

    private void deleteEmployee(EmployeeForm.DeleteEvent event) {
        employeeService.deleteEmployee(event.getEmployee());
        updateList();
        closeEditor();
    }

    public void editEmployee(Employee employee) {
        if (employee == null) {
            closeEditor();
        } else {
            form.setEmployee(employee);
            form.setVisible(true);
            addClassName("editing");
        }
    }

    private void closeEditor() {
        form.setEmployee(null);
        form.setVisible(false);
        removeClassName("editing");
    }

    private void addEmployee() {
        grid.asSingleSelect().clear();
        editEmployee(new Employee());
    }

    private void updateList() {
        grid.setItems(employeeService.findEmployeesComplex(
                filterText.getValue(),
                departmentFilter.getValue()
        ));
    }
}