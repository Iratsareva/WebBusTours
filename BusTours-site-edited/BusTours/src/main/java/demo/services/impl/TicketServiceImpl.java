package demo.services.impl;

import demo.dto.TicketDTO;
import demo.exception.PasNotFoundException;
import demo.models.*;
import demo.repos.PassengerRepository;
import demo.repos.TicketRepository;
import demo.repos.TripRepository;
import demo.services.TicketService;
import edu.rutmiit.demo.dto.ticket.TicketStatus;
import demo.repos.BusRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class  TicketServiceImpl implements TicketService {

    @Autowired
    private TicketRepository ticketRepository;
    @Autowired
    private BusRepository busRepository;
    @Autowired
    private TripRepository tripRepository;
    @Autowired
    private PassengerRepository passengerRepository;


    private ModelMapper modelMapper = new ModelMapper();


    @Override
    public TicketDTO getTicketById(String id){
        Ticket t = ticketRepository.findById(Ticket.class, id);
        return new TicketDTO(t.getId(), t.getPassenger().getId(), t.getTrip().getId(), t.getPrice(), t.getTicketStatus());

    }

    @Override
    @Transactional
    public List<TicketDTO> findAll() {
        return ticketRepository.getAll(Ticket.class).stream().map(t ->
                new TicketDTO(t.getId(), t.getPassenger().getId(), t.getTrip().getId(), t.getPrice(), t.getTicketStatus())
        ).toList();
    }



    @Override
    public Page<TicketDTO> getTicketsUser(int page, int size, String id) {
        List<Ticket> alllistQuery = ticketRepository.getAll(Ticket.class);

        Passenger passenger = passengerRepository.findById(Passenger.class, id);

        List<Ticket> listQuery = new ArrayList<>();
        for(int i=0 ;i< alllistQuery.size(); i++){
            if( alllistQuery.get(i).getPassenger().equals(passenger)){
                listQuery.add(alllistQuery.get(i));
            }
        }


        Collections.sort(listQuery, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket t1, Ticket t2) {
                return t2.getTicketStatus().compareTo(t1.getTicketStatus());
            }
        });

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("mark"));

        int totalElements = listQuery.size();
        int start = (int) Math.min(pageable.getOffset(), totalElements);
        int end = Math.min(start + pageable.getPageSize(), totalElements);
        List<Ticket> ticketOnPage = listQuery.subList(start, end);

        Page<Ticket> page1 = new PageImpl<>(ticketOnPage, pageable, totalElements);

        return page1.map(t ->
                new TicketDTO(t.getId(), t.getPassenger().getId(), t.getTrip().getId(), t.getPrice(), t.getTicketStatus()));

    }

    @Override
    public TicketDTO createTicket(String passengerId, String tripId) {
        List<Ticket> ticketList = ticketRepository.PassengerTickets(passengerId);
        for (Ticket value : ticketList) {
            if (value.getTrip().getId().equals(tripId)) {
                return null;
            }
        }






        Ticket ticket = ticketRepository.create(new Ticket(
                passengerRepository.findById(Passenger.class,passengerId ),
                tripRepository.findById(Trip.class,tripId),
                tripRepository.findById(Trip.class,tripId).getTour().getPrice(),
                TicketStatus.AWAITING));

        return new TicketDTO(ticket.getId(), ticket.getPassenger().getId(), ticket.getTrip().getId(), ticket.getPrice(), ticket.getTicketStatus());
    }

    @Override
    public void deleteTicket(String id) {
        Ticket ticket = ticketRepository.findById(Ticket.class, id);
        ticket.setTicketStatus(TicketStatus.CANCELLED);
        ticketRepository.create(ticket);
    }

    @Override
    public void confirmTicket(String id) {
        Ticket ticket = ticketRepository.findById(Ticket.class, id);
        ticket.setTicketStatus(TicketStatus.CONFIRMED);
        ticketRepository.create(ticket);
    }

    @Override
    public Page<TicketDTO> getTickets(int page, int size) {
        List<Ticket> listQuery = ticketRepository.getAll(Ticket.class);
        Collections.sort(listQuery, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket t1, Ticket t2) {
                return t2.getTicketStatus().compareTo(t1.getTicketStatus());
            }
        });

        Pageable pageable = PageRequest.of(page - 1, size, Sort.by("ticketStatus"));

        int totalElements = listQuery.size();
        int start = (int) Math.min(pageable.getOffset(), totalElements);
        int end = Math.min(start + pageable.getPageSize(), totalElements);
        List<Ticket> ticketOnPage = listQuery.subList(start, end);

        Page<Ticket> page1 = new PageImpl<>(ticketOnPage, pageable, totalElements);

        return page1.map(t ->
                new TicketDTO(t.getId(), t.getPassenger().getId(), t.getTrip().getId(), t.getPrice(), t.getTicketStatus()));

    }


}
