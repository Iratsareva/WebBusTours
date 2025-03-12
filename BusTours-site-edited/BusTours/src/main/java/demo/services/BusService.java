package demo.services;

import demo.dto.bus.BusCreateDto;
import demo.dto.bus.BusDto;
import demo.dto.bus.BusUpdateDto;
import demo.exception.NotFoundException;

import java.util.List;

public interface BusService  {
    BusDto createBus(BusCreateDto busCreateDto);
    void updateBus(BusUpdateDto busUpdateDto)throws NotFoundException;
    BusDto getBus(String id) throws NotFoundException;
    void deleteBus(String id) throws NotFoundException;
    List<BusDto> getBus() throws NotFoundException;
    List<BusDto> findAll() throws NotFoundException;
}
