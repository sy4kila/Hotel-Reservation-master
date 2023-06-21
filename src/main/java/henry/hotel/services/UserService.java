package henry.hotel.services;

import henry.hotel.model.User;
import henry.hotel.dto.UserDto;

import org.springframework.security.core.userdetails.UserDetailsService;

//Service Pattern for User
public interface UserService extends UserDetailsService {

	public User findUserByEmail(String email);

	public void saveUser(UserDto userDto);

	public int getLoggedUserId();
}
