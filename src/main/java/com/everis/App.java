package com.everis;

import com.atlassian.oai.validator.*;
import com.atlassian.oai.validator.report.*;
import com.atlassian.oai.validator.model.*;

/**
 * Testing com.atlassian.oai.validator
 *
 */
public class App 
{
    public static void main( String[] args )
    {
		OpenApiInteractionValidator validator = OpenApiInteractionValidator
			.createForSpecificationUrl("http://petstore.swagger.io/v2/swagger.json")
			.build();
		ValidationReport report = validator.validateRequest(SimpleRequest.Builder.get("/v2/store/inventory").withHeader("api_key","12345").build());
		displayErrors(report);

		report = validator.validate(SimpleRequest.Builder.get("/v2/store/inventory").withHeader("api_key","12345").build(),SimpleResponse.Builder.status(200).withBody("{'hola':'amigo'}").build());
		displayErrors(report);
		
		report = validator.validate(SimpleRequest.Builder.get("/v2/pet/123").withHeader("api_key","12345").withContentType("application/json").build(),SimpleResponse.Builder.status(200).withContentType("application/json").withBody("{\"id\":123, \"category\": {\"id\":1, \"name\":\"dog\"}, \"name\": \"123\", \"photoUrls\":[\"string\"], \"tags\":[{\"id\": 0,\"name\": \"string\"}],\"status\": \"available\"}").build());
		displayErrors(report);
		
		validator = OpenApiInteractionValidator.createForSpecificationUrl("polizas.yaml")
			.build();
		report = validator.validate(SimpleRequest.Builder.get("/poliza/1").build(),
			SimpleResponse.Builder.status(200).withContentType("application/json").
			withBody("{\"id\": 0, \"nombre_cliente\": \"Elvira\",  \"apellido_cliente\": \"Gomez\", \"datos_variables\": { \"ubicacion_schema\": \"http://api.insurance.com/schemas/1\", \"data\": {\"fecha_matriculacion\": 20200207, \"capital_asegurado\": 15000 } }}").build());
		displayErrors(report);
		report = validator.validate(SimpleRequest.Builder.get("/poliza/1").build(),
			SimpleResponse.Builder.status(200).withContentType("application/json").
			withBody("{\"id\": 0, \"nombre_cliente\": \"Elvira\",  \"apellido_cliente\": \"Gomez\", \"datos_variables\": { \"ubicacion_schema\": \"http://api.insurance.com/schemas/1\", \"data\": {\"seguro_vida\": \"muerte\", \"capital_asegurado\": 320000, \"tipo\" : \"revisable\" } }}").build());
		displayErrors(report);

	}
	
	public static void displayErrors (ValidationReport report) {
		if (report.hasErrors()) {
			System.out.println("Request or response contains errors:");
			report.getMessages().forEach((m) -> {
				System.out.println(m);
			}
			);
		}
		else {
			System.out.println("Request and response are ok");
		}
	}
}
