package mirkelor.account.controller

import mirkelor.account.dto.CustomerDto
import mirkelor.account.service.CustomerService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/customer")
class CustomerController(private val customerService: CustomerService) {

    @GetMapping("/{customerId}")
    fun getCustomerById(@PathVariable customerId: String): ResponseEntity<CustomerDto> {
        return ResponseEntity.ok(customerService.getCustomerById(customerId))
    }
}