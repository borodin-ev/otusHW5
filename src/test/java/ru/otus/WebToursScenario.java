package ru.otus;

import io.gatling.javaapi.core.*;

import static io.gatling.javaapi.core.CoreDsl.*;

public class WebToursScenario {

    Actions actions = new Actions();


    public final ScenarioBuilder webToursScn = scenario("Tickets buying")
            .exec(actions.openHomePage())
            .exec(actions.auth())
            .exec(actions.openFlights())
            .exec(actions.findFlight())
            .exec(actions.chooseFlight())
            .exec(actions.payment());
}
