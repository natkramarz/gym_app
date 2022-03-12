package uj.jwzp.kpnk.GymApp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.coach.CoachNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.EventNotFoundException;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.Coach;
import uj.jwzp.kpnk.GymApp.model.Event;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;
import uj.jwzp.kpnk.GymApp.repository.CoachRepository;
import uj.jwzp.kpnk.GymApp.repository.EventRepository;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class EventServiceTest {

    private static Event event;
    private static Club club;
    private static Coach coach;
    @Mock
    private EventRepository eventRepository;
    @Mock
    private CoachRepository coachRepository;
    @Mock
    private ClubRepository clubRepository;
    @InjectMocks
    private EventService eventService;

    @BeforeAll
    static void setUp() {
        Map<DayOfWeek, OpeningHours> whenOpen = new HashMap<>();
        whenOpen.put(DayOfWeek.MONDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(22, 0)));
        club = new Club(1, "testClub1", "testAddress1", whenOpen);
        coach = new Coach(1, "testCoach1", "testCoach1", 2000);
        event = new Event(1, "testEvent", DayOfWeek.MONDAY, LocalTime.of(11,0), Duration.ofMinutes(30), 1, 1);
    }

    @Test
    public void getAllEventsWithOneClub() {
        given(eventRepository.allEvents()).willReturn(Set.of(event));
        var events = eventService.allEvents();

        assertThat(events).containsExactly(event);
    }

    @Test
    public void getAllEventsEmpty() {
        given(eventRepository.allEvents()).willReturn(Collections.emptySet());

        Assertions.assertTrue(eventService.allEvents().isEmpty());
    }

    @Test
    public void addValidEvent() {
        given(clubRepository.club(event.clubId())).willReturn(Optional.of(club));
        given(coachRepository.coach(event.coachId())).willReturn(Optional.of(coach));
        given(eventRepository.addEvent(event.title(), event.day(), event.time(), event.duration(), event.clubId(), event.coachId()))
                .willReturn(event);

        var serviceEvent = eventService.addEvent(event.title(), event.day(), event.time(), event.duration(), event.clubId(), event.coachId());
        Assertions.assertEquals(serviceEvent, event);
    }

    @Test
    public void addEventWithNonExistentCoach() {
        given(clubRepository.club(event.clubId())).willReturn(Optional.of(club));
        given(coachRepository.coach(3)).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.addEvent(event.title(), event.day(), event.time(), event.duration(), event.clubId(), 3))
                .isInstanceOf(CoachNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown coach id: 3");
    }

    @Test
    public void addEventWithNonExistentClub() {
        given(clubRepository.club(event.clubId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.addEvent(event.title(), event.day(), event.time(), event.duration(), event.clubId(), event.coachId()))
                .isInstanceOf(ClubNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown club id: " + event.clubId());
    }

    @Test
    public void eventsByValidClub() {
        given(clubRepository.club(event.clubId())).willReturn(Optional.of(club));
        given(eventRepository.eventsByClub(event.clubId())).willReturn(Set.of(event));

        var events = eventService.eventsByClub(event.clubId());
        assertThat(events).containsExactly(event);
    }

    @Test
    public void eventsByNonExistentClub() {
        given(clubRepository.club(event.clubId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.eventsByClub(event.clubId()))
                .isInstanceOf(ClubNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown club id: " + event.clubId());
    }

    @Test
    public void eventsByValidCoach() {
        given(coachRepository.coach(event.coachId())).willReturn(Optional.of(coach));
        given(eventRepository.eventsByCoach(event.coachId())).willReturn(Set.of(event));

        var events = eventService.eventsByCoach(event.coachId());
        assertThat(events).containsExactly(event);
    }

    @Test
    public void eventsByNonExistentCoach() {
        given(coachRepository.coach(event.coachId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.eventsByCoach(event.coachId()))
                .isInstanceOf(CoachNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown coach id: " + event.coachId());
    }

    @Test
    public void modifyValidEvent() {
        given(eventRepository.event(event.id())).willReturn(Optional.of(event));
        given(coachRepository.coach(event.coachId())).willReturn(Optional.of(coach));
        given(clubRepository.club(event.clubId())).willReturn(Optional.of(club));

        var uut = new Event(1, "modified", event.day(), event.time(), event.duration(), event.clubId(), event.coachId());
        given(eventRepository.modifyEvent(1, uut)).willReturn(uut);

        var serviceEvent = eventService.modifyEvent(1, "modified", event.day(), event.time(), event.duration(), event.clubId(), event.coachId());
        Assertions.assertEquals(serviceEvent, uut);
    }

    @Test
    public void modifyNonExistentEvent() {
        given(eventRepository.event(event.id())).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                eventService.modifyEvent(event.id(), event.title(), event.day(), event.time(), event.duration(), event.clubId(), event.coachId()))
                .isInstanceOf(EventNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown event id: " + event.id());
    }

    @Test
    public void modifyEventWithNonExistentClub() {
        given(eventRepository.event(event.id())).willReturn(Optional.of(event));
        given(coachRepository.coach(event.coachId())).willReturn(Optional.of(coach));
        given(clubRepository.club(event.clubId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                eventService.modifyEvent(event.id(), event.title(), event.day(), event.time(), event.duration(), event.clubId(), event.coachId()))
                .isInstanceOf(ClubNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown club id: " + event.clubId());
    }

    @Test
    public void modifyEventWithNonExistentCoach() {
        given(eventRepository.event(event.id())).willReturn(Optional.of(event));
        given(coachRepository.coach(event.coachId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                eventService.modifyEvent(event.id(), event.title(), event.day(), event.time(), event.duration(), event.clubId(), event.coachId()))
                .isInstanceOf(CoachNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown coach id: " + event.clubId());
    }

    @Test
    public void removeNonExistentEvent() {
        given(eventRepository.event(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.removeEvent(1))
                .isInstanceOf(EventNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown event id: 1");
    }
}
