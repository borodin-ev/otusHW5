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

    // Ступенчатый тест

//
//    // Тест надежности (раскомментируйте для второго задания)
//
//  {
//    setUp(
//      scn.injectOpen(
//        constantUsersPerSec(80) // 80% от найденного максимума
//          .during(3600)
//      )
//    ).protocols(httpProtocol);
//  }
//
//    {
//        setUp(scn.injectOpen(atOnceUsers(1)))
//                .protocols(httpProtocol);
//    }

}
