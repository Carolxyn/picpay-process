package br.com.picpay.picpaychallange.Dto;

import br.com.picpay.picpaychallange.domain.user.UserType;

import java.math.BigDecimal;

public record UserDTO (String firstname, String lastname, String document, BigDecimal balance, String email, String password, UserType userType){
}

