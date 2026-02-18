

package com.example.studentbackend;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface StudentRepository extends JpaRepository<Student, Long> {
    // Change from single result to List
    List<Student> findByRollNumber(String rollNumber);
}
