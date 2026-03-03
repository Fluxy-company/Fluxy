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
        dto.setName(user.getName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setCnpj(user.getCnpj());
        dto.setAddress(user.getAddress());
        dto.setCity(user.getCity());
        dto.setState(user.getState());

        return dto;
    }

    public User toEntity(UserRequestDTO dto){
        User user = new User();
                user.setId(dto.getId());
                user.setName(dto.getName());
                user.setEmail(dto.getEmail());
                user.setPassword(dto.getPassword());
                user.setPhone(dto.getPhone());
                user.setCnpj(dto.getCnpj());
                user.setAddress(dto.getAddress());
                user.setCity(dto.getCity());
                user.setState(dto.getState());

                return user;
    }


}

