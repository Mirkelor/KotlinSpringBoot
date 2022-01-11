package mirkelor.account.service

import com.nhaarman.mockito_kotlin.any
import com.nhaarman.mockito_kotlin.argumentCaptor
import com.nhaarman.mockito_kotlin.given
import com.nhaarman.mockito_kotlin.verify
import mirkelor.account.dto.*
import mirkelor.account.model.Account
import mirkelor.account.model.Customer
import mirkelor.account.model.Transaction
import mirkelor.account.model.TransactionType
import mirkelor.account.repository.AccountRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension
import java.math.BigDecimal
import java.time.LocalDateTime

@ExtendWith(MockitoExtension::class)
internal class AccountServiceTest {

    @InjectMocks
    private lateinit var accountService: AccountService

    @Mock
    private lateinit var accountRepository: AccountRepository

    @Mock
    private lateinit var accountDtoConverter: AccountDtoConverter

    @Mock
    private lateinit var customerService: CustomerService


    @Test
    fun `it should create account`() {
        // Given
        val now = LocalDateTime.of(2022, 1, 9, 23, 23, 23)
        val customer = Customer(
            id = "34",
            name = "name",
            surname = "surname",
            accounts = setOf())
        val accountCustomerDto = AccountCustomerDto(
            id = "34",
            name = "name",
            surname = "surname"
        )
        val transactionDto = TransactionDto(
            id = "36",
            transactionType = TransactionType.INITIAL,
            transactionDate = now,
            amount = BigDecimal.TEN
        )
        val accountDto = AccountDto(
            id = "35",
            balance = BigDecimal.TEN,
            creationDate = now,
            customer = accountCustomerDto,
            transactions = setOf(transactionDto)
        )
        val createAccountRequest = CreateAccountRequest("34", BigDecimal.TEN)

        given(customerService.findCustomerById("34")).willReturn(customer)
        given(accountDtoConverter.convert(any())).willReturn(accountDto)
        given(accountRepository.save(Mockito.any(Account::class.java))).willReturn(Account())

        // When
        val result = accountService.createAccount(createAccountRequest)

        // Then
        val argumentCaptor = argumentCaptor<Account>()
        assertThat(result).isEqualTo(accountDto)
        verify(accountRepository).save(argumentCaptor.capture())
        verify(accountDtoConverter).convert(argumentCaptor.capture())
        assertThat(argumentCaptor.firstValue.transaction?.first()?.amount).isEqualTo(BigDecimal.TEN)
        assertThat(argumentCaptor.firstValue.transaction?.first()?.account?.id).isEqualTo("")
        assertThat(argumentCaptor.firstValue.transaction?.first()?.account?.balance).isEqualTo("10")
        assertThat(argumentCaptor.firstValue.transaction?.first()?.account?.customer?.id).isEqualTo("34")
        assertThat(argumentCaptor.firstValue.transaction?.first()?.account?.customer?.name).isEqualTo("name")
        assertThat(argumentCaptor.firstValue.transaction?.first()?.account?.customer?.surname).isEqualTo("surname")
    }

    @Test
    fun `it should create account when initial credit was not greater than zero`() {
        // Given
        val now = LocalDateTime.of(2022, 1, 9, 23, 23, 23)
        val customer = Customer(
            id = "34",
            name = "name",
            surname = "surname",
            accounts = setOf())
        val accountCustomerDto = AccountCustomerDto(
            id = "34",
            name = "name",
            surname = "surname"
        )
        val transactionDto = TransactionDto(
            id = "36",
            transactionType = TransactionType.INITIAL,
            transactionDate = now,
            amount = BigDecimal.TEN
        )
        val accountDto = AccountDto(
            id = "35",
            balance = BigDecimal.TEN,
            creationDate = now,
            customer = accountCustomerDto,
            transactions = setOf(transactionDto)
        )
        val createAccountRequest = CreateAccountRequest("34", BigDecimal.ZERO)

        given(customerService.findCustomerById("34")).willReturn(customer)
        given(accountDtoConverter.convert(any())).willReturn(accountDto)
        given(accountRepository.save(Mockito.any(Account::class.java))).willReturn(Account())

        // When
        val result = accountService.createAccount(createAccountRequest)

        // Then
        val argumentCaptor = argumentCaptor<Account>()
        assertThat(result).isEqualTo(accountDto)
        verify(accountRepository).save(argumentCaptor.capture())
        verify(accountDtoConverter).convert(argumentCaptor.capture())
        assertThat(argumentCaptor.firstValue.transaction).isEqualTo(setOf<Transaction>())
        assertThat(argumentCaptor.firstValue.id).isEqualTo("")
        assertThat(argumentCaptor.firstValue.balance).isEqualTo("0")
        assertThat(argumentCaptor.firstValue.customer?.id).isEqualTo("34")
        assertThat(argumentCaptor.firstValue.customer?.name).isEqualTo("name")
        assertThat(argumentCaptor.firstValue.customer?.surname).isEqualTo("surname")
    }
}