package com.example.vaadin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotEmpty
    private String nimi;

    private String kuvaus;

    private LocalDate alkamisPaiva;

    private LocalDate paattymisPaiva;

    @Min(0)
    private Double budjetti;

    @ManyToMany(mappedBy = "projects")
    private List<Employee> employees = new ArrayList<>();

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNimi() { return nimi; }
    public void setNimi(String nimi) { this.nimi = nimi; }

    public String getKuvaus() { return kuvaus; }
    public void setKuvaus(String kuvaus) { this.kuvaus = kuvaus; }

    public LocalDate getAlkamisPaiva() { return alkamisPaiva; }
    public void setAlkamisPaiva(LocalDate alkamisPaiva) { this.alkamisPaiva = alkamisPaiva; }

    public LocalDate getPaattymisPaiva() { return paattymisPaiva; }
    public void setPaattymisPaiva(LocalDate paattymisPaiva) { this.paattymisPaiva = paattymisPaiva; }

    public Double getBudjetti() { return budjetti; }
    public void setBudjetti(Double budjetti) { this.budjetti = budjetti; }

    public List<Employee> getEmployees() { return employees; }
    public void setEmployees(List<Employee> employees) { this.employees = employees; }
}