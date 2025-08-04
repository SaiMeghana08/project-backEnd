package com.example.project.placement.DataLoader;

import com.example.project.placement.Dto.*;
import com.example.project.placement.Entity.*;
import com.example.project.placement.Repository.*;
import com.fasterxml.jackson.databind.MappingIterator;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private UserRepo userRepo;
    @Autowired private AdminRepo adminRepo;
    @Autowired private StudentRepo studentRepo;
    @Autowired private CompanyRepo companyRepo;
    @Autowired private JobRepo jobRepo;
    @Autowired private ApplicationRepo applicationRepo;

    private final CsvMapper mapper = new CsvMapper();


    @Override
    public void run(String... args) throws Exception {
        loadData("/Data/users.csv", User.class, userRepo);
        loadAdmins();
        loadStudents();
        loadCompanies();
        loadJobs();
        loadApplications();
    }
    private void loadStudents() throws Exception {
        InputStream is = getClass().getResourceAsStream("/Data/students.csv");
        if (is == null) {
            System.err.println("students.csv not found!");
            return;
        }

        CsvSchema schema = mapper.schemaFor(StudentDto.class).withHeader().withColumnReordering(true);
        MappingIterator<StudentDto> iterator = mapper.readerFor(StudentDto.class)
                .with(schema)
                .readValues(is);

        List<StudentDto> dtos = iterator.readAll();

        List<Student> students = dtos.stream().map(dto -> {
            Student student = new Student();
            student.setId(dto.getId());
            student.setName(dto.getName());
            student.setBranch(dto.getBranch());
            student.setCgpa(dto.getCgpa());
            student.setBatchYear(dto.getBatch_year());

            // fetch User object by ID
            User user = userRepo.findById(dto.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUser_id()));
            student.setUser(user);

            return student;
        }).toList();

        studentRepo.saveAll(students);
    }
    private void loadCompanies() throws Exception {
        InputStream is = getClass().getResourceAsStream("/Data/companies.csv");
        if (is == null) {
            System.err.println("companies.csv not found!");
            return;
        }

        CsvSchema schema = mapper.schemaFor(CompanyDto.class).withHeader().withColumnReordering(true);
        MappingIterator<CompanyDto> iterator = mapper.readerFor(CompanyDto.class).with(schema).readValues(is);
        List<CompanyDto> dtos = iterator.readAll();

        List<Company> companies = dtos.stream().map(dto -> {
            Company company = new Company();
            company.setId(dto.getId());
            company.setName(dto.getName());
            company.setContactEmail(dto.getContact_email());

            Admin admin = adminRepo.findById(dto.getAdded_by_admin_id())
                    .orElseThrow(() -> new RuntimeException("Admin not found with ID: " + dto.getAdded_by_admin_id()));
            company.setAddedBy(admin);
            return company;
        }).toList();

        companyRepo.saveAll(companies);
    }
    private void loadApplications() throws Exception {
        InputStream is = getClass().getResourceAsStream("/Data/applications.csv");
        if (is == null) {
            System.err.println("applications.csv not found!");
            return;
        }

        CsvSchema schema = mapper.schemaFor(ApplicationsDto.class).withHeader().withColumnReordering(true);
        MappingIterator<ApplicationsDto> iterator = mapper.readerFor(ApplicationsDto.class).with(schema).readValues(is);
        List<ApplicationsDto> dtos = iterator.readAll();

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        List<Application> applications = dtos.stream().map(dto -> {
            Application app = new Application();
            app.setId(dto.getId());
            app.setStatus(dto.getStatus());

            // parse datetime
            app.setAppliedAt(LocalDateTime.parse(dto.getApplied_at(), formatter));

            Student student = studentRepo.findById(dto.getStudent_id())
                    .orElseThrow(() -> new RuntimeException("Student not found: " + dto.getStudent_id()));
            Job job = jobRepo.findById(dto.getJob_id())
                    .orElseThrow(() -> new RuntimeException("Job not found: " + dto.getJob_id()));

            app.setStudent(student);
            app.setJob(job);

            return app;
        }).toList();

        applicationRepo.saveAll(applications);
    }

    private void loadJobs() throws Exception {
        InputStream is = getClass().getResourceAsStream("/Data/jobs.csv");
        if (is == null) {
            System.err.println("jobs.csv not found!");
            return;
        }

        CsvSchema schema = mapper.schemaFor(JobDto.class).withHeader().withColumnReordering(true);
        MappingIterator<JobDto> iterator = mapper.readerFor(JobDto.class).with(schema).readValues(is);
        List<JobDto> dtos = iterator.readAll();

        List<Job> jobs = dtos.stream().map(dto -> {
            Job job = new Job();
            job.setId(dto.getId());
            job.setTitle(dto.getTitle());
            job.setDescription(dto.getDescription());
            job.setLocation(dto.getLocation());
            job.setSalary(dto.getSalary());
            job.setEligibilityCriteria(dto.getEligibility_criteria());

            Company company = companyRepo.findById(dto.getCompany_id())
                    .orElseThrow(() -> new RuntimeException("Company not found: " + dto.getCompany_id()));
            job.setCompany(company);

            return job;
        }).toList();

        jobRepo.saveAll(jobs);
    }
    private void loadAdmins() throws Exception {
        InputStream is = getClass().getResourceAsStream("/Data/admins.csv");
        if (is == null) {
            System.err.println("admins.csv not found!");
            return;
        }

        CsvSchema schema = mapper.schemaFor(AdminDto.class).withHeader().withColumnReordering(true);
        MappingIterator<AdminDto> iterator = mapper.readerFor(AdminDto.class).with(schema).readValues(is);
        List<AdminDto> dtos = iterator.readAll();

        List<Admin> admins = dtos.stream().map(dto -> {
            Admin admin = new Admin();
            admin.setId(dto.getId());
            admin.setName(dto.getName());
            admin.setDepartment(dto.getDepartment());

            User user = userRepo.findById(dto.getUser_id())
                    .orElseThrow(() -> new RuntimeException("User not found: " + dto.getUser_id()));
            admin.setUser(user);

            return admin;
        }).toList();

        adminRepo.saveAll(admins);
    }

    private <T> void loadData(String path, Class<T> clazz, JpaRepository<T, ?> repo) throws Exception {
        InputStream is = getClass().getResourceAsStream(path);
        if (is == null) {
            System.err.println("❌ CSV file not found at: " + path);
            return;
        }

        try {
            CsvSchema schema = mapper.schemaFor(clazz).withHeader().withColumnReordering(true);
            List<T> data = (List<T>) mapper.readerFor(clazz).with(schema).readValues(is).readAll();
            repo.saveAll(data);
            System.out.println("✅ Loaded: " + path + " -> " + data.size() + " records");
        } catch (Exception e) {
            System.err.println("❌ Failed to load: " + path + " (" + clazz.getSimpleName() + ")");
            e.printStackTrace();
        }
    }
}
