package demo.models;


import edu.rutmiit.demo.dto.driver.DriverStatus;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "driver")
public class Driver extends BaseEntity{
    private String name;
    private String surname;
    private String patronymic;
    private LocalDate birthday;
    private String passport;
    private String driverLicense;
    private LocalDate dateStartDriverLicense;
    private String category;
    private DriverStatus driverStatus;


    private Set<Trip> trips;

    public Driver(String name, String surname, String patronymic, LocalDate birthday, String passport, String driverLicense, LocalDate dateStartDriverLicense, String category, DriverStatus driverStatus) {
        this.name = name;
        this.surname = surname;
        this.patronymic = patronymic;
        this.birthday = birthday;
        this.passport = passport;
        this.driverLicense = driverLicense;
        this.dateStartDriverLicense = dateStartDriverLicense;
        this.category = category;
        this.driverStatus = driverStatus;
    }

    protected Driver(){}

    @Column(name = "name", nullable = false)
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "surname", nullable = false)
    public String getSurname() {
        return surname;
    }
    public void setSurname(String surname) {
        this.surname = surname;
    }


    @Column(name = "patronymic")
    public String getPatronymic() {
        return patronymic;
    }
    public void setPatronymic(String patronymic) {
        this.patronymic = patronymic;
    }


    @Column(name = "birthday", nullable = false)
    public LocalDate getBirthday() {
        return birthday;
    }
    public void setBirthday(LocalDate birthday) {
        this.birthday = birthday;
    }

    @Column(name = "passport", length = 10, nullable = false)
    public String getPassport() {
        return passport;
    }
    public void setPassport(String passport) {
        this.passport = passport;
    }


    @Column(name = "driver_license",length = 10, nullable = false)
    public String getDriverLicense() {
        return driverLicense;
    }
    public void setDriverLicense(String driverLicense) {
        this.driverLicense = driverLicense;
    }


    @Column(name = "date_start_driver_license", nullable = false)
    public LocalDate getDateStartDriverLicense() {
        return dateStartDriverLicense;
    }
    public void setDateStartDriverLicense(LocalDate dateStartDriverLicense) {
        this.dateStartDriverLicense = dateStartDriverLicense;
    }

    @Column(name = "category", nullable = false)
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }


    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    public DriverStatus getDriverStatus() {
        return driverStatus;
    }
    public void setDriverStatus(DriverStatus driverStatus) {
        this.driverStatus = driverStatus;
    }


    @OneToMany(mappedBy = "driver", targetEntity = Trip.class,
            fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    public Set<Trip> getTrips() {
        return trips;
    }

    public void setTrips(Set<Trip> trips) {
        this.trips = trips;
    }

    @Override
    public String toString() {
        return "Driver{" +
                "name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", patronymic='" + patronymic + '\'' +
                ", birthday=" + birthday +
                ", passport='" + passport + '\'' +
                ", driverLicense='" + driverLicense + '\'' +
                ", dateStartDriverLicense=" + dateStartDriverLicense +
                ", category='" + category + '\'' +
                ", driverStatus=" + driverStatus +
                ", trips=" + trips +
                '}';
    }
}
