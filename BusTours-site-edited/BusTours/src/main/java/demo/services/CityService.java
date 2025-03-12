package demo.services;

import demo.dto.city.CityCreateDTO;
import demo.dto.city.CityDTO;
import demo.dto.city.CityUpdateDTO;
import demo.exception.NotFoundException;

import java.util.List;

public interface CityService {
    CityDTO createCity(CityCreateDTO cityCreateDTO);
    void updateCity(CityUpdateDTO cityUpdateDTO) throws NotFoundException;
    CityDTO getCityById(String id) throws NotFoundException;
    List<CityDTO> findAll() throws NotFoundException;
    List<CityDTO> getCities() throws NotFoundException;
}
