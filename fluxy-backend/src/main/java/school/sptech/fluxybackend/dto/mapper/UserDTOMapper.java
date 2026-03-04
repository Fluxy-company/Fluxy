package school.sptech.fluxybackend.dto.mapper;

import org.springframework.stereotype.Component;
import school.sptech.fluxybackend.dto.UserRequestDTO;
import school.sptech.fluxybackend.dto.UserResponseDTO;
import school.sptech.fluxybackend.model.User;

@Component
public class UserDTOMapper {

    public UserResponseDTO toDTO(User user){
        UserResponseDTO dto = new UserResponseDTO();
        dto.setId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());

        return dto;
    }

    public User toEntity(UserRequestDTO dto){
        User user = new User();
                user.setId(dto.getId());
                user.setFirstName(dto.getFirstName());
                user.setLastName(dto.getLastName());
                user.setEmail(dto.getEmail());
                user.setPassword(dto.getPassword());
                user.setPhone(dto.getPhone());

                return user;
    }


}

