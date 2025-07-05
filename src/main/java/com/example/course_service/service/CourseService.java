package com.example.course_service.service;

import com.example.course_service.entity.Course;
import com.example.course_service.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
@RequiredArgsConstructor
public class CourseService {
    private final CourseRepository courseRepository;
    public List<Course> getAllCourses() {
        return courseRepository.findAll();
    }
    public Course createCourse(Course course) {
        course.setStatus("PENDING");
        return courseRepository.save(course);
    }
    public Course approveCourse(Long id) {
        Course course = courseRepository.findById(id).orElseThrow();
        course.setStatus("APPROVED");
        return courseRepository.save(course);
    }
}
