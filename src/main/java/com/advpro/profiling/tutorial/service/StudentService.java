package com.advpro.profiling.tutorial.service;

import com.advpro.profiling.tutorial.model.Student;
import com.advpro.profiling.tutorial.model.StudentCourse;
import com.advpro.profiling.tutorial.repository.StudentCourseRepository;
import com.advpro.profiling.tutorial.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author muhammad.khadafi
 */
@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private StudentCourseRepository studentCourseRepository;

    public List<StudentCourse> getAllStudentsWithCourses() {
        // Fetch all students and student courses
        List<Student> students = studentRepository.findAll();
        List<StudentCourse> allStudentCourses = studentCourseRepository.findAll();

        // Group student courses by student id
        Map<Long, List<StudentCourse>> studentCoursesMap = allStudentCourses.stream()
                .collect(Collectors.groupingBy(sc -> sc.getStudent().getId()));

        // Build the result
        List<StudentCourse> result = new ArrayList<>();
        for (Student student : students) {
            List<StudentCourse> studentCourses = studentCoursesMap.getOrDefault(student.getId(), Collections.emptyList());
            for (StudentCourse studentCourse : studentCourses) {
                StudentCourse newStudentCourse = new StudentCourse();
                newStudentCourse.setStudent(student);
                newStudentCourse.setCourse(studentCourse.getCourse());
                result.add(newStudentCourse);
            }
        }

        return result;
    }

    public Optional<Student> findStudentWithHighestGpa() {
        List<Student> students = studentRepository.findAll();
        Student highestGpaStudent = null;
        double highestGpa = 0.0;
        for (Student student : students) {
            if (student.getGpa() > highestGpa) {
                highestGpa = student.getGpa();
                highestGpaStudent = student;
            }
        }
        return Optional.ofNullable(highestGpaStudent);
    }

    public String joinStudentNames() {
        List<Student> students = studentRepository.findAll();
        String result = "";
        for (Student student : students) {
            result += student.getName() + ", ";
        }
        return result.substring(0, result.length() - 2);
    }
}

