package mirkelor.account.dto

import mirkelor.account.model.Account
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class CustomerAccountDtoConverter(private val transactionDtoConverter: TransactionDtoConverter) {

    fun convert(account: Account): CustomerAccountDto {
        return CustomerAccountDto(
            id = account.id!!,
            balance = account.balance,
            transactions = getTransactions(account),
            creationDate = account.creationDate
        )
    }

    private fun getTransactions(account: Account): Set<TransactionDto>? {
        return account.transaction?.stream()?.map(transactionDtoConverter::convert)?.collect(Collectors.toSet())
    }
}