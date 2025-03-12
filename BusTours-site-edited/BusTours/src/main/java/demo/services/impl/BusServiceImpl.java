package demo.services.impl;

import demo.dto.bus.BusCreateDto;
import demo.dto.bus.BusDto;
import demo.dto.bus.BusUpdateDto;
import demo.exception.NotFoundException;
import edu.rutmiit.demo.dto.bus.BusStatus;
import demo.models.Bus;
import demo.repositories.BusRepository;
import demo.services.BusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@EnableCaching
public class BusServiceImpl implements BusService {
    private BusRepository busRepo;

    @Override
    public BusDto createBus( BusCreateDto busCreateDto) {
        Bus bus = busRepo.create(new Bus(busCreateDto.mark(),  busCreateDto.numberBus(),  busCreateDto.numberSeats(),  busCreateDto.classBus(),  busCreateDto.year(), BusStatus.WORK));
        return new BusDto(bus.getId(), bus.getMark(), bus.getNumberBus(), bus.getNumberSeats(), bus.getClassBus(), bus.getYear(), bus.getBusStatus());
    }

    @Override
    public void updateBus(BusUpdateDto busUpdateDto) throws NotFoundException {
        var bus = busRepo.findById(busUpdateDto.id());
        if (bus == null) {
            throw new NotFoundException("Тур с ID " + busUpdateDto.id() + " не найден");
        }
        bus.setMark(busUpdateDto.mark());
        bus.setNumberBus(busUpdateDto.numberBus());
        bus.setNumberSeats(busUpdateDto.numberSeats());
        bus.setClassBus(busUpdateDto.classBus());
        bus.setYear(busUpdateDto.year());
        busRepo.create(bus);
    }

    @Override
    public BusDto getBus(String id)throws NotFoundException {
        var bus = busRepo.findById(id);
        if (bus == null) {
            throw new NotFoundException("Тур с ID " + id + " не найден");
        }
        return new BusDto(bus.getId(), bus.getMark(), bus.getNumberBus(), bus.getNumberSeats(), bus.getClassBus(), bus.getYear(), bus.getBusStatus());
    }

    @Override
    public void deleteBus(String id) throws NotFoundException {
        Bus bus = busRepo.findById(id);
        if (bus == null) {
            throw new NotFoundException("Тур с ID " + id + " не найден");
        }
        bus.setBusStatus(BusStatus.DELETED);
        busRepo.create(bus);
    }


    @Override
    @Cacheable(value =  "buses")
    public List<BusDto> getBus() throws NotFoundException {
        List<Bus> buses = busRepo.findAll();
        if(buses == null || buses.isEmpty()){
            throw new NotFoundException("Автобусы не найдены");
        }
        List<Bus> busesNotDeleted = buses.stream().filter(bus -> bus.getBusStatus() != BusStatus.DELETED).toList();
        if(busesNotDeleted.isEmpty()){
            throw new NotFoundException("Не удаленные автобусы не найдены");
        }
        return busesNotDeleted.stream().map(bus -> new BusDto(bus.getId(), bus.getMark(), bus.getNumberBus(), bus.getNumberSeats(), bus.getClassBus(), bus.getYear(), bus.getBusStatus())).toList();
    }

    @Override
    public List<BusDto> findAll() throws NotFoundException {
        List<Bus> buses = busRepo.findAll();
        if(buses == null || buses.isEmpty()){
            throw new NotFoundException("Автобусы не найдены");
        }
        List<Bus> workBuses = buses.stream().filter(bus -> bus.getBusStatus().equals(BusStatus.WORK)).toList();
        if(workBuses.isEmpty()){
            throw new NotFoundException("Работающие автобусы не найдены");
        }
        return workBuses.stream().map(bus -> new BusDto(bus.getId(), bus.getMark(), bus.getNumberBus(), bus.getNumberSeats(), bus.getClassBus(), bus.getYear(), bus.getBusStatus())).toList();
    }

    @Autowired
    public void setBusRepo(BusRepository busRepo) {
        this.busRepo = busRepo;
    }
}

