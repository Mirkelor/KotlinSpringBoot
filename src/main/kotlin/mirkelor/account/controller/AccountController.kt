package mirkelor.account.controller

import mirkelor.account.dto.AccountDto
import mirkelor.account.dto.CreateAccountRequest
import mirkelor.account.service.AccountService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid

@RestController
@RequestMapping("/v1/account")
class AccountController(private val accountService: AccountService) {

    @PostMapping
    fun createAccount(@RequestBody createAccountRequest: @Valid CreateAccountRequest): ResponseEntity<AccountDto> {
        return ResponseEntity.ok(accountService.createAccount(createAccountRequest))
    }
}