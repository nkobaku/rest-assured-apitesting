package com.restassured;

import static io.restassured.RestAssured.given;
/*import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;*/

import org.junit.Test;

    public class HelloWorldRestAssured {

	 @Test
	 public void makeSureThatGoogleIsUp() {
		 given().when().get("http://www.google.com").then().statusCode(200);
		 
	 }

    }