package mirkelor.account.service

import mirkelor.account.dto.AccountDto
import mirkelor.account.dto.AccountDtoConverter
import mirkelor.account.dto.CreateAccountRequest
import mirkelor.account.model.Account
import mirkelor.account.model.Transaction
import mirkelor.account.repository.AccountRepository
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDateTime

@Service
class AccountService(
    private val accountRepository: AccountRepository,
    private val customerService: CustomerService,
    private val accountDtoConverter: AccountDtoConverter
) {

    fun createAccount(createAccountRequest: CreateAccountRequest): AccountDto {
        val customer = customerService.findCustomerById(createAccountRequest.customerId)
        var account = Account(
            customer,
            createAccountRequest.initialCredit,
            LocalDateTime.now()
        )
        if (createAccountRequest.initialCredit > BigDecimal.ZERO) {
            val transaction = Transaction(createAccountRequest.initialCredit, account)
            account = account.copy(transaction = setOf(transaction));
        }
        return accountDtoConverter.convert(accountRepository.save(account))
    }
}