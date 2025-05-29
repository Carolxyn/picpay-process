package br.com.picpay.picpaychallange.repository;

import br.com.picpay.picpaychallange.domain.transaction.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TransactionRepository  extends JpaRepository<Transaction, Long> {
}
