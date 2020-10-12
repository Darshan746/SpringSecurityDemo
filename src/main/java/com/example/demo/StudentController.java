package com.example.demo;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
public class StudentController {

    private static final List<Student> studentList = Arrays.asList(new Student(1,"Rama"), new Student(2,"Krishna"), new Student(3,"kalki"));

    @GetMapping(path="/api/v1/student/{studentId}")
    public Student getStudent(@PathVariable("studentId") Integer studentId){
        return studentList.stream().filter(student->studentId.equals(student.getStudentId()))
                .findFirst().orElseThrow(()->new IllegalStateException("Student not found with ID"+studentId));
    }
}
