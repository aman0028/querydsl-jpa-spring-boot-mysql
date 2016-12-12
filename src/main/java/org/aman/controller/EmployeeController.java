package org.aman.controller;

import java.util.List;

import org.aman.entity.Employee;
import org.aman.repository.Repository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/")
public class EmployeeController {
	
	@Autowired
	private Repository empDAO;
	
	
	@RequestMapping(value ="/print", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String printIt() {
		
		return "Testing Server";
	}
	
	@RequestMapping(value ="/id/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getEemployeeNameById(@PathVariable(value="id") String id) {
		Employee emp;
		try{
			 emp = empDAO.getEmpNameById(Integer.valueOf(id));
		}
		catch(Exception e){
			return "user not found";
		}
		return ""+  emp;
	}
	
	@RequestMapping(value ="/name/{name}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getEemployeeNameByName(@PathVariable(value="name") String name) {
		Employee emp;
		try{
			 emp = empDAO.getEmpNameByName(name);
		}
		catch(Exception e){
			return "user not found";
		}
		return ""+  emp;
	}
	
	
	@RequestMapping(value ="/emp-list/orderby/id", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getEmployeeList() {
		List<Employee> employeList;
		try{
			employeList = empDAO.getEmployeeList(false);
		}
		catch(Exception e){
			return "user not found";
		}
		return "employee List: \n"+ employeList;
	}
	
	@RequestMapping(value ="/emp-list/orderby/name", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getEmployeeListWithOrder() {
		List<Employee> employeList;
		try{
			employeList = empDAO.getEmployeeList(true);
		}
		catch(Exception e){
			return "user not found";
		}
		return "employee List: \n"+ employeList;
	}
	
	@RequestMapping(value ="/groupby/dept", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getEmployeeCountByDept() {
		String strList;
		try{
			strList = empDAO.getEmployeeCountByDept();
		}
		catch(Exception e){
			return "user not found";
		}
		return "departmentwise employee Count: \n"+ strList;
	}
	
	
	@RequestMapping(value ="/deptid/{deptId}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getEmployeeByDeptId(@PathVariable(value="deptId") String deptId) {
		String strList;
		try{
			strList = empDAO.getEmpBydeptId(deptId);
		}
		catch(Exception e){
			return "user not found";
		}
		return "Employees belongs to dept Id:" + deptId + "\n"+ strList;
	}
	
	@RequestMapping(value ="/update/{id}/{salary}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getEmployeeByDeptId(
			@PathVariable(value="id") String id,
			@PathVariable(value="salary") String salary) {
		
		Employee emp = empDAO.getEmpNameById(Integer.valueOf(id));
		try{
			empDAO.updateSalaryForEmpId(Integer.valueOf(id), salary);
		}
		catch(Exception e){
			e.printStackTrace();
			return "user not found. Can't update";
		}
		return "Salary updated from " + emp.getSalary() + "/- to " + salary + "/- for empId: " + id;
	}
	
	@RequestMapping(value ="/delete/{id}", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String deleteEmployeeById(
			@PathVariable(value="id") String id) {
		Boolean isDeleted;
		try{
			isDeleted = empDAO.deleteEmployeeByEmpId(Integer.valueOf(id));
		}
		catch(Exception e){
			e.printStackTrace();
			return "user not found. Can't update";
		}
		if(isDeleted)
		  return "Employee Id: " + id + " deleted";
		else
			return "Employee Id: " + id + " not present. It may be deleted already";
	}
	
	@RequestMapping(value ="/create", method = RequestMethod.POST, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String createNewEmployee(
			@RequestParam(value="name", required= true) String name,
			@RequestParam(value="email", required= true) String email,
			@RequestParam(value="salary", required= false) String salary){
		Integer empId;
		try{
			empDAO.createNewEmployee(name, email, salary);
		}
		catch(Exception e){
			e.printStackTrace();
			return "user not created. Can't update";
		}
		
		return "Employee Id created";
	}
	
	//Find the employees who are managers
	@RequestMapping(value ="/empwhoaremanager", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getEmployeeWhoAreManager(){
		String strList;
		try{
			 strList = empDAO.getEmployeeWhoAreManager();
		}
		catch(Exception e){
			e.printStackTrace();
			return "Something went wrong";
		}
		
		return "Employee List who are managers:\n"+ strList;
	}
	//Find the managers with the count of subordinates
	@RequestMapping(value ="/empwhoaremanagerwithempcount", method = RequestMethod.GET, produces = { MediaType.APPLICATION_JSON_VALUE })
	public @ResponseBody String getEmployeeWhoAreManagerWithEmployeeCount(){
		String strList;
		try{
			 strList = empDAO.getEmployeeWhoAreManagerWithEmployeeCount();
		}
		catch(Exception e){
			e.printStackTrace();
			return "Something went wrong";
		}
		
		return "Employee List who are managers with subordinates count:\n"+ strList;
	}
	
	
	
}
