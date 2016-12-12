package org.aman.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.aman.entity.Employee;
import org.aman.entity.QDepartment;
import org.aman.entity.QEmployee;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;


@Component
public class Repository {
	private final Logger logger = LoggerFactory.getLogger(Repository.class);
	
	@PersistenceContext
	EntityManager entityManager;
	
	@Bean
    public JPAQueryFactory jpaQueryFactory() 
	{
        return new JPAQueryFactory(entityManager);
    }

	public Employee getEmpNameById(Integer id) 
	{
	  QEmployee employee = QEmployee.employee;
	  JPAQueryFactory  queryFactory = jpaQueryFactory();
	  Employee emp = queryFactory.selectFrom(employee)
			  								.where(employee.id.eq(id))
			  								.fetchOne();
      return emp;
	}
	
	public Employee getEmpNameByName(String name) 
	{
		  QEmployee employee = QEmployee.employee;
		  JPAQueryFactory  queryFactory = jpaQueryFactory();
		  Employee emp = queryFactory.selectFrom(employee)
												.where(employee.name.eq(name))
												.fetchOne();
	      return emp;
	   
	}
	
	public List<Employee> getEmployeeList(boolean withOrder) 
	{
		QEmployee employee = QEmployee.employee;
	    JPAQueryFactory  queryFactory = jpaQueryFactory();
     	List<Employee> empList;
     	
		if(withOrder)
		{
		   empList = queryFactory.selectFrom(employee)
  							     .orderBy(employee.name.asc())
  							     .fetch();
		}
		else
		{
		   empList = queryFactory.selectFrom(employee)
				   				 .orderBy(employee.id.asc())
					             .fetch();
		}
		logger.info("EmployeeList:: {}", empList);
	    return empList;
	}

	public String getEmpBydeptId(String deptId) 
	{
		QEmployee employee = QEmployee.employee;
		QDepartment department = QDepartment.department;
		JPAQueryFactory queryFactory = jpaQueryFactory();
		logger.info("Inside repository class");
		
	   List<Tuple> result = queryFactory.select(employee.id, employee.name, employee.email, department.dept_name)
										.from(employee)
										.leftJoin(employee.department,department)
										.where(department.dept_id.eq(deptId))
										.fetch(); //working
	   
	   /*List<Tuple> result = queryFactory.select(employee.id, employee.name, employee.email, department.dept_name)
										.from(employee)
										.leftJoin(department)
										.on(employee.department.eq(department))
										.where(department.dept_id.eq(deptId))
										.fetch(); // Not working*/
									    
		
		logger.info("results:{}", result);
		StringBuilder strbr = new StringBuilder();
		for(Tuple row : result)
		{
			strbr.append(row.get(employee.id));
			strbr.append(" :: ");
			strbr.append(row.get(employee.name));
			strbr.append(" :: ");
			strbr.append(row.get(employee.email));
			strbr.append(" :: ");
			strbr.append(row.get(department.dept_name));
			strbr.append("\n");
			logger.info("hhhh:::: {}", strbr.toString());
		}
		return strbr.toString();
	}

	public String getEmployeeCountByDept() 
	{
		QEmployee employee = QEmployee.employee;
		QDepartment department = QDepartment.department;
		JPAQueryFactory  queryFactory = jpaQueryFactory();
		List<Tuple> result = queryFactory.select(department.dept_name, employee.count())
										 .from(employee)
										 .leftJoin(employee.department,department)
										 .groupBy(department.dept_id)
										 .fetch();
		
		StringBuilder strbr = new StringBuilder();
		for(Tuple row : result)
		{
			strbr.append(row.get(department.dept_name));
			strbr.append(" :: ");
			strbr.append(row.get(employee.count()));
			strbr.append("\n");
			logger.info("Deptwise Count:::: {}", strbr.toString());
		}
		return strbr.toString();
	}

	@Transactional
	public void updateSalaryForEmpId(Integer id, String salary) 
	{
		QEmployee employee = QEmployee.employee;
		JPAQueryFactory  queryFactory = jpaQueryFactory();
		queryFactory.update(employee)
					.where(employee.id.eq(id))
					.set(employee.salary,salary)
				    .execute();
	}

	@Transactional
	public boolean deleteEmployeeByEmpId(Integer id) 
	{
		QEmployee employee = QEmployee.employee;
		JPAQueryFactory  queryFactory = jpaQueryFactory();
		Long empId = queryFactory.delete(employee)
					.where(employee.id.eq(id))
				    .execute();
		logger.info("Deleted employee Id: {}", empId);
		if(empId == 0)
			return false;
		return true;
		
	}

	@Transactional
	public void createNewEmployee(String name, String email, String salary) 
	{
		Employee emp = new Employee();
		emp.setName(name);
		emp.setEmail(email);
		emp.setSalary(salary);
		entityManager.persist(emp);
		return;
	}
	
//SELECT  distinct e.id AS 'mng_id', e.name AS 'mng_name' FROM users e, users m where e.id = m.manager_id;
	public String getEmployeeWhoAreManager() 
	{
		QEmployee employee = QEmployee.employee;
		QEmployee manager = new QEmployee("manager");
		JPAQueryFactory  queryFactory = jpaQueryFactory();
		List<Tuple> result = queryFactory.select(employee.id, employee.name)
										 .from(employee, manager)
										 .where(employee.id.eq(manager.manager_id))
										 .groupBy(employee.id)
										 .fetch();
		
		StringBuilder strbr = new StringBuilder();
		for(Tuple row : result)
		{
			strbr.append(row.get(employee.id));
			strbr.append(" :: ");
			strbr.append(row.get(employee.name));
			strbr.append("\n");
			logger.info("Employees who are managers:::: {}", strbr.toString());
		}
		return strbr.toString();
	}

	public String getEmployeeWhoAreManagerWithEmployeeCount() 
	{
		QEmployee employee = QEmployee.employee;
		QEmployee manager = new QEmployee("manager"); //Important
		JPAQueryFactory  queryFactory = jpaQueryFactory();
		List<Tuple> result = queryFactory.select(employee.id, employee.name, employee.id.count())
										 .from(employee, manager)
										 .where(employee.id.eq(manager.manager_id))
										 .groupBy(employee.id)
										 .fetch();
		
		StringBuilder strbr = new StringBuilder();
		for(Tuple row : result)
		{
			strbr.append(row.get(employee.id));
			strbr.append(" :: ");
			strbr.append(row.get(employee.name));
			strbr.append(" :: ");
			strbr.append(row.get(employee.id.count()));
			strbr.append("\n");
			logger.info("Employees who are managers with their subordinates count:::: {}", strbr.toString());
		}
		return strbr.toString();
	}
	
    //select * from users u1 where u1.salary = (select max(u2.salary) from users u2);
	//select * from users u1 where 1 = (select count(salary) from users u2 where u1.salary<=u2.salary);
	//SELECT MIN(salary) from (SELECT salary from users ORDER BY salary DESC LIMIT 3) as A;
	
	/*public List<Employee> getNthMaxSalary() 
	{
		QEmployee employee1 = QEmployee.employee;
		QEmployee employee2 = new QEmployee("manager"); //Important
		List<Employee> empList;
		JPAQueryFactory  queryFactory = jpaQueryFactory();
		
		empList = queryFactory.selectFrom(employee1).where(employee1.salary.eq(JPAExpressions.select(employee2.salary.max()).from(employee2))).fetch();
		return empList;
	}*/

}
