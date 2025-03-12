package demo.models;


import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "tour")
public class Tour extends BaseEntity {
    private String nameTour;
    private String description;
    private String itinerary;
    private int lengthTour;
    private int price;
    private City destination;

    private Set<Trip> trips;


    public Tour(String nameTour, String description, String itinerary, int lengthTour, int price, City destination) {
        this.nameTour = nameTour;
        this.description = description;
        this.itinerary = itinerary;
        this.lengthTour = lengthTour;
        this.price = price;
        this.destination = destination;
    }

    protected Tour(){}

    @Column(name = "name_tour", nullable = false)
    public String getNameTour() {
        return nameTour;
    }
    public void setNameTour(String nameTour) {
        this.nameTour = nameTour;
    }

    @Column(name = "description", columnDefinition = "TEXT")
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }




    @Column(name = "itinerary", nullable = false)
    public String getItinerary() {
        return itinerary;
    }
    public void setItinerary(String itinerary) {
        this.itinerary = itinerary;
    }



    @Column(name = "length_tour",nullable = false)
    public int getLengthTour() {
        return lengthTour;
    }

    public void setLengthTour(int lengthTour) {
        this.lengthTour = lengthTour;
    }


    @Column(name = "price", nullable = false)
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }



    @OneToMany(mappedBy = "tour", targetEntity = Trip.class,
    fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }



    @ManyToOne
    @JoinColumn(name = "destination", referencedColumnName = "id")
    public City getDestination() {
        return destination;
    }
    public void setDestination(City destination) {
        this.destination = destination;
    }


    @Override
    public String toString() {
        return "Tour{" +
                "nameTour='" + nameTour + '\'' +
                ", description='" + description + '\'' +
                ", itinerary='" + itinerary + '\'' +
                ", lengthTour=" + lengthTour +
                ", price=" + price +
                ", destination=" + destination +
                ", trips=" + trips +
                '}';
    }
}

