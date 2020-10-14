package com.ladys.space.api.services

import com.ladys.space.api.constants.ErrorMessage
import com.ladys.space.api.constants.ErrorMessage.Keys.DUPLICATE_EMAIL
import com.ladys.space.api.constants.ErrorMessage.Keys.INVALID_EMERGENCY_CONTACT
import com.ladys.space.api.constants.ErrorMessage.Keys.INVALID_MAX_EMERGENCY_CONTACTS
import com.ladys.space.api.constants.ErrorMessage.Keys.NULL_ADDRESS
import com.ladys.space.api.constants.ErrorMessage.Keys.NULL_CONTACT
import com.ladys.space.api.constants.ErrorMessage.Keys.NULL_FIELDS
import com.ladys.space.api.constants.ErrorMessage.Keys.NULL_USER
import com.ladys.space.api.errors.exceptions.BadValueException
import com.ladys.space.api.models.AddressModel
import com.ladys.space.api.models.ContactModel
import com.ladys.space.api.models.EmergencyContactModel
import com.ladys.space.api.models.UserModel
import com.ladys.space.api.models.dto.*
import com.ladys.space.api.repositories.AddressRepository
import com.ladys.space.api.repositories.ContactRepository
import com.ladys.space.api.repositories.EmergencyContactRepository
import com.ladys.space.api.repositories.UserRepository
import com.ladys.space.api.services.helpers.ApiHelper
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*
import java.util.stream.Collectors

@Service
class UserService {

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var addressRepository: AddressRepository

    @Autowired
    private lateinit var contactRepository: ContactRepository

    @Autowired
    private lateinit var emergencyContactRepository: EmergencyContactRepository

    @Autowired
    private lateinit var encoder: BCryptPasswordEncoder

    @Autowired
    private lateinit var apiHelper: ApiHelper

    @Autowired
    private lateinit var errorMessages: ErrorMessage

    fun registerUser(registerUserDTO: RegisterUserDTO, locale: Locale): Unit =
            this.verifyFields(registerUserDTO, locale)

    @Throws(BadValueException::class)
    private fun verifyFields(model: RegisterUserDTO, locale: Locale) {
        var userModel: UserModel? = null
        var addressModel: AddressModel? = null
        var contactModel: ContactModel? = null
        var emergencyContactModel: List<EmergencyContactModel>? = null

        with(model) {
            this.user?.let { userModel = verifyDTO(it, locale) }
                    ?: throw BadValueException(errorMessages.getMessage(NULL_USER, locale))

            this.address?.let { addressModel = verifyDTO(it, locale) }
                    ?: throw BadValueException(errorMessages.getMessage(NULL_ADDRESS, locale))

            if (this.contact == null) throw BadValueException(errorMessages.getMessage(NULL_CONTACT, locale))

            this.contact.personalContact?.let {
                contactModel = verifyDTO(it, locale)
            } ?: throw BadValueException(errorMessages.getMessage(NULL_FIELDS, locale))

            this.contact.emergencyContacts?.let {
                emergencyContactModel = verifyDTO(it, locale)
            } ?: throw BadValueException(errorMessages.getMessage(INVALID_EMERGENCY_CONTACT, locale))
        }

        with(userModel!!) {
            save(this)
            save(addressModel!!, this)
            save(contactModel!!, this)
            save(emergencyContactModel!!, this)

            this.apply {
                this.address = addressModel
                this.contact = contactModel
            }.also { save(it) }
        }
    }

    private fun dtoToModel(dto: UserDTO, locale: Locale): UserModel =
            with(dto) {
                UserModel(
                        name = this.name!!,
                        lastName = this.lastName!!,
                        email = this.email!!,
                        gender = this.gender!!,
                        birthDate = apiHelper.validateMinimumAge(this.birthDate!!, locale),
                        password = encoder.encode(this.password)
                )
            }

    private fun dtoToModel(dto: AddressDTO): AddressModel =
            with(dto) {
                AddressModel(
                        zipCode = this.zipCode!!,
                        neighbourhood = this.neighbourhood!!,
                        address = this.address!!,
                        city = this.city!!,
                        state = this.state!!
                )
            }

    private fun dtoToModel(dto: PersonalContactDTO): ContactModel =
            with(dto) {
                ContactModel(
                        areaCode = this.areaCode!!,
                        phoneNumber = this.phoneNumber!!
                )
            }

    private fun dtoToListModel(dto: List<EmergencyContactDTO>): List<EmergencyContactModel> =
            with(dto) {
                return stream().map {
                    EmergencyContactModel(
                            name = it.name,
                            areaCode = it.areaCode,
                            emergencyPhone = it.phoneNumber)
                }.collect(Collectors.toList())
            }

    private fun verifyDTO(dto: UserDTO, locale: Locale): UserModel =
            with(dto) {
                apiHelper.verifyNullFields(this, locale)
                apiHelper.validateEmail(this.email!!, locale)
                verifyDuplicity(this.email, locale)
                apiHelper.verifyPasswordLength(this.password!!, locale)
                dtoToModel(dto, locale)
            }

    private fun verifyDTO(dto: AddressDTO, locale: Locale): AddressModel =
            with(dto) {
                apiHelper.verifyNullFields(this, locale)
                apiHelper.validateZipCode(this.zipCode!!, locale)
                dtoToModel(dto)
            }

    private fun verifyDTO(dto: PersonalContactDTO, locale: Locale): ContactModel =
            with(dto) {
                apiHelper.verifyNullFields(this, locale)
                apiHelper.verifyAreaCode(this.areaCode!!, locale)
                apiHelper.validatePhoneNumber(this.phoneNumber!!, locale)
                dtoToModel(dto)
            }

    private fun verifyDTO(emergencyContacts: List<EmergencyContactDTO>, locale: Locale): List<EmergencyContactModel> =
            with(emergencyContacts) {
                if (this.isNotEmpty())
                    this.forEach { emergencyContactDTO: EmergencyContactDTO ->
                        apiHelper.verifyNullFields(emergencyContactDTO, locale)
                        apiHelper.verifyAreaCode(emergencyContactDTO.areaCode!!, locale)
                        apiHelper.validatePhoneNumber(emergencyContactDTO.phoneNumber!!, locale)
                    }

                if (this.size > 3) throw BadValueException(errorMessages.getMessage(INVALID_MAX_EMERGENCY_CONTACTS, locale))

                dtoToListModel(this)
            }

    @Throws(BadValueException::class)
    private fun verifyDuplicity(email: String, locale: Locale) {
        this.userRepository.findByEmail(email)?.let {
            throw BadValueException(this.errorMessages.getMessage(DUPLICATE_EMAIL, locale))
        }
    }

    @Transactional
    fun save(model: UserModel) {
        this.userRepository.save(model)
    }

    @Transactional
    fun save(model: AddressModel, userModel: UserModel) {
        model.apply { this.user = userModel }.also { this.addressRepository.save(it) }
    }

    @Transactional
    fun save(model: ContactModel, userModel: UserModel) {
        model.apply { this.user = userModel }.also { this.contactRepository.save(it) }
    }

    @Transactional
    fun save(model: List<EmergencyContactModel>, userModel: UserModel) {
        model.stream().map { it.apply { this.user = userModel } }.collect(Collectors.toList()).also {
            this.emergencyContactRepository.saveAll(it)
        }
    }

}