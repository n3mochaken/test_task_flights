package org.gridnine.testing.filter;

import org.gridnine.testing.entity.Flight;
import org.gridnine.testing.entity.Segment;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class FlightFilter {

    private List<Flight> flights;

    public FlightFilter(List<Flight> flights) {
        this.flights = new ArrayList<>(flights);
    }

    public List<Flight> build() {
        return flights;
    }

    public FlightFilter arrivalBeforeDeparturePresent() {
        flights.removeIf(flight -> flight.getSegments().stream().anyMatch
                (segment -> segment.getDepartureDate().isBefore(LocalDateTime.now())));
        return this;
    }

    public FlightFilter filterArrivalBeforeDeparture() {
        flights.removeIf(flight ->
                flight.getSegments().stream()
                        .anyMatch(segment -> segment.getArrivalDate().isBefore(segment.getDepartureDate())));
        return this;
    }

    public FlightFilter timeMoreTwoHours() {
        flights.removeIf(flight -> {
            List<Segment> segments = flight.getSegments();
            LocalDateTime curDeparture, lastArrival;
            Duration duration = Duration.ZERO;

            for (int i = 1; i < segments.size(); i++) {
                curDeparture = segments.get(i).getDepartureDate();
                lastArrival = segments.get(i - 1).getArrivalDate();
                duration = duration.plus(Duration.between(curDeparture, lastArrival).abs());
            }
            return duration.toHours() >= 2;
        });
        return this;
    }
}
