package henry.hotel.services;

import java.util.Collection;

import henry.hotel.model.Reservation;
import henry.hotel.dto.ReservationDto;

//Service Pattern for Reservation
public interface ReservationService {
	
	public Reservation getReservationForLoggedUserById(int resId);

	public Collection<Reservation> getReservationsForLoggedUser();
	
	public void saveOrUpdateReservation(ReservationDto reservationDto);
	
	public void deleteReservation(int resId);

	public ReservationDto reservationToCurrentReservationById(int resId);

}
