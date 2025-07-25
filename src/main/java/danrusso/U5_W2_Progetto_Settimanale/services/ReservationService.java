package danrusso.U5_W2_Progetto_Settimanale.services;

import danrusso.U5_W2_Progetto_Settimanale.entities.Employee;
import danrusso.U5_W2_Progetto_Settimanale.entities.Journey;
import danrusso.U5_W2_Progetto_Settimanale.entities.Reservation;
import danrusso.U5_W2_Progetto_Settimanale.enums.JourneyType;
import danrusso.U5_W2_Progetto_Settimanale.exceptions.BadRequestException;
import danrusso.U5_W2_Progetto_Settimanale.exceptions.NotFoundException;
import danrusso.U5_W2_Progetto_Settimanale.exceptions.ToManyReservationsException;
import danrusso.U5_W2_Progetto_Settimanale.payloads.NewReservationDTO;
import danrusso.U5_W2_Progetto_Settimanale.repositories.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {
    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private EmployeeService employeeService;

    @Autowired
    private JourneyService journeyService;

    public Page<Reservation> findAll(int pageNum, int pageSize, String sortBy) {
        if (pageSize > 10) pageSize = 10;
        Pageable pageable = PageRequest.of(pageNum, pageSize, Sort.by(sortBy));
        return this.reservationRepository.findAll(pageable);
    }

    public Reservation saveReservation(NewReservationDTO payload) {
        Employee foundEmp = this.employeeService.findById(payload.employeeId());
        Journey foundJou = this.journeyService.findById(payload.journeyId());
        if (foundJou.getStatus().equals(JourneyType.COMPLETED))
            throw new BadRequestException("You cannot book a completed trip.");
        List<Reservation> foundList = this.reservationRepository.filterByEmployee(foundEmp.getEmployeeId(), foundJou.getDate());
        if (!foundList.isEmpty()) throw new ToManyReservationsException("You already have a booking for that date.");

        Reservation newReser = new Reservation(foundJou, foundEmp, LocalDate.now(), payload.notes());

        Reservation savedReser = this.reservationRepository.save(newReser);
        System.out.println("Tutto bene");
        return savedReser;
    }

    public Reservation findById(UUID id) {
        return this.reservationRepository.findById(id).orElseThrow(() -> new NotFoundException(id, "Reservation"));
    }

//    public Reservation findByIdAndUpdate(UUID id, NewReservationDTO payload) {
//        Reservation found = this.findById(id);
//
//        if (payload.date().isBefore(LocalDate.now())) status = JourneyType.COMPLETED;
//
//        found.setDate(payload.date());
//        found.setDestination(payload.destination());
//        found.setStatus(status);
//
//        Journey updatedJourney = this.journeyRepository.save(found);
//        System.out.println("Journey with id " + found.getJourneyId() + " updated successfully.");
//        return updatedJourney;
//
//    }

    public void findByIdAndDelete(UUID id) {
        this.reservationRepository.delete(this.findById(id));
    }

}
