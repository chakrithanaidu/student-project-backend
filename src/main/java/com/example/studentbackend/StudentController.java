package com.example.studentbackend;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "https://student-project-frontend.netlify.app")
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

    // Add new student
    @PostMapping
    public Student createStudent(@RequestBody Student student) {
        return studentRepository.save(student);
    }

    // Update student
    @PutMapping("/{id}")
    public ResponseEntity<?> updateStudent(@PathVariable Long id, @RequestBody Student updatedStudent) {
        Optional<Student> optionalStudent = studentRepository.findById(id);

        if (optionalStudent.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student not found with id: " + id);
        }

        Student student = optionalStudent.get();
        student.setName(updatedStudent.getName());
        student.setRollNumber(updatedStudent.getRollNumber());

        return ResponseEntity.ok(studentRepository.save(student));
    }

    // Delete student
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteStudent(@PathVariable Long id) {
        if (!studentRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Student not found with id: " + id);
        }

        studentRepository.deleteById(id);
        return ResponseEntity.ok("Deleted student with id " + id);
    }

    // Search student (FIXED - no generics error)
    @GetMapping("/search")
    public ResponseEntity<?> searchStudent(
            @RequestParam(required = false) String rollNumber,
            @RequestParam(required = false) Long id) {

        // Search by roll number
        if (rollNumber != null) {
            List<Student> students = studentRepository.findByRollNumber(rollNumber);

            if (students.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No students found with roll number: " + rollNumber);
            }

            return ResponseEntity.ok(students);
        }

        // Search by ID (FIXED PROPERLY)
        if (id != null) {
            Optional<Student> student = studentRepository.findById(id);

            if (student.isPresent()) {
                return ResponseEntity.ok(student.get());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No student found with id: " + id);
            }
        }

        return ResponseEntity.badRequest()
                .body("Please provide rollNumber or id");
    }
}