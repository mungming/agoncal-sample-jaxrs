package org.agoncal.sample.jaxrs.jwt.rest;

import org.agoncal.sample.jaxrs.jwt.filter.JWTTokenCheckFilter;
import org.agoncal.sample.jaxrs.jwt.filter.JWTTokenNeeded;
import org.agoncal.sample.jaxrs.jwt.util.KeyGenerator;
import org.agoncal.sample.jaxrs.jwt.util.LoggerProducer;
import org.agoncal.sample.jaxrs.jwt.util.SimpleKeyGenerator;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Response;
import java.net.URI;

import static javax.ws.rs.core.MediaType.TEXT_PLAIN;
import static org.junit.Assert.assertEquals;

@RunWith(Arquillian.class)
@RunAsClient
public class SecuredEchoEndpointTest {

    // ======================================
    // =             Attributes             =
    // ======================================

    private Client client;
    private WebTarget webTarget;

    // ======================================
    // =          Injection Points          =
    // ======================================

    @ArquillianResource
    private URI baseURL;

    // ======================================
    // =         Deployment methods         =
    // ======================================

    @Deployment(testable = false)
    public static WebArchive createDeployment() {

        return ShrinkWrap.create(WebArchive.class)
                .addClasses(SecuredEchoEndpoint.class)
                .addClasses(JWTTokenCheckFilter.class, JWTTokenNeeded.class, KeyGenerator.class, SimpleKeyGenerator.class)
                .addClasses(LoggerProducer.class, SecuredEchoApplicationConfig.class)
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml");
    }

    // ======================================
    // =          Lifecycle methods         =
    // ======================================

    @Before
    public void initWebTarget() {
        client = ClientBuilder.newClient();
        webTarget = client.target(baseURL).path("api/securedecho");
    }

    // ======================================
    // =            Test methods            =
    // ======================================

    @Test
    public void shouldEcho() throws Exception {
        Response response = webTarget.request(TEXT_PLAIN).get();
        assertEquals(401, response.getStatus());
    }
}