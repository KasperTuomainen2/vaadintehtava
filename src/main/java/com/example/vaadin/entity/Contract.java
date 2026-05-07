package com.example.vaadin.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotEmpty;

import java.time.LocalDate;

@Entity
public class Contract {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDate alkamisPaiva;

    @Min(value = 0, message = "Palkka ei voi olla negatiivinen")
    private Double palkka;

    private String sopimustyyppi;

    @Min(value = 0)
    @Max(value = 40)
    private Double viikkotunnit;

    @Min(value = 0)
    private Integer koeaikaKuukausina;



    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDate getAlkamisPaiva() { return alkamisPaiva; }
    public void setAlkamisPaiva(LocalDate alkamisPaiva) { this.alkamisPaiva = alkamisPaiva; }

    public Double getPalkka() { return palkka; }
    public void setPalkka(Double palkka) { this.palkka = palkka; }

    public String getSopimustyyppi() { return sopimustyyppi; }
    public void setSopimustyyppi(String sopimustyyppi) { this.sopimustyyppi = sopimustyyppi; }

    public Double getViikkotunnit() { return viikkotunnit; }
    public void setViikkotunnit(Double viikkotunnit) { this.viikkotunnit = viikkotunnit; }

    public Integer getKoeaikaKuukausina() { return koeaikaKuukausina; }
    public void setKoeaikaKuukausina(Integer koeaikaKuukausina) { this.koeaikaKuukausina = koeaikaKuukausina; }

//    @OneToOne(mappedBy = "contract")

    @OneToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
}