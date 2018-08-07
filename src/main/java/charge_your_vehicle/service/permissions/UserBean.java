package charge_your_vehicle.service.permissions;

import charge_your_vehicle.dao.UsersDao;
import charge_your_vehicle.dto.UserDto;
import charge_your_vehicle.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserBean {

    UsersDao usersDao;

    public UserBean(UsersDao usersDao) {
        this.usersDao = usersDao;
    }

    public User getUser(UserDto userDto) {
        User user = usersDao.findById(userDto.getId());
        if (user != null)
            return user;
        else {
            User newUser = User.createFromUserDto(userDto);
            if (usersDao.findAll().size() == 0) {
                newUser.setRoleAdministration(true);
            }
            usersDao.save(newUser);
            return newUser;
        }
    }
}
