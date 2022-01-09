package mirkelor.account.dto

import mirkelor.account.model.Customer
import org.springframework.stereotype.Component
import java.util.*
import java.util.stream.Collectors

@Component
class CustomerDtoConverter(private val customerAccountDtoConverter: CustomerAccountDtoConverter) {

    fun convert(customer: Customer): CustomerDto {
        return CustomerDto(
            id = customer.id!!,
            name = customer.name!!,
            surname = customer.surname!!,
            accounts = getAccounts(customer)
        )
    }

    private fun getAccounts(customer: Customer): Set<CustomerAccountDto> {
        return customer.accounts.stream().map(customerAccountDtoConverter::convert).collect(Collectors.toSet())
    }

    fun convertToAccountCustomer(customer: Customer): AccountCustomerDto {
        return when {
            Objects.isNull(customer) -> AccountCustomerDto("", "", "")
            else -> AccountCustomerDto(customer.id!!, customer.name!!, customer.surname!!)
        }
    }
}