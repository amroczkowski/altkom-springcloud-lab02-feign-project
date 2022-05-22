package pl.altkom.springcloud.lab02.feign.projectservice.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import pl.altkom.springcloud.lab02.feign.projectservice.client.model.Employee;

@FeignClient(value = "employee-service", url = "${pl.altkom.employee.url}")
public interface EmployeeClient {

    @GetMapping("/employee/project/{projectId}")
    List<Employee> getProjectEmployees(@PathVariable("projectId") final Long projectId);

    @GetMapping("/employee")
    List<Employee> getEmployees();
}
