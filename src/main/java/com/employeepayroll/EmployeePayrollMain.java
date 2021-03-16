package com.employeepayroll;

import java.util.*;

public class EmployeePayrollMain {
	
	public enum IOCommand
		{CONSOLE_IO,FILE_IO,DB_IO,REST_IO}
	
	//Declaring global var list of employee data
	public List<EmployeePayrollData> employeeDataList;

	/**Setter for list containing Emp Data
	 * @param List containing Emp Data
	 */
	public void setEmployeeDataList(List<EmployeePayrollData> employeeDataList) {
		this.employeeDataList = employeeDataList;
	}

	/**Constructor For Main Class
	 * 
	 */
	public EmployeePayrollMain() {
		employeeDataList = new ArrayList<EmployeePayrollData>();
	}

	/**Read Emp Data from console <br>
	 * Adds data to Employee Data List
	 */
	public void readEmployeeData() {
		Scanner consoleScanner=new Scanner(System.in);
		System.out.print("Enter Employee ID : ");
		int id = consoleScanner.nextInt();
		System.out.print("Enter Employee name : ");
		String name = consoleScanner.next();
		System.out.print("Enter Employee salary : ");
		double salary = consoleScanner.nextDouble();
		EmployeePayrollData employee=new EmployeePayrollData(id,name,salary);
		System.out.println(employee);
		employeeDataList.add(employee);
		consoleScanner.close();
	}
	
	/**Write Emp Data to console and file
	 * @param ioType <br> CONSOLE_IO or FILE_IO
	 */
	public void writeEmployeeData(IOCommand ioType) {
		if(ioType.equals(ioType.CONSOLE_IO)) {
			System.out.println("Writing Employee Payroll Data to Console.");
			for (EmployeePayrollData employee:employeeDataList) {
				employee.printData();
			}
		}else if (ioType.equals(ioType.FILE_IO)){
			new EmployeePayrollIO().writeData(employeeDataList);
			System.out.println("Write in File");
		}
	}
	
	/**Method to print data to console
	 * 
	 */
	public void printData() {
		new EmployeePayrollIO().printData();
	}
	
	/**Method to count entries in file
	 * @param ioType
	 * @return NoOfEntries
	 */
	public int countEntries(IOCommand ioType) {
		if(ioType.equals(IOCommand.FILE_IO)) return new EmployeePayrollIO().countEntries();
		return 0;
	}
	
	/**Method to read data from file
	 * @return List containing Emp Data
	 */
	public List<EmployeePayrollData> readData() {
		return new EmployeePayrollIO().readData();
	}
	
	//Main Method
	public static void main(String[] args) {
		EmployeePayrollMain employeeFunction = new EmployeePayrollMain();
		employeeFunction.readEmployeeData();
		employeeFunction.writeEmployeeData(IOCommand.CONSOLE_IO);
		employeeFunction.writeEmployeeData(IOCommand.FILE_IO);
		employeeFunction.printData();
		for (EmployeePayrollData employee:employeeFunction.readData()) {
			employee.printData();
		}
	}
}