package com.hotel.test;

import io.restassured.RestAssured;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import javax.annotation.PostConstruct;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public abstract class BaseIT {

    private static final String SERVICE_URL = "http://localhost";

    @LocalServerPort private int port;

    @PostConstruct
    public void init() {
        RestAssured.port = port;
        RestAssured.baseURI = SERVICE_URL;
    }
}
