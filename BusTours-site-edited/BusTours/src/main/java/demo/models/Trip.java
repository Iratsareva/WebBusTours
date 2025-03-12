package demo.models;

import edu.rutmiit.demo.dto.trip.TripStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "trip")
public class Trip extends BaseEntity {
    private Tour tour;
    private Bus bus;
    private Driver driver;
    private LocalDate startDate;
    private LocalDate endDate;

    private TripStatus tripStatus;

    private Set<Ticket> tickets;

    public Trip(Tour tour, Bus bus, Driver driver, LocalDate startDate, LocalDate endDate, TripStatus tripStatus) {
        this.tour = tour;
        this.bus = bus;
        this.driver = driver;
        this.startDate = startDate;
        this.endDate = endDate;
        this.tripStatus = tripStatus;
    }

    protected Trip(){}

    @ManyToOne
    @JoinColumn(name = "id_tour", referencedColumnName = "id")
    public Tour getTour() {
        return tour;
    }
    public void setTour(Tour tour) {
        this.tour = tour;
    }

    @ManyToOne
    @JoinColumn(name = "id_bus", referencedColumnName = "id")
    public Bus getBus() {
        return bus;
    }
    public void setBus(Bus bus) {
        this.bus = bus;
    }

    @ManyToOne
    @JoinColumn(name = "id_driver", referencedColumnName = "id")
    public Driver getDriver() {
        return driver;
    }
    public void setDriver(Driver driver) {
        this.driver = driver;
    }

    @Column(name = "start_date", nullable = false)
    public LocalDate getStartDate() {
        return startDate;
    }
    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    @Column(name = "end_date", nullable = false)
    public LocalDate getEndDate() {
        return endDate;
    }
    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public TripStatus getTripStatus() {
        return tripStatus;
    }
    public void setTripStatus(TripStatus tripStatus) {
        this.tripStatus = tripStatus;
    }




    @OneToMany(mappedBy = "trip", targetEntity = Ticket.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Ticket> getTickets() {
        return tickets;
    }
    public void setTickets(Set<Ticket> tickets) {
        this.tickets = tickets;
    }


    @Override
    public String toString() {
        return "Trip{" +
                "tour=" + tour +
                ", bus=" + bus +
                ", driver=" + driver +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", tripStatus=" + tripStatus +
                ", tickets=" + tickets +
                '}';
    }
}
