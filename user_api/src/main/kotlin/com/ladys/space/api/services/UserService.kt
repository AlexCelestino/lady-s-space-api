package com.ladys.space.api.services

import com.ladys.space.api.services.ErrorMessageService.Keys.EQUAL_PASSWORDS
import com.ladys.space.api.services.ErrorMessageService.Keys.INCORRECT_LOGIN
import com.ladys.space.api.services.ErrorMessageService.Keys.USER_NOT_FOUND
import com.ladys.space.api.errors.exceptions.BadCredentialsException
import com.ladys.space.api.errors.exceptions.BadValueException
import com.ladys.space.api.errors.exceptions.UserNotFoundException
import com.ladys.space.api.models.AddressModel
import com.ladys.space.api.models.UserModel
import com.ladys.space.api.models.dto.*
import com.ladys.space.api.repositories.AddressRepository
import com.ladys.space.api.repositories.UserRepository
import com.ladys.space.api.security.JwtSecurity
import com.ladys.space.api.services.helpers.ConverterHelper.Companion.toBase64
import com.ladys.space.api.services.helpers.ValidationHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId

@Service
class UserService : UserDetailsService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var encoder: BCryptPasswordEncoder

    @Autowired
    private lateinit var errorMessagesService: ErrorMessageService

    @Autowired
    private lateinit var jwtSecurity: JwtSecurity

    private val helper: ValidationHelper by lazy {
        ValidationHelper(this.userRepository, this.errorMessagesService, this.encoder, this.jwtSecurity)
    }

    fun authenticateUser(loginDTO: LoginDTO): AuthenticatedUserDTO {
        val userDetails: UserDetails = loadUserByUsername(loginDTO.email)
                ?: throw BadCredentialsException(errorMessagesService.getMessage(INCORRECT_LOGIN))

        val user: UserModel = this.userRepository.findByEmail(loginDTO.email)!!

        val token: String by lazy { this.jwtSecurity.generateToken(loginDTO.email) }

        val expireDate: String by lazy {
            this.jwtSecurity.expireDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime().run {
                toBase64(this.toString())
            }
        }

        return if (!this.helper.isPasswordsEquals(loginDTO.password, userDetails.password))
            throw BadCredentialsException(errorMessagesService.getMessage(INCORRECT_LOGIN))
        else
            with(user) {
                AuthenticatedUserDTO(this.name, this.lastName, this.email, this.address!!.address, token, expireDate)
            }
    }

    override fun loadUserByUsername(email: String): UserDetails? = this.userRepository.findByEmail(email)?.let {
        User.builder().username(it.email).password(it.password).roles("").build()
    }

    @Transactional
    fun registerUser(registerDTO: RegisterDTO): Unit = with(registerDTO) {
        val user = UserModel(
                null,
                this.name,
                this.lastName,
                helper.verifyDuplicity(this.email),
                helper.validateMinimumAge(this.birthDate),
                this.gender[0],
                encoder.encode(this.password),
                null
        )

        val address = AddressModel(
                null,
                this.zipCode,
                this.address,
                this.neighbourhood,
                this.city,
                this.state,
                user
        )

        userRepository.save(user)

        addressRepository.save(address).also { user.apply { this.address = it }.also { userRepository.save(it) } }
    }

    fun getUser(rawToken: String): UserModel = this.helper.getLoginFromToken(rawToken).run {
        userRepository.findByEmail(this) ?: throw BadValueException(errorMessagesService.getMessage(USER_NOT_FOUND))
    }

    @Transactional
    fun updateUser(profileDTO: ProfileDTO): UserModel = this.userRepository.findByEmail(profileDTO.email)?.let {
        it.apply {
            with(profileDTO) {
                this@apply.name = this.name
                this@apply.lastName = this.lastName
                this@apply.email = this.email
                this@apply.birthDate = this.birthDate
                this@apply.gender = this.gender[0]
                this@apply.address?.zipCode = this.zipCode
                this@apply.address?.address = this.address
                this@apply.address?.neighbourhood = this.name
                this@apply.address?.city = this.city
                this@apply.address?.state = this.address
            }
        }
        this.userRepository.save(it)
    } ?: throw  UserNotFoundException(USER_NOT_FOUND)

    @Transactional
    fun updatePassword(rawToken: String, passwordDTO: PasswordDTO) {
        val user: UserModel = this.helper.getLoginFromToken(rawToken).run {
            userRepository.findByEmail(this) ?: throw BadValueException(errorMessagesService.getMessage(USER_NOT_FOUND))
        }

        if (this.helper.isPasswordsEquals(passwordDTO.password, user.password))
            throw BadValueException(this.errorMessagesService.getMessage(EQUAL_PASSWORDS))

        user.apply { user.password = encoder.encode(passwordDTO.password) }.also { this.userRepository.save(it) }
    }

}
