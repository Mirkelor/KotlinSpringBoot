package mirkelor.account.dto

import mirkelor.account.model.Transaction
import org.springframework.stereotype.Component

@Component
class TransactionDtoConverter {

    fun convert(transaction: Transaction): TransactionDto {
        return TransactionDto(
            id = transaction.id,
            transactionType = transaction.transactionType,
            amount = transaction.amount,
            transactionDate = transaction.transactionDate
        )
    }
}