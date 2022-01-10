package mirkelor.account.controller

import mirkelor.account.dto.CustomerAccountDto
import mirkelor.account.dto.CustomerDto
import mirkelor.account.dto.TransactionDto
import mirkelor.account.model.TransactionType
import mirkelor.account.service.CustomerService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.Mockito.verify
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import java.math.BigDecimal
import java.time.LocalDateTime

@AutoConfigureMockMvc
@WebMvcTest
@ContextConfiguration(classes = [CustomerController::class, CustomerService::class])
internal class CustomerControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @MockBean
    private lateinit var customerService: CustomerService

    @Test
    @Throws(Exception::class)
    fun `it should get customer by id`() {
        // Given
        val now = LocalDateTime.of(2022, 1, 9, 23, 23, 23)
        val transactionDto = TransactionDto("36", TransactionType.INITIAL, BigDecimal.TEN, now)
        val customerAccountDto = CustomerAccountDto("35", BigDecimal.TEN, setOf(transactionDto), now)
        val customerDto = CustomerDto("34", "name", "surname", setOf(customerAccountDto))
        given(customerService.getCustomerById("34")).willReturn(customerDto)

        // When
        val resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/v1/customer/34")
        )

        // Then
        resultActions.andExpect(MockMvcResultMatchers.status().isOk)
            .andExpect(jsonPath("$.id").value("34"))
            .andExpect(jsonPath("$.name").value("name"))
            .andExpect(jsonPath("$.surname").value("surname"))
            .andExpect(jsonPath("$.accounts.[0].id").value("35"))
            .andExpect(jsonPath("$.accounts.[0].balance").value(10))
            .andExpect(jsonPath("$.accounts.[0].creationDate").value("2022-01-09T23:23:23"))
            .andExpect(jsonPath("$.accounts.[0].transactions.[0].id").value("36"))
            .andExpect(jsonPath("$.accounts.[0].transactions.[0].transactionType").value("INITIAL"))
            .andExpect(jsonPath("$.accounts.[0].transactions.[0].amount").value(10))
            .andExpect(jsonPath("$.accounts.[0].transactions.[0].transactionDate").value("2022-01-09T23:23:23"))
        verify(customerService).getCustomerById("34")
    }
}