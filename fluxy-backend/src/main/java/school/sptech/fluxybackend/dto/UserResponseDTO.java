package school.sptech.fluxybackend.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor

public class UserResponseDTO {

        private Long id;
        private String name;
        private String email;
        private String phone;
        private String cnpj;
        private String address;
        private String city;
        private String state;

}
