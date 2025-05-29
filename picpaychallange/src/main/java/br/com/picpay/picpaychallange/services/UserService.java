package br.com.picpay.picpaychallange.services;

import br.com.picpay.picpaychallange.Dto.UserDTO;
import br.com.picpay.picpaychallange.domain.user.User;
import br.com.picpay.picpaychallange.domain.user.UserType;
import br.com.picpay.picpaychallange.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserRepository repository;

    public void validateTransaction(User sender, BigDecimal amount, User receiver) throws Exception {
        // Validação de tipo de usuário SENDER
        if (sender.getUserType() == UserType.MERCHANT) {
            throw new Exception("Comerciantes não podem enviar dinheiro");
        }

        // NOVA VALIDAÇÃO: Verificar se não é transferência para MERCHANT
        if (sender.getUserType() == UserType.COMMON && receiver.getUserType() == UserType.MERCHANT) {
            throw new Exception("Usuários comuns não podem transferir dinheiro para comerciantes");
        }

        // Validação de auto-transferência
        if (sender.getId().equals(receiver.getId())) {
            throw new Exception("Não é possível transferir dinheiro para si mesmo");
        }

        // Validações básicas
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new Exception("Valor da transação deve ser maior que zero");
        }

        if (sender.getBalance() == null) {
            throw new Exception("Saldo do usuário não está definido");
        }

        // Verificação de saldo suficiente
        if (sender.getBalance().compareTo(amount) < 0) {
            throw new Exception(String.format(
                    "Saldo insuficiente. Saldo atual: R$ %s, Valor solicitado: R$ %s",
                    sender.getBalance().toString(),
                    amount.toString()
            ));
        }

        // Log para debug (remover em produção)
        System.out.println("Validação OK - Saldo: " + sender.getBalance() +
                ", Valor: " + amount);
    }

    public User findUserById(Long id) throws Exception {
        return this.repository.findUserById(id)
                .orElseThrow(() -> new Exception("Usuário não encontrado com ID: " + id));
    }

    public User createUser(UserDTO data) {
        User newUser = new User(data);
        this.saveUser(newUser);
        return newUser;
    }

    public List<User> getAllUsers() {
        return this.repository.findAll();
    }

    public void saveUser(User user) {
        this.repository.save(user);
    }
}