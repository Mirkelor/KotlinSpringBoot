package mirkelor.account.dto

import mirkelor.account.model.Account
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors

@Component
class AccountDtoConverter(private val customerDtoConverter: CustomerDtoConverter, private val transactionDtoConverter: TransactionDtoConverter) {

    fun convert(account: Account): AccountDto {
        return AccountDto(
            id = account.id,
            balance = account.balance,
            creationDate = account.creationDate,
            customer = customerDtoConverter.convertToAccountCustomer(account.customer!!),
            transactions = if (Objects.nonNull(account.transaction)) getTransactions(account) else HashSet()
        )
    }

    private fun getTransactions(account: Account): Set<TransactionDto> {
        return account.transaction!!.stream()
            .map(transactionDtoConverter::convert)
            .collect(Collectors.toSet());
    }
}