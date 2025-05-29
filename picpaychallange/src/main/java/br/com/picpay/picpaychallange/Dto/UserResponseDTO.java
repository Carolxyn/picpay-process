package br.com.picpay.picpaychallange.Dto;

import br.com.picpay.picpaychallange.domain.user.User;
import br.com.picpay.picpaychallange.domain.user.UserType;
import java.math.BigDecimal;

public record UserResponseDTO(
        Long id,
        String firstname,
        String lastname,
        String document,    // null por segurança
        String email,
        String password,    // null por segurança
        BigDecimal balance,
        UserType userType   // null por segurança
) {
    // Construtor que cria UserResponseDTO a partir de User, definindo campos sensíveis como null
    public static UserResponseDTO fromUser(User user) {
        return new UserResponseDTO(
                user.getId(),
                user.getFirstname(),
                user.getLastname(),
                null,              // document como null
                user.getEmail(),   // email mantido
                null,              // password como null
                user.getBalance(),
                null               // userType como null
        );
    }
}