package demo.services.impl;

import demo.models.City;
import demo.services.CityService;
import demo.dto.CityDTO;
import demo.repos.CityRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@EnableCaching
public class CityServiceImpl implements CityService {

    @Autowired
    private CityRepository cityRepository;
    private ModelMapper modelMapper = new ModelMapper();



    @Override
    public CityDTO createCity(String nameCity, String description) {
        City city =  cityRepository.create(new City(nameCity, description));
        return new CityDTO(city.getId(), city.getNameCity(), city.getDescriptionCity());
    }

    @Override
    public void updateCity(String id, String nameCity, String description) {
        var city = cityRepository.findById(City.class, id);

        city.setNameCity(nameCity);
        city.setDescriptionCity(description);

        cityRepository.create(city);
    }


    @Override
    public CityDTO getCityById(String id) {
        City c = cityRepository.findById(City.class, id);
        return new CityDTO(c.getId(), c.getNameCity(), c.getDescriptionCity());
    }

    @Override
    @Transactional
    public List<CityDTO> findAll() {
        return cityRepository.getAll(City.class).stream().map(c ->
                new CityDTO(c.getId(), c.getNameCity(), c.getDescriptionCity())).toList();
    }

    @Override
    @Cacheable(value = "cities")
    public Page<CityDTO> getCities(int page, int size) {
        List<City> listQuery = cityRepository.getAll(City.class);
        Pageable pageable = PageRequest.of(page - 1, size);

        int totalElements = listQuery.size();
        int start = (int) Math.min(pageable.getOffset(), totalElements);
        int end = Math.min(start + pageable.getPageSize(), totalElements);
        List<City> cityOnPage = listQuery.subList(start, end);

        Page<City> page1 = new PageImpl<>(cityOnPage, pageable, totalElements);

        return page1.map(book -> new CityDTO(book.getId(), book.getNameCity(), book.getDescriptionCity()));

    }

}
