package mirkelor.account.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.nhaarman.mockito_kotlin.argumentCaptor
import mirkelor.account.dto.AccountCustomerDto
import mirkelor.account.dto.AccountDto
import mirkelor.account.dto.CreateAccountRequest
import mirkelor.account.dto.TransactionDto
import mirkelor.account.model.TransactionType
import mirkelor.account.service.AccountService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.http.MediaType
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import java.math.BigDecimal
import java.time.LocalDateTime

@AutoConfigureMockMvc
@WebMvcTest
@ContextConfiguration(classes = [AccountController::class, AccountService::class])
internal class AccountControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var accountService: AccountService

    @Test
    @Throws(Exception::class)
    fun `it should create account`() {
        // Given
        val now = LocalDateTime.of(2022, 1, 9, 23, 23, 23)
        val createAccountRequest = CreateAccountRequest("34", BigDecimal.TEN)
        val accountCustomerDto = AccountCustomerDto("35", "name", "surname")
        val transactionDto = TransactionDto("36", TransactionType.INITIAL, BigDecimal.TEN, now)
        val accountDto = AccountDto(
            "34",
            BigDecimal.TEN,
            now,
            accountCustomerDto,
            mutableSetOf(transactionDto)
        )
        given(accountService.createAccount(createAccountRequest)).willReturn(accountDto)

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/v1/account")
                .contentType(MediaType.APPLICATION_JSON)
                .content(ObjectMapper().writeValueAsString(createAccountRequest))
        )

        // Then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(MockMvcResultMatchers.jsonPath("$.id").value("34"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.balance").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$.creationDate").value("2022-01-09T23:23:23"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.customer.id").value("35"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.customer.name").value("name"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.customer.surname").value("surname"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transactions.[0].id").value("36"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transactions.[0].transactionType").value("INITIAL"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transactions.[0].amount").value(10))
            .andExpect(MockMvcResultMatchers.jsonPath("$.transactions.[0].transactionDate").value("2022-01-09T23:23:23"))
        val argumentCaptor = argumentCaptor<CreateAccountRequest>()
        verify(accountService).createAccount(argumentCaptor.capture())
        assertThat(argumentCaptor.firstValue.customerId).isEqualTo("34")
        assertThat(argumentCaptor.firstValue.initialCredit).isEqualTo(BigDecimal.TEN)
    }
}