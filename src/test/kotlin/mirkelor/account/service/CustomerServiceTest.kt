package mirkelor.account.service

import com.nhaarman.mockito_kotlin.given
import mirkelor.account.dto.CustomerDto
import mirkelor.account.dto.CustomerDtoConverter
import mirkelor.account.exception.CustomerNotFoundException
import mirkelor.account.model.Customer
import mirkelor.account.repository.CustomerRepository
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.verify
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@ExtendWith(MockitoExtension::class)
internal class CustomerServiceTest {

    @InjectMocks
    private lateinit var customerService: CustomerService

    @Mock
    private lateinit var customerRepository: CustomerRepository

    @Mock
    private lateinit var customerDtoConverter: CustomerDtoConverter

    @Test
    fun `it should find by customer id when customer id exists`() {
        // Given
        val customer = Customer("id", "name", "surname", setOf())
        given(customerRepository.findById("id")).willReturn(Optional.of(customer))

        // When
        val result = customerService.findCustomerById("id")

        // Then
        Assertions.assertThat(result).isEqualTo(customer)
    }

    @Test
    fun `it should find by customer id when customer id does not exists then throws customer not found exception`() {
        // Given
        given(customerRepository.findById("id")).willReturn(Optional.empty())

        // Then
        assertThrows(CustomerNotFoundException::class.java) { customerService.findCustomerById("id") }
    }

    @Test
    fun `it should get customer by id when customer id exists`() {
        // Given
        val customer = Customer("id", "name", "surname", setOf())
        val customerDto = CustomerDto("id", "name", "surname", setOf())
        given(customerRepository.findById("id")).willReturn(Optional.of(customer))
        given(customerDtoConverter.convert(customer)).willReturn(customerDto)

        // When
        val result = customerService.getCustomerById("id")

        // Then
        Assertions.assertThat(result).isEqualTo(customerDto)
        verify(customerRepository).findById("id")
        verify(customerDtoConverter).convert(customer)
    }

    @Test
    fun `it should get customer by id when customer id does not exists then throws customer not found exception`() {
        // Given
        given(customerRepository.findById("id")).willReturn(Optional.empty())

        // Then
        assertThrows(CustomerNotFoundException::class.java) { customerService.getCustomerById("id") }
        Mockito.verifyNoInteractions(customerDtoConverter)
    }
}