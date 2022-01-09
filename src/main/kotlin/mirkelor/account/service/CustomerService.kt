package mirkelor.account.service

import mirkelor.account.dto.CustomerDto
import mirkelor.account.dto.CustomerDtoConverter
import mirkelor.account.exception.CustomerNotFoundException
import mirkelor.account.model.Customer
import mirkelor.account.repository.CustomerRepository
import org.springframework.stereotype.Service

@Service
class CustomerService(private val customerRepository: CustomerRepository, private val customerDtoConverter: CustomerDtoConverter) {

    fun findCustomerById(id: String): Customer {
        return customerRepository.findById(id).orElseThrow { CustomerNotFoundException("Customer could not find by id: $id") }!!
    }

    fun getCustomerById(customerId: String): CustomerDto {
        return customerDtoConverter.convert(findCustomerById(customerId))
    }
}