package com.example.demo;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
public class StudentManagementController {
    private static final List<Student> studentList = Arrays.asList(new Student(1,"Rama"), new Student(2,"Krishna"), new Student(3,"kalki"));

    @GetMapping("/management/api/v1/students")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN', 'ROLE_ADMINTRAINEE')")
    public List<Student> getStudents() {
        return  studentList;
    }

    @RequestMapping(value = "/management/api/v1/students", method = RequestMethod.POST)
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public void insertStudent(@RequestBody Student student){
        System.out.println(student);
    }


    @DeleteMapping("{studentId}")
    @PreAuthorize("hasAuthority('course:read')")
    public void deleteStudent(@PathVariable("studentId") Integer studentId){
        System.out.println(studentId );
    }

}
