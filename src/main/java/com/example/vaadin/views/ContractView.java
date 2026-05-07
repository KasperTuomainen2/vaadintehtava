package com.example.vaadin.views;

import com.example.vaadin.entity.Contract;
import com.example.vaadin.entity.Employee;
import com.example.vaadin.repository.ContractRepository;
import com.example.vaadin.repository.DepartmentRepository;
import com.example.vaadin.repository.EmployeeRepository;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import java.util.List;

@PageTitle("Sopimukset")
@Route(value = "contracts", layout = MainLayout.class)
public class ContractView extends VerticalLayout {

    private final ContractRepository repository;
    private final EmployeeRepository employeeRepository;
    Grid<Contract> grid = new Grid<>(Contract.class);
    ContractForm form;

    public ContractView(ContractRepository repository, EmployeeRepository employeeRepository) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
        setSizeFull();
        configureGrid();
        configureForm();

        add(new Button("Lisää sopimus", e -> addContract()) {{
            addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        }}, getContent());
        updateList();
        closeEditor();
    }

    private void configureGrid() {
        grid.setSizeFull();
        grid.setColumns("alkamisPaiva", "palkka", "sopimustyyppi", "viikkotunnit");
        grid.asSingleSelect().addValueChangeListener(e -> editContract(e.getValue()));
        grid.addColumn(contract -> {
            if (contract.getEmployee() == null) return "Ei määritetty";
            return contract.getEmployee().getEtunimi() + " " + contract.getEmployee().getSukunimi();
        }).setHeader("Työntekijä");
    }

    private void configureForm() {
        List<Employee> vapaatTyontekijat = employeeRepository.findAll().stream()
                .filter(emp -> emp.getContract() == null)
                .toList();

        form = new ContractForm(vapaatTyontekijat);
        form.setWidth("25em");
        form.addSaveListener(this::saveContract);
        form.addDeleteListener(this::deleteContract);
        form.addCloseListener(e -> closeEditor());
    }

    private HorizontalLayout getContent() {
        HorizontalLayout content = new HorizontalLayout(grid, form);
        content.setFlexGrow(2, grid);
        content.setFlexGrow(1, form);
        content.setSizeFull();
        return content;
    }

    private void saveContract(ContractForm.SaveEvent event) {
        Contract contract = event.getContract();
        Employee employee = contract.getEmployee();

        if (employee != null) {
            // TARKISTUS: Jos työntekijällä on jo eri sopimus, poistetaan se ensin
            // tai varmistetaan, että liitetään tämä uusi oikein.
            if (employee.getContract() != null && !employee.getContract().equals(contract)) {
                // Valinnainen: poista vanha sopimus repositoryn kautta tai
                // estä tallennus virheilmoituksella (Notification).
            }

            employee.setContract(contract);
            contract.setEmployee(employee); // Varmistetaan molemmat suunnat
        }

        repository.save(contract);
        // Huom: Jos Employee on "owner", tallenna se myös
        if (employee != null) {
            employeeRepository.save(employee);
        }

        updateList();
        closeEditor();
    }

    private void deleteContract(ContractForm.DeleteEvent event) {
        repository.delete(event.getContract());
        updateList();
        closeEditor();
    }

    private void editContract(Contract contract) {
        if (contract == null) closeEditor();
        else {
            form.setContract(contract);
            form.setVisible(true);
        }
    }

    private void addContract() {
        grid.asSingleSelect().clear();
        editContract(new Contract());
    }

    private void closeEditor() {
        form.setContract(null);
        form.setVisible(false);
    }

    private void updateList() { grid.setItems(repository.findAll()); }
}