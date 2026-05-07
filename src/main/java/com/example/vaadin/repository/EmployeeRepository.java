package com.example.vaadin.repository;

import com.example.vaadin.entity.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface EmployeeRepository extends JpaRepository<Employee, Long>, JpaSpecificationExecutor<Employee> {
    // JpaSpecificationExecutor lisätään tässä jo valmiiksi,
    // jotta voimme myöhemmin tehdä sen vaativan Criteria API -suodatuksen.
}