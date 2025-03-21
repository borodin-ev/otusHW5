package ru.otus;

import io.gatling.javaapi.core.ChainBuilder;
import io.gatling.javaapi.http.HttpRequestActionBuilder;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Random;

import static io.gatling.javaapi.core.CoreDsl.*;
import static io.gatling.javaapi.http.HttpDsl.http;
import static io.gatling.javaapi.http.HttpDsl.status;

public class Actions {

    public ChainBuilder auth() {
        return group("Login").on(navPL(), login());
    }

    public ChainBuilder findFlight() {
        return group("Find Flights").on(selectRandomCities(), chooseTrip());
    }

    public ChainBuilder chooseFlight() {
        return group("Choose Flight").on(selectRandomFlight(), flightReservation());
    }

    public ChainBuilder openHomePage() {
        return exec(http("Open Home Page")
                .get("/webtours")
                .resources(
                        http("/headers").get("/webtours/header.html").check(status().is(200)),
                        http("/welcome").get("/cgi-bin/welcome.pl?signOff=true").check(status().is(200)),
                        http("home.html").get("/WebTours/home.html").check(status().is(200))
                ));
    }

    public ChainBuilder navPL() {
        return exec(http("get userSession")
                .get("/cgi-bin/nav.pl?in=home")
                .check(css("input[name=userSession]", "value").saveAs("userSession"))
        );
    }

    public ChainBuilder login() {
        return exec(http("Login")
                .post("/cgi-bin/login.pl")
                .formParam("userSession", "#{userSession}")
                .formParam("username", "loadTestUser")
                .formParam("password", "passwordLoad")
                .formParam("login.x", "75")
                .formParam("login.y", "5")
                .formParam("JSFormSubmit", "false")
                .check(status().is(200))
                .resources(
                        http("login.pl/intro=true").get("/cgi-bin/login.pl?intro=true").check(status().is(200)),
                        http("Login nav.pl").get("/cgi-bin/nav.pl?page=menu&in=home").check(status().is(200))
                ));

    }
    public ChainBuilder openFlights() {
        return exec(http("Navigate to Flights")
                .get("/cgi-bin/welcome.pl?page=search")
                .resources(
                        http("Flights nav.pl").get("/cgi-bin/nav.pl?page=menu&in=flights").check(status().is(200)),
                        http("Reservations").get("/cgi-bin/reservations.pl?page=welcome").check(status().is(200))
                                .check(css("option", "value")
                                        .findAll()
                                        .saveAs("cities")))
        );
    }

    public ChainBuilder chooseTrip() {
        LocalDate from = LocalDate.now().plusDays(1);
        LocalDate to = LocalDate.now().plusDays(7);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd/yyyy");
        String fromDate = from.format(formatter);
        String toDate = to.format(formatter);

        return exec(http("Choose trip")
                 .post("/cgi-bin/reservations.pl")
                .formParam("advanceDiscount", "0")
                .formParam("depart", "#{fromPort}")
                .formParam("departDate", fromDate)
                .formParam("arrive", "#{toPort}")
                .formParam("returnDate", toDate)
                .formParam("numPassengers", "1")
                .formParam("seatPref", "None")
                .formParam("seatType", "Coach")
                .formParam("findFlights.x", "63")
                .formParam("findFlights.y", "15")
                .formParam(".cgifields", "roundtrip")
                .formParam(".cgifields", "seatType")
                .formParam(".cgifields", "seatPref")
                .check(status().is(200))
                .check(css("input[name=outboundFlight]", "value")
                        .findAll()
                        .saveAs("outboundFlights"))
        );
    }

    public ChainBuilder flightReservation() {
        return exec(http("Choose trip")
                .post("/cgi-bin/reservations.pl")
                .formParam("outboundFlight", "#{flight}")
                .formParam("numPassengers", "1")
                .formParam("advanceDiscount", "0")
                .formParam("seatType", "Coach")
                .formParam("seatPref", "None")
                .formParam("reserveFlights.x", "28")
                .formParam("reserveFlights.y", "9")
                .check(status().is(200)));
    }

    public ChainBuilder payment() {
        return exec(http("Buy Flight")
                .post("/cgi-bin/reservations.pl")
                .formParam("firstName", "Test")
                .formParam("lastName", "Load")
                .formParam("address1", "Street 1")
                .formParam("address2", "111222")
                .formParam("pass1", "Test Load")
                .formParam("creditCard", "32435")
                .formParam("expDate", "1243")
                .formParam("oldCCOption", "")
                .formParam("numPassengers", "1")
                .formParam("seatType", "Coach")
                .formParam("seatPref", "None")
                .formParam("outboundFlight", "#{flight}")
                .formParam("advanceDiscount", "0")
                .formParam("JSFormSubmit", "off")
                .formParam("buyFlights.x", "63")
                .formParam("buyFlights.y", "15")
                .formParam(".cgifields", "saveCC")
                .check(status().is(200))
                .check(bodyString().saveAs("responseBody"))
        );
    }

    private ChainBuilder selectRandomCities() {
        return exec(session -> {
            List<String> cities = session.getList("cities");

            String fromPort = cities.get(new Random().nextInt(cities.size()));

            String toPort;
            do {
                toPort = cities.get(new Random().nextInt(cities.size()));
            } while (toPort.equals(fromPort));

            return session
                    .set("fromPort", fromPort)
                    .set("toPort", toPort);
        });
    }

    private ChainBuilder selectRandomFlight() {
        return exec(session -> {
            List<String> flights = session.getList("outboundFlights");

            String flight = flights.get(new Random().nextInt(flights.size()));

            return session
                    .set("flight", flight);
        });
    }
}
