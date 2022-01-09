package mirkelor.account.repository

import mirkelor.account.model.Transaction
import org.springframework.data.jpa.repository.JpaRepository

interface TransactionRepository : JpaRepository<Transaction, String>