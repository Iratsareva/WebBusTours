package demo.models;

import edu.rutmiit.demo.dto.bus.BusStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "bus")
public class Bus extends BaseEntity {
    private String mark;
    private String numberBus;
    private Integer numberSeats;
    private String classBus;
    private LocalDate year;
    private BusStatus busStatus;
    private Set<Trip> trips;

    public Bus(String mark, String numberBus, Integer numberSeats, String classBus, LocalDate year, BusStatus busStatus) {
        this.mark = mark;
        this.numberBus = numberBus;
        this.numberSeats = numberSeats;
        this.classBus = classBus;
        this.year = year;
         this.busStatus = busStatus;
    }

    protected Bus(){}


    @Column(name = "mark", nullable = false)
    public String getMark() {
        return mark;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }


    @Column(name = "number_bus", nullable = false)
    public String getNumberBus() {
        return numberBus;
    }
    public void setNumberBus(String numberBus) {
        this.numberBus = numberBus;
    }


    @Column(name = "number_seats", nullable = false)
    public Integer getNumberSeats() {
        return numberSeats;
    }
    public void setNumberSeats(Integer numberSeats) {
        this.numberSeats = numberSeats;
    }


    @Column(name = "class_bus")
    public String getClassBus() {
        return classBus;
    }
    public void setClassBus(String classBus) {
        this.classBus = classBus;
    }


    @Column(name = "year",nullable = false)
    public LocalDate getYear() {
        return year;
    }
    public void setYear(LocalDate year) {
        this.year = year;
    }


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public BusStatus getBusStatus() {
        return busStatus;
    }
    public void setBusStatus(BusStatus busStatus) {
        this.busStatus = busStatus;
    }




    @OneToMany(mappedBy = "bus", targetEntity = Trip.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }


    @Override
    public String toString() {
        return "Bus{" +
                "mark='" + mark + '\'' +
                ", numberBus='" + numberBus + '\'' +
                ", numberSeats=" + numberSeats +
                ", classBus='" + classBus + '\'' +
                ", year=" + year +
                ", busStatus=" + busStatus +
                ", trips=" + trips +
                '}';
    }
}
