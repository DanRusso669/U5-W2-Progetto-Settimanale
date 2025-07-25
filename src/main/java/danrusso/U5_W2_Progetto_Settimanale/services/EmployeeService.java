package danrusso.U5_W2_Progetto_Settimanale.services;

import danrusso.U5_W2_Progetto_Settimanale.entities.Employee;
import danrusso.U5_W2_Progetto_Settimanale.exceptions.BadRequestException;
import danrusso.U5_W2_Progetto_Settimanale.payloads.NewEmployeesDTO;
import danrusso.U5_W2_Progetto_Settimanale.payloads.NotFoundException;
import danrusso.U5_W2_Progetto_Settimanale.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class EmployeeService {
    @Autowired
    private EmployeeRepository employeeRepository;

    public Page<Employee> findAll(int pageNum, int pageSize, String sortBy) {
        if (pageSize > 10) pageSize = 10;
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        return this.employeeRepository.findAll(pageable);
    }

    public Employee saveEmployee(NewEmployeesDTO payload) {
        this.employeeRepository.findByEmail(payload.email()).ifPresent(employee -> {
            throw new BadRequestException(payload.email());
        });

        Employee newEmployee = new Employee(payload.username(), payload.name(), payload.surname(), payload.email());
        newEmployee.setAvatar("https://ui-avatars.com/api/?name=" + payload.name() + "+" + payload.surname());

        Employee savedEmployee = this.employeeRepository.save(newEmployee);
        System.out.println("New employee  with id " + savedEmployee.getEmployeeId() + " added successfully.");
        return savedEmployee;
    }

    public Employee findById(UUID employeeId) {
        return this.employeeRepository.findById(employeeId).orElseThrow(() -> new NotFoundException(employeeId, "Employee"));
    }

    public Employee findByIdAndUpdate(NewEmployeesDTO payload, UUID employeeId) {
        Employee found = this.findById(employeeId);

        if (!found.getEmail().equals(payload.email())) {
            this.employeeRepository.findByEmail(payload.email()).ifPresent(employee -> {
                throw new BadRequestException(payload.email());
            });
        }

        found.setUsername(payload.username());
        found.setName(payload.name());
        found.setSurname(payload.surname());
        found.setEmail(payload.email());
        found.setAvatar("https://ui-avatars.com/api/?name=" + payload.name() + "+" + payload.surname());

        Employee updatedEmp = this.employeeRepository.save(found);
        System.out.println("Employee with id " + found.getEmployeeId() + " updated successfully.");
        return updatedEmp;
    }
}
