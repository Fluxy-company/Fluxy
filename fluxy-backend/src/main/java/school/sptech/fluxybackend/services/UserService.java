package school.sptech.fluxybackend.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import school.sptech.fluxybackend.dto.UserRequestDTO;
import school.sptech.fluxybackend.dto.UserResponseDTO;
import school.sptech.fluxybackend.dto.mapper.UserDTOMapper;
import school.sptech.fluxybackend.exception.handler.ResourceNotFoundException;
import school.sptech.fluxybackend.model.User;
import school.sptech.fluxybackend.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository repository;

    @Autowired
    private UserDTOMapper mapper;

    public List<UserResponseDTO> findAll(){
       List<User> users = repository.findAll();
       List<UserResponseDTO> dtos = new ArrayList<>();

       for(User user : users){
           dtos.add(mapper.toDTO(user));
       }
                return dtos;
    }

    public UserResponseDTO findById(Long id){
       User user = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sem registros nesse id"));
    return mapper.toDTO(user);
    }

    public UserResponseDTO create(UserRequestDTO dto){
        User user = mapper.toEntity(dto);
        user.setCreatedAt(System.currentTimeMillis());
        User saved = repository.save(user);
        return mapper.toDTO(saved);
    }

    public UserResponseDTO update(Long id, UserRequestDTO dto){
       User entity = repository.findById(id)
           .orElseThrow(() -> new ResourceNotFoundException("Não existe"));
       entity.setName(dto.getName());
       entity.setEmail(dto.getEmail());
       entity.setPhone(dto.getPhone());
       entity.setAddress(dto.getAddress());
       entity.setCity(dto.getCity());
        entity.setState(dto.getState());

       User saved = repository.save(entity);
       return mapper.toDTO(saved);
    }

    public void delete(Long id){
        User entity = repository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Sem registros nesse id"));
        repository.delete(entity);
    }
}
