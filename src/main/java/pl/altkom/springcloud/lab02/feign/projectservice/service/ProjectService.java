package pl.altkom.springcloud.lab02.feign.projectservice.service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import lombok.RequiredArgsConstructor;
import pl.altkom.springcloud.lab02.feign.projectservice.client.EmployeeClient;
import pl.altkom.springcloud.lab02.feign.projectservice.controller.mapper.RequestMapper;
import pl.altkom.springcloud.lab02.feign.projectservice.controller.mapper.ResponseMapper;
import pl.altkom.springcloud.lab02.feign.projectservice.controller.model.CreateProjectRequest;
import pl.altkom.springcloud.lab02.feign.projectservice.controller.model.Employee;
import pl.altkom.springcloud.lab02.feign.projectservice.controller.model.Project;
import pl.altkom.springcloud.lab02.feign.projectservice.controller.model.UpdateProjectRequest;
import pl.altkom.springcloud.lab02.feign.projectservice.repository.ProjectRepository;

@RequiredArgsConstructor
@Service
public class ProjectService {

    private final EmployeeClient employeeClient;
    private final ProjectRepository projectRepository;

    public List<Project> getProjects() {
        final List<Employee> employees = pl.altkom.springcloud.lab02.feign.projectservice.client.mapper.ResponseMapper.map(employeeClient.getEmployees());
        return ResponseMapper.map(projectRepository.findAll(), employees);
    }

    public Project getProject(final Long projectId) {
        final pl.altkom.springcloud.lab02.feign.projectservice.repository.model.Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        final List<Employee> employees = pl.altkom.springcloud.lab02.feign.projectservice.client.mapper.ResponseMapper.map(employeeClient.getProjectEmployees(project.getId()));
        return ResponseMapper.map(project, employees);
    }

    public Project createProject(final CreateProjectRequest request) {
        final pl.altkom.springcloud.lab02.feign.projectservice.repository.model.Project savedProject = projectRepository.save(RequestMapper.bind(request));
        return ResponseMapper.map(savedProject, List.of());
    }

    public Project updateProject(final Long projectId, final UpdateProjectRequest request) {

        final pl.altkom.springcloud.lab02.feign.projectservice.repository.model.Project sourceProject = projectRepository.findById(projectId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        final pl.altkom.springcloud.lab02.feign.projectservice.repository.model.Project modifiedProject = projectRepository
                .save(RequestMapper.bind(request, sourceProject));
        final List<Employee> employees = pl.altkom.springcloud.lab02.feign.projectservice.client.mapper.ResponseMapper
                .map(employeeClient.getProjectEmployees(modifiedProject.getId()));
        return ResponseMapper.map(modifiedProject, employees);
    }
}
