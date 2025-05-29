package br.com.picpay.picpaychallange.services;

import br.com.picpay.picpaychallange.Dto.TransactionDTO;
import br.com.picpay.picpaychallange.domain.transaction.Transaction;
import br.com.picpay.picpaychallange.domain.user.User;
import br.com.picpay.picpaychallange.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
public class TransactionService {

    @Autowired
    private UserService userService;

    @Autowired
    private TransactionRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @Transactional
    public Transaction createTransaction(TransactionDTO transactionDTO) throws Exception {
        // Validações iniciais
        if (transactionDTO == null) {
            throw new Exception("Dados da transação são obrigatórios");
        }

        if (transactionDTO.senderId().equals(transactionDTO.receiverId())) {
            throw new Exception("Não é possível transferir para si mesmo");
        }

        // Busca usuários - com verificação de existência
        User sender = this.userService.findUserById(transactionDTO.senderId());
        User receiver = this.userService.findUserById(transactionDTO.receiverId());

        if (sender == null) {
            throw new Exception("Usuário remetente não encontrado");
        }

        if (receiver == null) {
            throw new Exception("Usuário destinatário não encontrado");
        }

        // CORREÇÃO: Validação completa ANTES da autorização
        System.out.println("Validando transação...");
        System.out.println("Sender: " + sender.getFirstname() + " (" + sender.getUserType() + ")");
        System.out.println("Receiver: " + receiver.getFirstname() + " (" + receiver.getUserType() + ")");
        System.out.println("Saldo atual: " + sender.getBalance());
        System.out.println("Valor da transação: " + transactionDTO.value());

        userService.validateTransaction(sender, transactionDTO.value(), receiver);

        // Verifica autorização externa
        boolean isAuthorized = this.authorizationTransaction(sender, transactionDTO.value());
        if (!isAuthorized) {
            throw new Exception("Transação não autorizada pelo serviço externo");
        }

        // Cria a transação
        Transaction newTransaction = new Transaction();
        newTransaction.setAmount(transactionDTO.value());
        newTransaction.setSender(sender);
        newTransaction.setReceiver(receiver);
        newTransaction.setTimestamp(LocalDateTime.now());

        // CORREÇÃO: Atualiza saldos de forma mais segura
        BigDecimal transactionAmount = transactionDTO.value();
        BigDecimal newSenderBalance = sender.getBalance().subtract(transactionAmount);
        BigDecimal newReceiverBalance = receiver.getBalance().add(transactionAmount);

        sender.setBalance(newSenderBalance);
        receiver.setBalance(newReceiverBalance);

        // Salva primeiro a transação, depois os usuários
        Transaction savedTransaction = this.repository.save(newTransaction);
        this.userService.saveUser(sender);
        this.userService.saveUser(receiver);

        System.out.println("Transação criada com sucesso. ID: " + savedTransaction.getId());
        return savedTransaction;
    }

    public boolean authorizationTransaction(User sender, BigDecimal value) {
        try {
            ResponseEntity<Map> authorizationResponse = restTemplate.getForEntity(
                    "https://run.mocky.io/v3/ad30abce-b1f5-4674-87b3-0cd526154a18",
                    Map.class
            );

            if (authorizationResponse.getStatusCode() == HttpStatus.OK) {
                String message = (String) authorizationResponse.getBody().get("message");
                return "Autorizado".equalsIgnoreCase(message);
            } else {
                return false;
            }
        } catch (Exception e) {
            System.err.println("Erro na autorização: " + e.getMessage());
            return false;
        }
    }
}