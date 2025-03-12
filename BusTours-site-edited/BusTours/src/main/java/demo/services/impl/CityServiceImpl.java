package demo.services.impl;

import demo.dto.city.CityCreateDTO;
import demo.dto.city.CityUpdateDTO;
import demo.exception.NotFoundException;
import demo.models.City;
import demo.services.CityService;
import demo.dto.city.CityDTO;
import demo.repositories.CityRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableCaching
public class CityServiceImpl implements CityService {
    private CityRepository cityRepository;

    @Override
    public CityDTO createCity(CityCreateDTO cityCreateDTO) {
        City city =  cityRepository.create(new City(cityCreateDTO.nameCity(), cityCreateDTO.description()));
        return new CityDTO(city.getId(), city.getNameCity(), city.getDescriptionCity());
    }

    @Override
    public void updateCity(CityUpdateDTO cityUpdateDTO)throws NotFoundException {
        var city = cityRepository.findById(cityUpdateDTO.id());
        if (city == null) {
            throw new NotFoundException("Город с ID " + cityUpdateDTO.id() + " не найден");
        }
        city.setNameCity(cityUpdateDTO.nameCity());
        city.setDescriptionCity(cityUpdateDTO.description());
        cityRepository.create(city);
    }

    @Override
    public CityDTO getCityById(String id)throws NotFoundException {
        City city = cityRepository.findById(id);
        if (city == null) {
            throw new NotFoundException("Город с ID " + id + " не найден");
        }
        return new CityDTO(city.getId(), city.getNameCity(), city.getDescriptionCity());
    }

    @Override
    public List<CityDTO> findAll()throws NotFoundException {
        List<City> cities = cityRepository.findAll();
        if(cities == null || cities.isEmpty()){
            throw new NotFoundException("Города не найдены");
        }
        return cities.stream().map(city ->
                new CityDTO(city.getId(), city.getNameCity(), city.getDescriptionCity())).toList();
    }

    @Override
    @Cacheable(value = "cities")
    public List<CityDTO> getCities()throws NotFoundException {
        List<City> cities = cityRepository.findAll();
        if(cities == null || cities.isEmpty()){
            throw new NotFoundException("Города не найдены");
        }
        return cities.stream().map(city -> new CityDTO(city.getId(), city.getNameCity(), city.getDescriptionCity())).toList();
    }

    @Autowired
    public void setCityRepository(CityRepository cityRepository) {
        this.cityRepository = cityRepository;
    }
}
