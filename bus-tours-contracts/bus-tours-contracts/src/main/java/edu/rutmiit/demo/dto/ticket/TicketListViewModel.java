package edu.rutmiit.demo.dto.ticket;

import edu.rutmiit.demo.dto.base.BaseViewModel;

import java.util.List;

public record TicketListViewModel(
        BaseViewModel base,
        List<TicketDetailListViewModel> tickets
) {}