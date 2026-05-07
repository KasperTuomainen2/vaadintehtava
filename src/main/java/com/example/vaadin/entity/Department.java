package com.example.vaadin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String nimi;

    @NotEmpty(message = "Kustannuspaikka on pakollinen")
    private String kustannuspaikka;

    private String rakennus;

    @Min(value = 1, message = "Kerroksen on oltava vähintään 1")
    @Max(value = 10, message = "Kerros ei voi olla yli 10")
    private Integer kerros;

    private Double budjetti;

    @OneToMany(mappedBy = "department")
    private List<Employee> employees = new ArrayList<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNimi() {
        return nimi;
    }

    public void setNimi(String nimi) {
        this.nimi = nimi;
    }

    public String getKustannuspaikka() {
        return kustannuspaikka;
    }

    public void setKustannuspaikka(String kustannuspaikka) {
        this.kustannuspaikka = kustannuspaikka;
    }

    public String getRakennus() {
        return rakennus;
    }

    public void setRakennus(String rakennus) {
        this.rakennus = rakennus;
    }

    public Integer getKerros() {
        return kerros;
    }

    public void setKerros(Integer kerros) {
        this.kerros = kerros;
    }

    public Double getBudjetti() {
        return budjetti;
    }

    public void setBudjetti(Double budjetti) {
        this.budjetti = budjetti;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }
}