package ru.otus;

import io.gatling.javaapi.core.*;

import java.time.Duration;

import static io.gatling.javaapi.core.CoreDsl.*;
import static ru.otus.WebTours.httpProtocol;

public class MaxPerfomanceTest extends Simulation{
    {
        setUp(
                new WebToursScenario().webToursScn.injectOpen(
                        incrementUsersPerSec(0.6)
                                .times(10)
                                .eachLevelLasting(120)
                                .startingFrom(0.6)
                )
        ).protocols(httpProtocol)
                .maxDuration(Duration.ofMinutes(30));
    }
}
