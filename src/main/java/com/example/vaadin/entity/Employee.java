package com.example.vaadin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class Employee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String etunimi;

    @NotEmpty
    private String sukunimi;

    @Email
    private String sahkoposti;

    @NotEmpty
    private String titteli;

    @NotEmpty
    private String puhelin;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "contract_id")
    private Contract contract;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "employee_project",
            joinColumns = @JoinColumn(name = "employee_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id")
    )
    private Set<Project> projects = new HashSet<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getEtunimi() { return etunimi; }
    public void setEtunimi(String etunimi) { this.etunimi = etunimi; }

    public String getSukunimi() { return sukunimi; }
    public void setSukunimi(String sukunimi) { this.sukunimi = sukunimi; }

    public String getSahkoposti() { return sahkoposti; }
    public void setSahkoposti(String sahkoposti) { this.sahkoposti = sahkoposti; }

    public String getTitteli() { return titteli; }
    public void setTitteli(String titteli) { this.titteli = titteli; }

    public String getPuhelin() { return puhelin; }
    public void setPuhelin(String puhelin) { this.puhelin = puhelin; }

    public Department getDepartment() { return department; }
    public void setDepartment(Department department) { this.department = department; }

    public Contract getContract() { return contract; }
    public void setContract(Contract contract) { this.contract = contract; }

    public Set<Project> getProjects() {
        return projects; }
    public void setProjects(Set<Project> projects) {
        this.projects = projects; }
}