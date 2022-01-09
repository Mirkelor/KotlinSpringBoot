package mirkelor.account

import mirkelor.account.model.Customer
import mirkelor.account.repository.CustomerRepository
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class AccountApplication(private val customerRepository: CustomerRepository) : CommandLineRunner {

    @Throws(Exception::class)
    override fun run(vararg args: String) {
        val customer = customerRepository.save(Customer("", "Hasan", "Efe", HashSet()))
        println(customer)
    }

}

fun main(args: Array<String>) {
    SpringApplication.run(AccountApplication::class.java, *args)
}
