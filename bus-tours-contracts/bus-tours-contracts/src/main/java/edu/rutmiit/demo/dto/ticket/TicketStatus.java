package edu.rutmiit.demo.dto.ticket;

public enum TicketStatus {
    CONFIRMED,
    CANCELLED,
    AWAITING;




    public String getValue() {
        if(this == TicketStatus.AWAITING ){
            return "Ожидает подтверждения";
        } else if (this == TicketStatus.CANCELLED) {
            return "Отмененный";
        }else if (this == TicketStatus.CONFIRMED){
            return "Подтвержденный";
        }
        return null;
    }
}
