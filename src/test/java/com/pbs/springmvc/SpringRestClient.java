package com.pbs.springmvc;

import java.net.URI;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.codec.binary.Base64;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.client.RestTemplate;

import com.pbs.springmvc.model.AuthTokenInfo;
import com.pbs.springmvc.model.Person;

public class SpringRestClient {

	public static final String REST_SERVICE_URI = "http://localhost:8080/SpringSecurityOAuth2Example";

	public static final String AUTH_SERVER_URI = "http://localhost:8080/SpringSecurityOAuth2Example/oauth/token";

	public static final String QPM_PASSWORD_GRANT = "?grant_type=password&username=bill&password=abc123";

	public static final String QPM_ACCESS_TOKEN = "?access_token=";

	/*
	 * Prepare HTTP Headers.
	 */
	private static HttpHeaders getHeaders() {
		HttpHeaders headers = new HttpHeaders();
		headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
		return headers;
	}

	/*
	 * Add HTTP Authorization header, using Basic-Authentication to send client-credentials.
	 */
	private static HttpHeaders getHeadersWithClientCredentials() {
		String plainClientCredentials = "my-trusted-client:secret";
		String base64ClientCredentials = new String(Base64.encodeBase64(plainClientCredentials.getBytes()));

		HttpHeaders headers = getHeaders();
		headers.add("Authorization", "Basic " + base64ClientCredentials);
		return headers;
	}

	/*
	 * Send a POST request [on /oauth/token] to get an access-token, which will then be send with each request.
	 */
	@SuppressWarnings({ "unchecked" })
	private static AuthTokenInfo sendTokenRequest() {
		RestTemplate restTemplate = new RestTemplate();

		HttpEntity<String> request = new HttpEntity<String>(getHeadersWithClientCredentials());
		ResponseEntity<Object> response = restTemplate.exchange(AUTH_SERVER_URI + QPM_PASSWORD_GRANT, HttpMethod.POST, request, Object.class);
		LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) response.getBody();
		
		AuthTokenInfo tokenInfo = null;
		if (map != null) {
			tokenInfo = new AuthTokenInfo();
			tokenInfo.setAccess_token((String) map.get("access_token"));
			tokenInfo.setToken_type((String) map.get("token_type"));
			tokenInfo.setRefresh_token((String) map.get("refresh_token"));
			tokenInfo.setExpires_in((int) map.get("expires_in"));
			tokenInfo.setScope((String) map.get("scope"));
			System.out.println(tokenInfo);
		} else {
			System.out.println("No user exist----------");

		}
		
		return tokenInfo;
	}

	/*
	 * Send a GET request to get list of all users.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static void listAllPersons(AuthTokenInfo tokenInfo) {
		Assert.notNull(tokenInfo, "Authenticate first please......");

		System.out.println("\nTesting listAllPersons API-----------");
		RestTemplate restTemplate = new RestTemplate();

		HttpEntity<String> request = new HttpEntity<String>(getHeaders());
		ResponseEntity<List> response = restTemplate.exchange(REST_SERVICE_URI + "/user/" + QPM_ACCESS_TOKEN + tokenInfo.getAccess_token(), HttpMethod.GET, request, List.class);
		List<LinkedHashMap<String, Object>> usersMap = (List<LinkedHashMap<String, Object>>) response.getBody();

		if (usersMap != null) {
			for (LinkedHashMap<String, Object> map : usersMap) {
				System.out.println("Person : id=" + map.get("id") + ", Name=" + map.get("name") + ", Age=" + map.get("age") + ", Salary=" + map.get("salary"));
			}
		} else {
			System.out.println("No user exist----------");
		}
	}

	/*
	 * Send a GET request to get a specific user.
	 */
	private static void getPerson(AuthTokenInfo tokenInfo) {
		Assert.notNull(tokenInfo, "Authenticate first please......");
		
		System.out.println("\nTesting getPerson API----------");
		RestTemplate restTemplate = new RestTemplate();
		
		HttpEntity<String> request = new HttpEntity<String>(getHeaders());
		ResponseEntity<Person> response = restTemplate.exchange(REST_SERVICE_URI + "/user/1" + QPM_ACCESS_TOKEN + tokenInfo.getAccess_token(), HttpMethod.GET, request, Person.class);
		Person user = response.getBody();
		
		System.out.println(user);
	}

	/*
	 * Send a POST request to create a new user.
	 */
	private static void createPerson(AuthTokenInfo tokenInfo) {
		Assert.notNull(tokenInfo, "Authenticate first please......");
		
		System.out.println("\nTesting create Person API----------");
		RestTemplate restTemplate = new RestTemplate();
		
		Person person = new Person(Long.valueOf(0), "Sarah", Long.valueOf(51), Long.valueOf(134));
		HttpEntity<Object> request = new HttpEntity<Object>(person, getHeaders());
		URI uri = restTemplate.postForLocation(REST_SERVICE_URI + "/user/" + QPM_ACCESS_TOKEN + tokenInfo.getAccess_token(), request, Person.class);
		
		System.out.println("Location : " + uri.toASCIIString());
	}

	/*
	 * Send a PUT request to update an existing user.
	 */
	private static void updatePerson(AuthTokenInfo tokenInfo) {
		Assert.notNull(tokenInfo, "Authenticate first please......");
	
		System.out.println("\nTesting update Person API----------");
		RestTemplate restTemplate = new RestTemplate();
		
		Person person = new Person(Long.valueOf(1), "D", Long.valueOf(33), Long.valueOf(70000));
		HttpEntity<Object> request = new HttpEntity<Object>(person, getHeaders());
		ResponseEntity<Person> response = restTemplate.exchange(REST_SERVICE_URI + "/user/1" + QPM_ACCESS_TOKEN + tokenInfo.getAccess_token(), HttpMethod.PUT, request, Person.class);
		
		System.out.println(response.getBody());
	}

	/*
	 * Send a DELETE request to delete a specific user.
	 */
	private static void deletePerson(AuthTokenInfo tokenInfo) {
		Assert.notNull(tokenInfo, "Authenticate first please......");
		
		System.out.println("\nTesting delete Person API----------");
		HttpEntity<String> request = new HttpEntity<String>(getHeaders());
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(REST_SERVICE_URI + "/user/3" + QPM_ACCESS_TOKEN + tokenInfo.getAccess_token(), HttpMethod.DELETE, request, Person.class);
	}

	/*
	 * Send a DELETE request to delete all users.
	 */
	private static void deleteAllPersons(AuthTokenInfo tokenInfo) {
		Assert.notNull(tokenInfo, "Authenticate first please......");
		
		System.out.println("\nTesting all delete Persons API----------");
		HttpEntity<String> request = new HttpEntity<String>(getHeaders());
		
		RestTemplate restTemplate = new RestTemplate();
		restTemplate.exchange(REST_SERVICE_URI + "/user/" + QPM_ACCESS_TOKEN + tokenInfo.getAccess_token(),	HttpMethod.DELETE, request, Person.class);
	}

	public static void main(String args[]) {
		AuthTokenInfo tokenInfo = sendTokenRequest();
		listAllPersons(tokenInfo);

		getPerson(tokenInfo);

		createPerson(tokenInfo);
		listAllPersons(tokenInfo);

		updatePerson(tokenInfo);
		listAllPersons(tokenInfo);

		deletePerson(tokenInfo);
		listAllPersons(tokenInfo);

		deleteAllPersons(tokenInfo);
		listAllPersons(tokenInfo);
	}
}