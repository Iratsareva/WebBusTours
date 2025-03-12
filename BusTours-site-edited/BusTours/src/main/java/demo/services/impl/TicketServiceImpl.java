package demo.services.impl;

import demo.dto.ticket.TicketCreateDTO;
import demo.dto.ticket.TicketDTO;
import demo.exception.NotFoundException;
import demo.models.*;
import demo.repositories.PassengerRepository;
import demo.repositories.TicketRepository;
import demo.repositories.TripRepository;
import demo.services.TicketService;
import edu.rutmiit.demo.dto.ticket.TicketStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class  TicketServiceImpl implements TicketService {
    private TicketRepository ticketRepository;
    private TripRepository tripRepository;
    private PassengerRepository passengerRepository;


    @Override
    public TicketDTO getTicketById(String id) throws NotFoundException {
        Ticket ticket = ticketRepository.findById(id);
        if (ticket == null) {
            throw new NotFoundException("Билет с ID " + id + " не найден");
        }
        return new TicketDTO(ticket.getId(), ticket.getPassenger().getId(), ticket.getTrip().getId(), ticket.getPrice(), ticket.getTicketStatus());
    }

    @Override
    public List<TicketDTO> findAll() throws NotFoundException {
        List<Ticket> tickets = ticketRepository.findAll();
        if(tickets == null || tickets.isEmpty()){
            throw new NotFoundException("Билеты не найдены");
        }
        return tickets.stream().map(t ->
                new TicketDTO(t.getId(), t.getPassenger().getId(), t.getTrip().getId(), t.getPrice(), t.getTicketStatus())).toList();
    }

    @Override
    public List<TicketDTO> getTicketsUser(String id) throws NotFoundException {
        List<Ticket> tickets = ticketRepository.findAll();
        if(tickets == null || tickets.isEmpty()){
            throw new NotFoundException("Билеты не найдены");
        }

        Passenger passenger = passengerRepository.findById(id);
        if (passenger == null) {
            throw new NotFoundException("Пассажир с ID " + id + " не найден");
        }

        List<Ticket> listQuery = new ArrayList<>();
        for(int i = 0; i< tickets.size(); i++){
            if( tickets.get(i).getPassenger().equals(passenger)){
                listQuery.add(tickets.get(i));
            }
        }
        Collections.sort(listQuery, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket t1, Ticket t2) {
                return t2.getTicketStatus().compareTo(t1.getTicketStatus());
            }
        });
        return listQuery.stream().map(t ->
                new TicketDTO(t.getId(), t.getPassenger().getId(), t.getTrip().getId(), t.getPrice(), t.getTicketStatus())).toList();
    }

    @Override
    public TicketDTO createTicket(TicketCreateDTO ticketCreateDTO) throws NotFoundException {
        Passenger passenger =  passengerRepository.findById(ticketCreateDTO.passengerId());
        if (passenger == null) {
            throw new NotFoundException("Пассажир с ID " + ticketCreateDTO.passengerId() + " не найден");
        }
        Trip trip = tripRepository.findById(ticketCreateDTO.tripId());
        if (trip == null) {
            throw new NotFoundException("Рейс с ID " + ticketCreateDTO.tripId() + " не найден");
        }

        List<Ticket> ticketList = ticketRepository.PassengerTickets(ticketCreateDTO.passengerId());
        for (Ticket value : ticketList) {
            if (value.getTrip().getId().equals(ticketCreateDTO.tripId())) {
                return null;
            }
        }

        Ticket ticket = ticketRepository.create(new Ticket(passenger, trip,
                trip.getTour().getPrice(),
                TicketStatus.AWAITING));

        return new TicketDTO(ticket.getId(), ticket.getPassenger().getId(), ticket.getTrip().getId(), ticket.getPrice(), ticket.getTicketStatus());
    }

    @Override
    public void deleteTicket(String id)throws NotFoundException {
        Ticket ticket = ticketRepository.findById(id);
        if (ticket == null) {
            throw new NotFoundException("Билет с ID " + id + " не найден");
        }
        ticket.setTicketStatus(TicketStatus.CANCELLED);
        ticketRepository.create(ticket);
    }

    @Override
    public void confirmTicket(String id)throws NotFoundException {
        Ticket ticket = ticketRepository.findById(id);
        if (ticket == null) {
            throw new NotFoundException("Билет с ID " + id + " не найден");
        }
        ticket.setTicketStatus(TicketStatus.CONFIRMED);
        ticketRepository.create(ticket);
    }

    @Override
    public List<TicketDTO> getTickets() throws NotFoundException {
        List<Ticket> tickets = ticketRepository.findAll();
        if(tickets == null || tickets.isEmpty()){
            throw new NotFoundException("Билеты не найдены");
        }

        Collections.sort(tickets, new Comparator<Ticket>() {
            @Override
            public int compare(Ticket t1, Ticket t2) {
                return t2.getTicketStatus().compareTo(t1.getTicketStatus());
            }
        });

        return tickets.stream().map(t ->
                new TicketDTO(t.getId(), t.getPassenger().getId(), t.getTrip().getId(), t.getPrice(), t.getTicketStatus())).toList();

    }


    @Autowired
    public void setTicketRepository(TicketRepository ticketRepository) {
        this.ticketRepository = ticketRepository;
    }
    @Autowired
    public void setTripRepository(TripRepository tripRepository) {
        this.tripRepository = tripRepository;
    }
    @Autowired
    public void setPassengerRepository(PassengerRepository passengerRepository) {
        this.passengerRepository = passengerRepository;
    }
}
