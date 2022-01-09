package mirkelor.account.repository

import mirkelor.account.model.Account
import org.springframework.data.jpa.repository.JpaRepository

interface AccountRepository : JpaRepository<Account, String>