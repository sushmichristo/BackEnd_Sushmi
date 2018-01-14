package com.workplace.employee.controller;

import static org.junit.Assert.*;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.workplace.employee.controller.EmployeeController;
import com.workplace.employee.model.Employee;
import com.workplace.employee.service.EmployeeService;

@RunWith(SpringRunner.class)
@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

	private static final String contentType = "application/json;charset=UTF-8";
	
	@Autowired
	private WebApplicationContext webApplicationContext;

	@MockBean
	private EmployeeService employeeService;

	@Autowired
	private MockMvc mockMvc;

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext)
				.build();
	}

	private List<Employee> createEmployee() {
		return Arrays.asList(new Employee(1, "sushmi", "SE", 30000.0));
	}

	/**
	 * To test the getAllEmployees method without json path.
	 * 
	 * @throws Exception
	 */
	@Test
	public void getAllEmployeesTest() throws Exception {

		when(this.employeeService.getAllEmployees()).thenReturn(
				createEmployee());

		RequestBuilder requestBuilder = MockMvcRequestBuilders.get(
				"/getAllEmployees").accept(MediaType.APPLICATION_JSON);

		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();
		final String expected = "[{id:1,name:sushmi,designation:SE,salary:30000.0}]";
		JSONAssert.assertEquals(expected, result.getResponse()
				.getContentAsString(), false);

		verify(this.employeeService).getAllEmployees();
		verify(this.employeeService, times(1)).getAllEmployees();
		verifyNoMoreInteractions(this.employeeService);
	}

	/**
	 * To test the addEmployee method with out json path
	 * 
	 * @throws Exception
	 */
	@Test
	public void addEmployee() throws Exception {
		final Employee employee = this.createEmployee().get(0);

		doNothing().when(this.employeeService).saveEmployee(employee);

		RequestBuilder requestBuilder = MockMvcRequestBuilders
				.post("/addEmployee").accept(MediaType.APPLICATION_JSON)
				.content(employee.toString())
				.contentType(MediaType.APPLICATION_JSON);

		MvcResult result = this.mockMvc.perform(requestBuilder).andReturn();

		MockHttpServletResponse response = result.getResponse();
	}

	/**
	 * To test the getAllEmployees method with json path.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetAllEmployees() throws Exception {
		when(this.employeeService.getAllEmployees()).thenReturn(
				createEmployee());
		this.mockMvc.perform(get("/getAllEmployees"))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.[0].id", Matchers.is(1)))
				.andExpect(jsonPath("$.[0].designation", Matchers.is("SE")))
				.andExpect(jsonPath("$.[0].name", Matchers.is("sushmi")))
				.andExpect(jsonPath("$.[0].salary", Matchers.is(30000.0)));
		verify(this.employeeService).getAllEmployees();
		verify(this.employeeService, times(1)).getAllEmployees();
		verifyNoMoreInteractions(this.employeeService);
	}

	/**
	 * To test the getEmployee by Id.
	 * 
	 * @throws Exception
	 */
	@Test
	public void testgetEmployeeById() throws Exception {
		when(employeeService.getEmployee(1)).thenReturn(
				this.createEmployee().get(0));
		mockMvc.perform(
				get("/getEmployee/{id}", this.createEmployee().get(0).getId()))
				.andExpect(status().isOk())
				.andExpect(content().contentType(contentType))
				.andExpect(jsonPath("$.id", Matchers.is(1)))
				.andExpect(jsonPath("$.designation", Matchers.is("SE")))
				.andExpect(jsonPath("$.name", Matchers.is("sushmi")))
				.andExpect(jsonPath("$.salary", Matchers.is(30000.0)));
		verify(this.employeeService).getEmployee(1);
		verify(this.employeeService, times(1)).getEmployee(1);
		verifyNoMoreInteractions(this.employeeService);
	}

	
	/*
	 * converts a Java object into JSON representation
	 */
	public static String asJsonString(final Object obj) {
		try {
			return new ObjectMapper().writeValueAsString(obj);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
