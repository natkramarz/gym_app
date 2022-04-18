package uj.jwzp.kpnk.GymApp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.jwzp.kpnk.GymApp.exception.coach.AssignedEventsException;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.model.Coach;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.repository.CoachRepository;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class CoachServiceTest {

    private static Coach coach;
    @Mock
    private CoachRepository coachRepository;
    @Mock
    private EventService eventService;
    @InjectMocks
    private CoachService coachService;

    @BeforeAll
    static void setUp() {
        coach = new Coach(1, "testCoach", "testCoach", 2000);
    }


    @Test
    public void getAllCoachesWithOneCoach() {
        given(coachRepository.findAll()).willReturn(List.of(coach));

        var coaches = coachService.allCoaches();
        assertThat(coaches).containsExactly(coach);
    }

    @Test
    public void getAllCoachesEmpty() {
        given(coachRepository.findAll()).willReturn(List.of());

        Assertions.assertTrue(coachService.allCoaches().isEmpty());
    }

    @Test
    public void addValidCoach() {
        given(coachService.addCoach(coach.getFirstName(), coach.getLastName(), coach.getYearOfBirth())).willReturn(coach);

        var serviceCoach = coachService.addCoach(coach.getFirstName(), coach.getLastName(), coach.getYearOfBirth());
        Assertions.assertEquals(serviceCoach, coach);
    }
    @Test
    public void modifyValidCoach() {
        given(coachRepository.findById(1)).willReturn(Optional.of(coach));
        Coach uut = new Coach(1, "modified1", "modified2", 2001);
        given(coachRepository.save(uut)).willReturn(uut);

        Coach serviceCoach = coachService.modifyCoach(1, "modified1", "modified2", 2001);

        Assertions.assertEquals(serviceCoach, uut);
    }

    @Test
    public void modifyNonExistentClub() {
        given(coachRepository.findById(2)).willReturn(Optional.empty());

        assertThatThrownBy(() -> coachService.modifyCoach(2, "test", "test", 2000))
                .isInstanceOf(CoachNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown coach id: 2");
    }

    @Test
    public void removeNonExistentCoach() {
        given(coachRepository.findById(anyInt())).willReturn(Optional.empty());

        assertThatThrownBy(() -> coachService.removeCoach(1))
                .isInstanceOf(CoachNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown coach id: 1");
    }

    @Test
    public void removeCoachWithAssignedEvents() {
        given(coachRepository.findById(1)).willReturn(Optional.of(coach));
        given(eventService.eventsByCoach(1)).willReturn(List.of(new Event()));

        assertThatThrownBy(() -> coachService.removeCoach(1))
                .isInstanceOf(AssignedEventsException.class)
                .hasMessageContaining("There are events assigned to");
    }
}
