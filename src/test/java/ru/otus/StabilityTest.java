package ru.otus;

import io.gatling.javaapi.core.Simulation;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static ru.otus.WebTours.httpProtocol;

public class StabilityTest extends Simulation {
    {
        double intensity = 4.8;

        setUp(
                new WebToursScenario().webToursScn.injectOpen(
                        rampUsersPerSec(0).to(intensity).during(30),
                        constantUsersPerSec(intensity).during(60 * 60)
                )
        ).protocols(httpProtocol);
    }
}