package uj.jwzp.kpnk.GymApp.service;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.jwzp.kpnk.GymApp.controller.request.EventCreateRequest;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachAlreadyBookedException;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.EventPastDateException;
import uj.jwzp.kpnk.GymApp.exception.event.EventTimeException;
import uj.jwzp.kpnk.GymApp.exception.event_template.PeopleLimitFormatException;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.Coach;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;
import uj.jwzp.kpnk.GymApp.repository.CoachRepository;
import uj.jwzp.kpnk.GymApp.repository.EventRepository;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class EventServiceTest {

    private static Club club;
    private static Coach coach;
    private static Map<DayOfWeek, OpeningHours> whenOpen;


    @Mock
    private EventRepository repository;

    @Mock
    private ClubRepository clubRepository;

    @Mock
    private CoachRepository coachRepository;

    @InjectMocks
    private EventService eventService;

    @BeforeAll
    static void setup() {
        whenOpen = new HashMap<>();
        whenOpen.put(DayOfWeek.MONDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(22, 0)));
        whenOpen.put(DayOfWeek.TUESDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(22, 0)));
        whenOpen.put(DayOfWeek.WEDNESDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.MAX));
        whenOpen.put(DayOfWeek.THURSDAY, new OpeningHours(LocalTime.of(0, 0), LocalTime.MAX));
        whenOpen.put(DayOfWeek.FRIDAY, new OpeningHours(LocalTime.of(1, 0), LocalTime.of(22, 0)));
        club = new Club(0, "testClub1", "testAddress1", whenOpen);
        coach = new Coach(0, "testCoach1", "testCoach1", 2000);
    }

    @Test
    void createValidEvent() {
        Event event = new Event(0, "event testowy", DayOfWeek.MONDAY, LocalTime.of(12, 0), Duration.ofHours(2), 0, 0, LocalDate.of(2022, 7, 1), 8);
        given(clubRepository.findById(0)).willReturn(Optional.of(club));
        given(coachRepository.findById(0)).willReturn(Optional.of(coach));
        given(repository.save(event)).willReturn(event);

        var newEvent = eventService.add(new EventCreateRequest("event testowy", LocalDate.of(2022, 7, 1), LocalTime.of(12, 0), Duration.ofHours(2), 0, 0, 8));
        assertEquals(event, newEvent);
    }

    @Test
    void createEvent_NonExistingCoach() {
        Event event = new Event(0, "event testowy", DayOfWeek.MONDAY, LocalTime.of(12, 0), Duration.ofHours(2), 0, 0, LocalDate.of(2022, 7, 1), 8);
        given(clubRepository.findById(0)).willReturn(Optional.of(club));
        given(coachRepository.findById(2)).willReturn(Optional.empty());


        assertThatThrownBy(() -> eventService.add(new EventCreateRequest("event testowy", LocalDate.of(2022, 6, 1), LocalTime.of(12, 0), Duration.ofHours(2), 0, 2, 8)))
                .isInstanceOf(CoachNotFoundException.class)
                .hasMessageContaining("Unknown coach id");
    }

    @Test
    void createEvent_InvalidPeopleLimit() {
        given(clubRepository.findById(0)).willReturn(Optional.of(club));
        given(coachRepository.findById(0)).willReturn(Optional.of(coach));

        assertThatThrownBy(() -> eventService.add(new EventCreateRequest("event testowy", LocalDate.of(2022, 7, 1), LocalTime.of(12, 0), Duration.ofHours(2), 0, 0, -5)))
                .isInstanceOf(PeopleLimitFormatException.class)
                .hasMessageContaining("People limit is negative");
    }

    @Test
    void createEvent_eventDateInPast() {
        given(clubRepository.findById(0)).willReturn(Optional.of(club));
        given(coachRepository.findById(0)).willReturn(Optional.of(coach));

        assertThatThrownBy(() -> eventService.add(new EventCreateRequest("event testowy", LocalDate.of(2022, 5, 1), LocalTime.of(12, 0), Duration.ofHours(2), 0, 0, 5)))
                .isInstanceOf(EventPastDateException.class)
                .hasMessageContaining("new event is in the past");
    }

    @Test
    void createEvent_eventOutsideClubOpeningHours() {
        given(clubRepository.findById(0)).willReturn(Optional.of(club));
        given(coachRepository.findById(0)).willReturn(Optional.of(coach));

        assertThatThrownBy(() -> eventService.add(new EventCreateRequest("event testowy", LocalDate.of(2022, 7, 1), LocalTime.of(22, 0), Duration.ofHours(2), 0, 0, 5)))
                .isInstanceOf(EventTimeException.class)
                .hasMessageContaining("not within opening hours of club");
    }

    @Test
    void changeEventDate() {
        Event event = new Event(0, "event testowy", DayOfWeek.MONDAY, LocalTime.of(12, 0), Duration.ofHours(2), 0, 0, LocalDate.of(2022, 7, 1), 8);
        given(repository.findById(0)).willReturn(Optional.of(event));
        given(clubRepository.findById(0)).willReturn(Optional.of(club));
        Event modifiedEvent = new Event(
                0,
                "event testowy",
                LocalDate.of(2022, 7, 5).getDayOfWeek(),
                LocalTime.of(15, 0),
                Duration.ofHours(2),
                0,
                0,
                LocalDate.of(2022, 7, 5),
                8);
        given(repository.save(modifiedEvent)).willReturn(modifiedEvent);
        var result = eventService.changeEventDate(0, LocalDate.of(2022, 7, 5), LocalTime.of(15, 0));

        assertEquals(modifiedEvent, result);
    }

    @Test
    void addEventWithCoachAlreadyBooked() {
        Event event = new Event(0, "event testowy", LocalDate.of(2022, 7, 1), Duration.ofHours(2), LocalTime.of(12, 0), 0, 0, 8);
        given(clubRepository.findById(0)).willReturn(Optional.of(club));
        given(coachRepository.findById(0)).willReturn(Optional.of(coach));
        given(repository.findByCoachIdAndEventDate(0, LocalDate.of(2022, 7, 1))).willReturn(Set.of(event));

        assertThatThrownBy(() -> eventService.add(new EventCreateRequest("event z zarezerwowanym trenerem", LocalDate.of(2022, 7, 1), LocalTime.of(13, 0), Duration.ofHours(2), 0, 0, 8)))
                .isInstanceOf(CoachAlreadyBookedException.class)
                .hasMessageContaining("is already booked during event hours");
    }
}