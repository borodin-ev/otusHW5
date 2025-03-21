package ru.otus;

import io.gatling.javaapi.http.HttpProtocolBuilder;

import static io.gatling.javaapi.http.HttpDsl.http;

public class WebTours {

    public static final HttpProtocolBuilder httpProtocol = http
            .baseUrl("http://webtours.load-test.ru:1080/")
            .acceptHeader("text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8")
            .contentTypeHeader("application/x-www-form-urlencoded");

}
