package uj.jwzp.kpnk.GymApp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.jwzp.kpnk.GymApp.controller.request.RegistrationCreateRequest;
import uj.jwzp.kpnk.GymApp.exception.event.EventFullyBookedException;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.model.Registration;
import uj.jwzp.kpnk.GymApp.repository.EventRepository;
import uj.jwzp.kpnk.GymApp.repository.RegistrationRepository;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;


import static org.mockito.BDDMockito.given;
import static org.assertj.core.api.Assertions.assertThatThrownBy;


@ExtendWith(MockitoExtension.class)
class RegistrationServiceTest {

    private static Event event;

    @Mock
    private RegistrationRepository repository;
    @Mock
    private EventRepository eventRepository;
    @InjectMocks
    private RegistrationService registrationService;

    @BeforeAll
    static void setUp() {
        String eventTitle = "Fitness class";
        event = new Event(2, "Fitness class", DayOfWeek.MONDAY, LocalTime.NOON, Duration.ofHours(1), 3, 4, LocalDate.now(), 3);
    }

    @Test
    void createRegistration_KeptLimit() {
        given(eventRepository.findById(2)).willReturn(Optional.of(event));
        given(repository.findByEventId(2)).willReturn(List.of(new Registration("Kate", "Tested", 2)));
        var registration = new Registration("Hannah", "Test", 2);
        given(repository.save(registration)).willReturn(registration);

        var newRegistration = registrationService.add(new RegistrationCreateRequest("Hannah", "Test", 2));

        Assertions.assertEquals(registration, newRegistration);
    }

    @Test
    void createRegistration_overPeopleLimit() {
        given(eventRepository.findById(2)).willReturn(Optional.of(event));
        given(repository.findByEventId(2)).willReturn(List.of(
                new Registration("Kate", "Tested", 2),
                new Registration("Suzie", "Test", 2),
                new Registration("Adam", "Test", 2)
                ));

        assertThatThrownBy(() -> registrationService.add(new RegistrationCreateRequest("Hannah", "Test", 2)))
                .isInstanceOf(EventFullyBookedException.class)
                .hasFieldOrPropertyWithValue("message", "Registration limit reached for the event with id:" + 2);
    }

}