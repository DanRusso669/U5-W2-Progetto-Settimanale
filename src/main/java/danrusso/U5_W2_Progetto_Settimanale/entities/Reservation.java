package danrusso.U5_W2_Progetto_Settimanale.entities;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.UUID;

@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue
    @Column(name = "reservation_id")
    private UUID reservationId;
    @ManyToOne
    @JoinColumn(name = "journey_id")
    private Journey journey;
    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;
    private LocalDate date;
    private String notes;
}
