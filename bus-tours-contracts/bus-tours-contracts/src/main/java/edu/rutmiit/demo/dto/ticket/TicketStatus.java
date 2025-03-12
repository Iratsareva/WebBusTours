package edu.rutmiit.demo.dto.ticket;

public enum TicketStatus {
    CONFIRMED("Подтвержденный"),
    CANCELLED("Отмененный"),
    AWAITING ("Ожидает подтверждения");


    private String value;
    TicketStatus(String value) {}

    public String getValue(){
        return value;
    }
}
