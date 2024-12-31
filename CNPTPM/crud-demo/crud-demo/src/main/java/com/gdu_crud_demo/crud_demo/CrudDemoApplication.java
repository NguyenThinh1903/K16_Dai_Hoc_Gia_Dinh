package com.gdu_crud_demo.crud_demo;

import com.gdu_crud_demo.crud_demo.dao.StudentDAO;
import com.gdu_crud_demo.crud_demo.entity.Student;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.List;

@SpringBootApplication
public class CrudDemoApplication {

	@Bean
	public CommandLineRunner commandLineRunner(StudentDAO studentDAO) {
		return runner ->  {
			System.out.println("Connected Database");
			CreateStudent(studentDAO);
//			System.out.println("Insert Student");
//			ViewStudent(studentDAO);
//			ViewAllStudent(studentDAO);
//			ViewAllName(studentDAO);
//			UpdateStudent(studentDAO);
//			DeleteStudent(studentDAO);
//			DeleteAllStudent(studentDAO);
//			System.out.println("Delete All Student");
		};

	}
	public void DeleteAllStudent(StudentDAO studentDAO) {
		int numberOfRowsDeleted = studentDAO.deleteAll();
		System.out.println("Delete" + numberOfRowsDeleted + "student");
	}
	public void DeleteStudent(StudentDAO studentDAO) {
		int id = 1;
		studentDAO.delete(id);
		System.out.println("Delete Student");
	}
	public void UpdateStudent(StudentDAO studentDAO) {
		Student student = studentDAO.findById(1);
		student.setFirstName("Thinh");
		student.setLastName("Nguyen");
		student.setEmail("NguyenThinh@email.com");
		studentDAO.update(student);
	}
	public void ViewStudent(StudentDAO studentDAO) {
		int id =3;
		Student student = studentDAO.findById(id);
		System.out.println(student);
	}
	public void ViewAllStudent(StudentDAO studentDAO) {
		List<Student>students = studentDAO.findAll();
		for (Student student : students) {
			System.out.println(student);
		}
	}
	public void ViewAllName(StudentDAO studentDAO) {
		List<Student>students = studentDAO.findByName("Nguyen");
		for (Student student : students) {
			System.out.println(student);
		}
	}
	public static void main(String[] args) {
		SpringApplication.run(CrudDemoApplication.class, args);
	}
	public void CreateStudent(StudentDAO studentDAO) {

		Student student = new Student("Nguyen", "THinh", "acczinsv5@gmail.com");
		studentDAO.save(student);
		Student student1 = new Student("Nguyen", "THinh", "acczinsv5@gmail.com");
		studentDAO.save(student1);
		Student student2 = new Student("Nguyen", "THinh", "acczinsv5@gmail.com");
		studentDAO.save(student2);
		Student student3 = new Student("Nguyen", "THinh", "acczinsv5@gmail.com");
		studentDAO.save(student3);
		Student student4 = new Student("Nguyen", "THinh", "acczinsv5@gmail.com");
		studentDAO.save(student4);
		Student student5 = new Student("Nguyen", "THinh", "acczinsv5@gmail.com");
		studentDAO.save(student5);
		Student student6 = new Student("Nguyen", "THinh", "acczinsv5@gmail.com");
		studentDAO.save(student6);
	}


}
