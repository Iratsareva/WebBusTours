package demo.services;

import demo.dto.CityDTO;
import org.springframework.data.domain.Page;

import java.util.List;

public interface CityService {
    CityDTO createCity(String nameCity, String description);

    void updateCity( String id,String nameCity,String description);

    CityDTO getCityById(String id);
    List<CityDTO> findAll();

    Page<CityDTO> getCities(int page, int size);

}
