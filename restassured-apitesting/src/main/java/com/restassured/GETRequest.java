package com.restassured;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import org.hamcrest.Matcher;
import org.junit.Test;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;

import static com.jayway.restassured.RestAssured.given;
import com.jayway.restassured.path.json.JsonPath;
import com.jayway.restassured.response.Response;
import com.jayway.restassured.response.ValidatableResponse;
import com.jayway.restassured.response.ValidatableResponseOptions;

import static org.hamcrest.core.StringContains.*;
import static org.hamcrest.xml.HasXPath.*;

public class GETRequest {

	@Test
	public void testGetSingleUser() {

		RestAssured.given().baseUri("http://localhost:8080/rest-assured-example/")
				.body(new BodyObj("test@hascode.com", "Tim", "Testerman1", "1")).when().get("/service/single-user")
				.then().statusCode(200);
	}

	@Test
	public void testGetSingleUserProgrammatic() {
		BodyObj obj = new BodyObj("test@hascode.com", "Tim", "Testerman", "1");
		Response res = RestAssured.given().baseUri("http://localhost:8080/rest-assured-example/")
				.get("/service/single-user");
		assertEquals(200, res.getStatusCode());
		String json = res.asString();
		JsonPath jp = new JsonPath(json);
		assertEquals("test@hascode.com", jp.get("email"));
		assertEquals("Tim", jp.get("firstName"));
		assertEquals("Testerman", jp.get("lastName"));
		assertEquals("1", jp.get("id"));
	}

	@Test
	public void testGetSingleUser1() {
		RestAssured.given().baseUri("http://localhost:8080/rest-assured-example/").then().statusCode(200)
				.body("email", equalTo("test@hascode.com"), "firstName", equalTo("Tim"), "lastName",
						equalTo("Testerman"), "id", equalTo("1"))
				.when().get("/service/single-user");
	}

	@Test
	public void validatetestxml() {

		Response res = given().baseUri("http://localhost:8080/rest-assured-example/").get("/service/single-user/xml");
		assertEquals(200, res.getStatusCode());
		res.then().body("user.email", equalTo("test@hascode.com"), "user.firstName", equalTo("Tim"), "user.id",
				equalTo("1"), "user.lastName", equalTo("Testerman"));
	}

	@Test
	public void validatexpath() {

		Response res = given().baseUri("http://localhost:8080/rest-assured-example/").get("/service/persons/xml");
		assertEquals(200, res.getStatusCode());
		res.then().body("people.person[0].email", equalTo("test@hascode.com"));
		ValidatableResponse a = res.then().body(hasXPath("/people/person/email",containsString("gmail.com")));
		a.log();
		res.then().body(hasXPath("/people/person/email", containsString("hascode.com")));
		res.then().body(hasXPath("/people/person/email[contains(text(),'hascode.com')]"));

	}


	@Test
	public void validate404() {

		Response res = given().baseUri("http://localhost:8080/rest-assured-example/").get("/service/status/notfound");
		assertEquals(404, res.getStatusCode());
	}
	@Test
	public void validatebigjson() {

		Response res = given().baseUri("http://localhost:8080/rest-assured-example/").get("/service/persons/json");
		
		/*
		 * Response res =given().headers("Content-Type", ContentType.JSON, "Accept",
		 * ContentType.JSON).
		 * when().get("http://localhost:8080/rest-assured-example/service/persons/json")
		 * . then().contentType(ContentType.JSON).extract().response();
		 */
		
		assertEquals(200, res.getStatusCode());
		
		String ids = res.jsonPath().getString("person.@id");
		System.out.println(ids);        
        
		String id1 = res.jsonPath().getString("person[0].@id");
		System.out.println(id1);
		
		List<String> firstNamesList = res.jsonPath().getList("person.firstName");
		System.out.println(firstNamesList);
		
	//	 Map<String, String> persons = res.jsonPath().getMap("person");
		// System.out.println(persons.get("email"));
		//res.then().body("people.person[0].email", equalTo("test@hascode.com"));
		

	}
	@Test
	public void validateparams() {
		 given().parameters("email","test@hascode.com").when().get("/service/user/create").then().body("user.email", containsString("dev@hascode.com"),is(false));
	}
}
