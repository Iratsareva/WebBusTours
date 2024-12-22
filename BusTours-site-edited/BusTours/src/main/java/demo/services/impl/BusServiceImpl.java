package demo.services.impl;

import demo.dto.BusDto;
import edu.rutmiit.demo.dto.bus.BusStatus;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import demo.models.Bus;
import demo.repos.BusRepository;
import demo.services.BusService;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@EnableCaching
public class BusServiceImpl implements BusService {

    @PersistenceContext
    private EntityManager entityManager;
    private final BusRepository busRepo;

    public BusServiceImpl(BusRepository busRepo) {
        this.busRepo = busRepo;
    }

    @Override
    public BusDto createBus(String mark, String numberBus, Integer numberSeats, String classBus, LocalDate year) {
        Bus bus = busRepo.create(new Bus(mark,  numberBus,  numberSeats,  classBus,  year, BusStatus.WORK));
        return new BusDto(bus.getId(), bus.getMark(), bus.getNumberBus(), bus.getNumberSeats(), bus.getClassBus(), bus.getYear(), bus.getBusStatus());
    }

    @Override
    public void updateBus(String id,String mark, String numberBus, Integer numberSeats, String classBus, LocalDate year) {
        var bus = busRepo.findById(Bus.class, id);
        bus.setMark(mark);
        bus.setNumberBus(numberBus);
        bus.setNumberSeats(numberSeats);
        bus.setClassBus(classBus);
        bus.setYear(year);
        busRepo.create(bus);
    }

    @Override
    public BusDto getBus(String id) {
        var bus = busRepo.findById(Bus.class, id);
        return new BusDto(bus.getId(), bus.getMark(), bus.getNumberBus(), bus.getNumberSeats(), bus.getClassBus(), bus.getYear(), bus.getBusStatus());
    }

    @Override
    public void deleteBus(String id) {
        Bus bus = busRepo.findById(Bus.class, id);
        bus.setBusStatus(BusStatus.DELETED);
        busRepo.create(bus);
    }



    @Override
    @Cacheable(value =  "buses")
    public Page<BusDto> getBus( int page, int size) {

        List<Bus> listQuery1 = busRepo.getAll(Bus.class);
        List<Bus> listQuery = new ArrayList<>();
        for (Bus value : listQuery1) {
            if (value.getBusStatus() != BusStatus.DELETED) {
                listQuery.add(value);
            }
        }


        Pageable pageable = PageRequest.of(page - 1, size);

        int totalElements = listQuery.size();
        int start = (int) Math.min(pageable.getOffset(), totalElements);
        int end = Math.min(start + pageable.getPageSize(), totalElements);
        List<Bus> busOnPage = listQuery.subList(start, end);



        Page<Bus> page1 = new PageImpl<>(busOnPage, pageable, totalElements);


        return page1.map(bus -> new BusDto(bus.getId(), bus.getMark(), bus.getNumberBus(), bus.getNumberSeats(), bus.getClassBus(), bus.getYear(), bus.getBusStatus()));
    }

    @Override
    @Transactional

    public List<BusDto> findAll() {
        List<Bus> buses = busRepo.getAll(Bus.class).stream()
                .filter(bus -> bus.getBusStatus().equals(BusStatus.WORK)).toList();

        return buses.stream().map(b ->
                new BusDto(b.getId(), b.getMark(), b.getNumberBus(), b.getNumberSeats(), b.getClassBus(), b.getYear(), b.getBusStatus())).toList();
    }
}

