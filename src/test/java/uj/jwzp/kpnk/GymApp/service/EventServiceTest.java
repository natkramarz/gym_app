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
import uj.jwzp.kpnk.GymApp.exception.event.EventDurationException;
import uj.jwzp.kpnk.GymApp.exception.event.EventNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.EventTimeException;
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
        whenOpen.put(DayOfWeek.TUESDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(22, 0)));
        whenOpen.put(DayOfWeek.WEDNESDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.MAX));
        whenOpen.put(DayOfWeek.THURSDAY, new OpeningHours(LocalTime.of(0, 0), LocalTime.MAX));
        whenOpen.put(DayOfWeek.FRIDAY, new OpeningHours(LocalTime.of(1, 0), LocalTime.of(22, 0)));
        club = new Club(0, "testClub1", "testAddress1", whenOpen);
        coach = new Coach(0, "testCoach1", "testCoach1", 2000);
        event = new Event(0, "testEvent", DayOfWeek.MONDAY, LocalTime.of(11,0), Duration.ofMinutes(30), 1, 1);
    }


    @Test
    public void getAllEventsWithOneClub() {
        given(eventRepository.findAll()).willReturn(List.of(event));
        var events = eventService.allEvents();

        assertThat(events).containsExactly(event);
    }

    @Test
    public void getAllEventsEmpty() {
        given(eventRepository.findAll()).willReturn(List.of());

        Assertions.assertTrue(eventService.allEvents().isEmpty());
    }

    @Test
    public void addValidEvent() {
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.of(club));
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.of(coach));
        given(eventService.addEvent(event.getTitle(), event.getDay(), event.getTime(), event.getDuration(), event.getClubId(), event.getCoachId())).willReturn(event);
        given(eventRepository.save(event)).willReturn(event);
        var serviceEvent = eventService.addEvent(event.getTitle(), event.getDay(), event.getTime(), event.getDuration(), event.getClubId(), event.getCoachId());
        Assertions.assertEquals(serviceEvent, event);
    }

    @Test
    public void addEventWithNonExistentCoach() {
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.of(club));
        given(coachRepository.findById(3)).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.addEvent(event.getTitle(), event.getDay(), event.getTime(), event.getDuration(), event.getClubId(), 3))
                .isInstanceOf(CoachNotFoundException.class)
                .hasMessageContaining("Unknown coach id");
    }

    @Test
    public void addEventWithNonExistentClub() {
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.addEvent(event.getTitle(), event.getDay(), event.getTime(), event.getDuration(), event.getClubId(), event.getCoachId()))
                .isInstanceOf(ClubNotFoundException.class)
                .hasMessageContaining("Unknown club id");
    }

    @Test
    public void eventsByValidClub() {
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.of(club));
        given(eventRepository.findByClubId(event.getClubId())).willReturn(List.of(event));

        var events = eventService.eventsByClub(event.getClubId());
        assertThat(events).containsExactly(event);
    }

    @Test
    public void eventsByNonExistentClub() {
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.eventsByClub(event.getClubId()))
                .isInstanceOf(ClubNotFoundException.class)
                .hasMessageContaining("Unknown club id: " + event.getClubId());
    }

    @Test
    public void eventsByValidCoach() {
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.of(coach));
        given(eventRepository.findByCoachId(event.getCoachId())).willReturn(List.of(event));

        var events = eventService.eventsByCoach(event.getCoachId());
        assertThat(events).containsExactly(event);
    }

    @Test
    public void eventsByNonExistentCoach() {
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.eventsByCoach(event.getCoachId()))
                .isInstanceOf(CoachNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown coach id: " + event.getCoachId());
    }

    @Test
    public void modifyValidEvent() {
        given(eventRepository.findById(event.getId())).willReturn(Optional.of(event));
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.of(club));

        var uut = new Event(0, "modified", event.getDay(), event.getTime(), event.getDuration(), event.getClubId(), event.getCoachId());
        given(eventRepository.save(uut)).willReturn(uut);

        var serviceEvent = eventService.modifyEvent(0, "modified", event.getDay(), event.getTime(), event.getDuration(), event.getClubId(), event.getCoachId());
        Assertions.assertEquals(serviceEvent, uut);
    }

    @Test
    public void modifyNonExistentEvent() {
        given(eventRepository.findById(event.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                eventService.modifyEvent(event.getId(), event.getTitle(), event.getDay(), event.getTime(), event.getDuration(), event.getClubId(), event.getCoachId()))
                .isInstanceOf(EventNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown event id: " + event.getId());
    }

    @Test
    public void modifyEventWithNonExistentClub() {
        given(eventRepository.findById(event.getId())).willReturn(Optional.of(event));
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                eventService.modifyEvent(event.getId(), event.getTitle(), event.getDay(), event.getTime(), event.getDuration(), event.getClubId(), event.getCoachId()))
                .isInstanceOf(ClubNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown club id: " + event.getClubId());
    }

    @Test
    public void modifyEventWithNonExistentCoach() {
        given(eventRepository.findById(event.getId())).willReturn(Optional.of(event));
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                eventService.modifyEvent(event.getId(), event.getTitle(), event.getDay(), event.getTime(), event.getDuration(), event.getClubId(), event.getCoachId()))
                .isInstanceOf(CoachNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown coach id: " + event.getClubId());
    }

    @Test
    public void removeNonExistentEvent() {
        given(eventRepository.findById(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.removeEvent(1))
                .isInstanceOf(EventNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown event id: 1");
    }

    @Test
    public void addEventLongerThan24Hours() {
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.of(club));
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.of(coach));

        assertThatThrownBy(() -> eventService.addEvent(event.getTitle(), event.getDay(), event.getTime(), Duration.ofHours(30), event.getClubId(), event.getCoachId()))
                .isInstanceOf(EventDurationException.class)
                .hasFieldOrPropertyWithValue("message", "Event duration too long: " + event.getTitle());
    }

    @Test
    public void modifyEventToBeLongerThan24Hours() {
        given(eventRepository.findById(event.getId())).willReturn(Optional.of(event));
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.of(club));
        var uut = new Event(0, "modified", event.getDay(), event.getTime(), event.getDuration(), event.getClubId(), event.getCoachId());

        assertThatThrownBy(() -> eventService.modifyEvent(0, "modified", event.getDay(), event.getTime(), Duration.ofHours(30), event.getClubId(), event.getCoachId()))
                .isInstanceOf(EventDurationException.class)
                .hasMessageContaining("Event duration too long");
    }

    @Test
    public void addEventBeforeClubOpeningHour() {
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.of(club));

        assertThatThrownBy(() -> eventService.addEvent(event.getTitle(), event.getDay(), LocalTime.of(6,0), Duration.ofHours(1), event.getClubId(), event.getCoachId()))
                .isInstanceOf(EventTimeException.class)
                .hasMessageContaining("Event not within the club's opening hours");
    }

    @Test
    public void addEventAfterClubClosingHour() {
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.of(club));

        assertThatThrownBy(() -> eventService.addEvent(event.getTitle(), event.getDay(), LocalTime.of(23,0), Duration.ofMinutes(30), event.getClubId(), event.getCoachId()))
                .isInstanceOf(EventTimeException.class)
                .hasMessageContaining("Event not within the club's opening hours");
    }

    @Test
    public void addEvent_ClubOpen24per7() {
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.of(club));
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.of(coach));
        var event = new Event(0, "testEvent1", DayOfWeek.WEDNESDAY, LocalTime.of(22,0), Duration.ofHours(4), EventServiceTest.event.getClubId(), EventServiceTest.event.getCoachId());
        given(eventRepository.save(event)).willReturn(event);
        var serviceEvent = eventService.addEvent(event.getTitle(), event.getDay(), event.getTime(), event.getDuration(), event.getClubId(), event.getCoachId());
        Assertions.assertEquals(serviceEvent, event);
    }

    @Test
    public void addEvent_EventStartingDuringOpeningHours_AndEndingWhenClubIsClosed() {
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.of(club));

        assertThatThrownBy(() -> eventService.addEvent(event.getTitle(), DayOfWeek.MONDAY, LocalTime.of(20,0), Duration.ofHours(5), event.getClubId(), event.getCoachId()))
                .isInstanceOf(EventTimeException.class)
                .hasMessageContaining("Event not within the club's opening hours");
    }

    @Test
    public void addEvent_EventStartingBeforeOpeningHours_AndEndingWhenClubIsOpen() {
        given(coachRepository.findById(event.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(event.getClubId())).willReturn(Optional.of(club));

        assertThatThrownBy(() -> eventService.addEvent(event.getTitle(), DayOfWeek.MONDAY, LocalTime.of(6,0), Duration.ofHours(5), event.getClubId(), event.getCoachId()))
                .isInstanceOf(EventTimeException.class)
                .hasMessageContaining("Event not within the club's opening hours");
    }


}
