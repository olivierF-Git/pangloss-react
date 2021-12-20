package org.owasp.pangloss.infra.configurations;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.APPLICATION_XHTML_XML;
import static org.springframework.http.MediaType.TEXT_HTML;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
@ActiveProfiles("poc")
public class WebSecurityConfigTests {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void should_not_be_redirected_to_builtin_login_page_when_not_authenticated_from_the_browser() {
        ResponseEntity<String> response = restTemplate.exchange("/api/profile", GET, request(), String.class);
        assertThat(response.getStatusCode())
                .isNotEqualTo(OK)
                .isEqualTo(UNAUTHORIZED);
    }

    @Test
    public void should_not_be_redirected_on_logout() {
        ResponseEntity<String> response = restTemplate.postForEntity("/api/logout", null, String.class);

        assertThat(response.getStatusCode())
                .isNotEqualTo(FOUND)
                .isEqualTo(OK);
    }

    private HttpEntity<String> request() {
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(asList(TEXT_HTML, APPLICATION_XHTML_XML));
        return new HttpEntity<>("", headers);
    }
}