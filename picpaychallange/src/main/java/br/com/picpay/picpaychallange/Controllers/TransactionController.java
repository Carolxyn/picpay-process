package br.com.picpay.picpaychallange.Controllers;

import br.com.picpay.picpaychallange.Dto.TransactionDTO;
import br.com.picpay.picpaychallange.services.TransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import br.com.picpay.picpaychallange.domain.transaction.Transaction;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    @Autowired
    private TransactionService transactionService;

    @PostMapping
    public ResponseEntity<?> createTransaction(@RequestBody TransactionDTO transaction) {
        try {
            Transaction newTransaction = this.transactionService.createTransaction(transaction);
            return new ResponseEntity<>(newTransaction, HttpStatus.CREATED);
        } catch (Exception e) {
            // Log detalhado do erro
            System.err.println("Erro na transação: " + e.getMessage());
            e.printStackTrace();

            return ResponseEntity.badRequest()
                    .body(Map.of("error", e.getMessage()));
        }
    }
}
