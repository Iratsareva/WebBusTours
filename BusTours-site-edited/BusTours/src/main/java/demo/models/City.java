package demo.models;


import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name = "city")
public class City extends BaseEntity{
    private String nameCity;
    private String descriptionCity;
    private Set<Tour> tours;


    public City(String nameCity, String descriptionCity) {
        this.nameCity = nameCity;
        this.descriptionCity = descriptionCity;
    }

    public City() {}

    @Column(name = "name_city", nullable = false)
    public String getNameCity() {
        return nameCity;
    }
    public void setNameCity(String nameCity) {
        this.nameCity = nameCity;
    }


    @Column(name = "description_city" , columnDefinition = "TEXT")
    public String getDescriptionCity() {
        return descriptionCity;
    }
    public void setDescriptionCity(String descriptionCity) {
        this.descriptionCity = descriptionCity;
    }


    @OneToMany(mappedBy = "destination", targetEntity = Tour.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Tour> getTours() {
        return tours;
    }

    public void setTours(Set<Tour> tours) {
        this.tours = tours;
    }


    @Override
    public String toString() {
        return "City{" +
                "nameCity='" + nameCity + '\'' +
                ", descriptionCity='" + descriptionCity + '\'' +
                '}';
    }
}
