package com.example.studentbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/students")
public class StudentController {

    @Autowired
    private StudentRepository studentRepository;

    // Get all students
    @GetMapping
    public List<Student> getAllStudents() {
        return studentRepository.findAll();
    }

    // Add a new student
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // Update a student
    @PutMapping("/{id}")
    public Student updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        return studentRepository.findById(id)
                .map(student -> {
                    student.setName(updatedStudent.getName());
                    student.setRollNumber(updatedStudent.getRollNumber());
                    return studentRepository.save(student);
                })
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + id));
    }

    // Delete a student
    @DeleteMapping("/{id}")
    public String deleteStudent(@PathVariable Long id) {
        studentRepository.deleteById(id);
        return "Deleted student with id " + id;
    }

    // 🔍 Search student by roll number (Query Parameter)
    @GetMapping("/search")
    public List<Student> searchStudent(
            @RequestParam(required = false) String rollNumber,
            @RequestParam(required = false) Long id) {

        if (rollNumber != null) {
            List<Student> students = studentRepository.findByRollNumber(rollNumber);
            if (students.isEmpty()) {
                throw new RuntimeException("No students found with roll number: " + rollNumber);
            }
            return students;
        } else if (id != null) {
            return studentRepository.findById(id)
                    .map(List::of)
                    .orElseThrow(() -> new RuntimeException("No student found with id: " + id));
        } else {
            throw new RuntimeException("Please provide either rollNumber or id as a query parameter");
        }
    }

}