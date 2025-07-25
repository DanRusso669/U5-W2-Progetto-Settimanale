package danrusso.U5_W2_Progetto_Settimanale.services;

import danrusso.U5_W2_Progetto_Settimanale.entities.Employee;
import danrusso.U5_W2_Progetto_Settimanale.exceptions.BadRequestException;
import danrusso.U5_W2_Progetto_Settimanale.payloads.NewEmployeesDTO;
import danrusso.U5_W2_Progetto_Settimanale.repositories.EmployeeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

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
}
