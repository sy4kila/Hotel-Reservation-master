package henry.hotel.services;

import java.util.Collection;

import jakarta.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import henry.hotel.model.Reservation;
import henry.hotel.repository.ReservationRep;
import henry.hotel.dto.ReservationDto;

@Service
public class ReservationServiceImpl implements ReservationService {
	
	// service pattern to manage transactionals  
	//	and handle services for reservation between server and client
	
	private UserService userService;

	private ReservationRep reservationRepository;
	
	@Autowired
	public ReservationServiceImpl(UserService userService, ReservationRep reservationRepository) {
		this.userService = userService;
		this.reservationRepository = reservationRepository;
	}
	
	// get reservation for logged user
	@Override
	@Transactional
	public Reservation getReservationForLoggedUserById(int resId) {
		
		return reservationRepository.findById(resId);
	}

	// get all reservations for logger user 
	@Override
	@Transactional
	public Collection<Reservation> getReservationsForLoggedUser() {
		return reservationRepository.findAllByUserId((userService.getLoggedUserId()));
	}

	// transfer data between temp reservation and Reservation class after check it to save it 
	@Override
	@Transactional
	public void saveOrUpdateReservation(ReservationDto reservationDto) {
		Reservation reservation = new Reservation();

		// get required id user using user services
		reservation.setUserId(userService.getLoggedUserId());

		reservation.setArrivalDate(reservationDto.getArrivalDate());
		reservation.setOpenBuffet(reservationDto.getOpenBuffet());
		reservation.setStayDays(reservationDto.getStayPeriod());
		reservation.setChildren(reservationDto.getChildren());
		reservation.setPersons(reservationDto.getPersons());
		reservation.setPrice(reservationDto.getPrice());
		reservation.setRooms(reservationDto.getRooms());
		reservation.setRoom(reservationDto.getRoom());
		reservation.setId(reservationDto.getId());

		reservationRepository.save(reservation);
	}
	
	// transfer data between Reservation and temp Reservation class to update request  
	@Override
	public ReservationDto reservationToCurrentReservationById(int resId) {
		Reservation reservation = getReservationForLoggedUserById(resId);
		ReservationDto reservationDto = new ReservationDto();
		
		reservationDto.setArrivalDate(reservation.getArrivalDate());
		reservationDto.setOpenBuffet(reservation.getOpenBuffet());
		reservationDto.setStayPeriod(reservation.getStayDays());
		reservationDto.setChildren(reservation.getChildren());
		reservationDto.setPersons(reservation.getPersons());
		reservationDto.setUsertId(reservation.getUserId());
		reservationDto.setRooms(reservation.getRooms());
		reservationDto.setPrice(reservation.getPrice());
		reservationDto.setRoom(reservation.getRoom());
		reservationDto.setId(reservation.getId());
		
		return reservationDto;
	}
	
	// delete reservation 
	@Override
	@Transactional
	public void deleteReservation(int resId) {
		
		reservationRepository.deleteById(resId);
	}
}
