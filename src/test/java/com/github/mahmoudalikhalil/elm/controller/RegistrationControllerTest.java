package com.github.mahmoudalikhalil.elm.controller;

import com.github.mahmoudalikhalil.elm.model.dto.ClientUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.SuperUserRegistrationRequest;
import com.github.mahmoudalikhalil.elm.model.dto.SuperUserRole;
import com.github.mahmoudalikhalil.elm.service.RegistrationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class RegistrationControllerTest {
    @Mock
    private RegistrationService registrationService;

    @InjectMocks
    private RegistrationController registrationController;

    @Test
    void shouldReturnSuccessfulResponseWhenRegisterClientSucceed() {
        ClientUserRegistrationRequest request = new ClientUserRegistrationRequest("username", "password", "dummy@example.com");

        ResponseEntity<Void> response = registrationController.registerClient(request);

        assertThat(response).isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isSameAs(HttpStatus.OK);

        verify(registrationService).registerClient(request);
    }

    @Test
    void shouldThrowsExceptionWhenRegisterClientFails() {
        ClientUserRegistrationRequest invalidRequest = new ClientUserRegistrationRequest("duplicateUsername", "password", "dummy@example.com");

        doThrow(new DataValidationException("Duplicate username")).when(registrationService).registerClient(invalidRequest);

        assertThatThrownBy(() -> registrationController.registerClient(invalidRequest))
                .isExactlyInstanceOf(DataValidationException.class);

        verify(registrationService).registerClient(invalidRequest);
    }

    @Test
    void shouldReturnSuccessfulResponseWhenRegisterAdminOrDealerSucceed() {
        SuperUserRegistrationRequest request = new SuperUserRegistrationRequest("adminUser", "adminPassword", "admin@example.com", SuperUserRole.ADMIN);

        ResponseEntity<Void> response = registrationController.registerAdminOrDealer(request);

        assertThat(response).isNotNull()
                .extracting(ResponseEntity::getStatusCode)
                .isSameAs(HttpStatus.OK);

        verify(registrationService).registerAdminOrDealer(request);
    }

    @Test
    void shouldThrowsExceptionWhenRegisterAdminOrDealerFails() {
        SuperUserRegistrationRequest invalidRequest = new SuperUserRegistrationRequest("duplicateAdminUser", "adminPassword", "admin@example.com", SuperUserRole.ADMIN);

        doThrow(new DataValidationException("Duplicate username")).when(registrationService).registerAdminOrDealer(invalidRequest);

        assertThatThrownBy(() -> registrationController.registerAdminOrDealer(invalidRequest))
                .isExactlyInstanceOf(DataValidationException.class);

        verify(registrationService).registerAdminOrDealer(invalidRequest);
    }
}