package edu.rutmiit.demo.dto.trip;

import edu.rutmiit.demo.dto.ticket.TicketStatus;

public enum TripStatus {
    COMPLETED,
    AWAITING,
    ON_WAY;



    public String getValue() {
        if(this == TripStatus.AWAITING ){
            return "В ожидании";
        } else if (this == TripStatus.COMPLETED) {
            return "Завершенный";
        }else if (this == TripStatus.ON_WAY){
            return "В пути";
        }
        return null;
    }
}