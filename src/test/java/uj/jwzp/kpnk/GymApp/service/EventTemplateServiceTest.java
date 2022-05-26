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
import uj.jwzp.kpnk.GymApp.exception.event.EventTemplateDurationException;
import uj.jwzp.kpnk.GymApp.exception.event.EventTemplateNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.event.EventTemplateTimeException;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.Coach;
import uj.jwzp.kpnk.GymApp.model.EventTemplate;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;
import uj.jwzp.kpnk.GymApp.repository.CoachRepository;
import uj.jwzp.kpnk.GymApp.repository.EventTemplateRepository;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class EventTemplateServiceTest {


    private static EventTemplate eventTemplate;
    private static Club club;
    private static Coach coach;
    @Mock
    private EventTemplateRepository eventTemplateRepository;
    @Mock
    private CoachRepository coachRepository;
    @Mock
    private ClubRepository clubRepository;
    @InjectMocks
    private EventTemplateService eventTemplateService;

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
        eventTemplate = new EventTemplate(0, "testEvent", DayOfWeek.MONDAY, LocalTime.of(11,0), Duration.ofMinutes(30), 1, 1);
    }


    @Test
    public void getAllEventsWithOneClub() {
        given(eventTemplateRepository.findAll()).willReturn(List.of(eventTemplate));
        var events = eventTemplateService.allEventTemplates();

        assertThat(events).containsExactly(eventTemplate);
    }

    @Test
    public void getAllEventsEmpty() {
        given(eventTemplateRepository.findAll()).willReturn(List.of());

        Assertions.assertTrue(eventTemplateService.allEventTemplates().isEmpty());
    }

    @Test
    public void addValidEvent() {
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.of(club));
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.of(coach));
        given(eventTemplateService.addEventTemplate(eventTemplate.getTitle(), eventTemplate.getDay(), eventTemplate.getTime(), eventTemplate.getDuration(), eventTemplate.getClubId(), eventTemplate.getCoachId())).willReturn(eventTemplate);
        given(eventTemplateRepository.save(eventTemplate)).willReturn(eventTemplate);
        var serviceEvent = eventTemplateService.addEventTemplate(eventTemplate.getTitle(), eventTemplate.getDay(), eventTemplate.getTime(), eventTemplate.getDuration(), eventTemplate.getClubId(), eventTemplate.getCoachId());
        Assertions.assertEquals(serviceEvent, eventTemplate);
    }

    @Test
    public void addEventWithNonExistentCoach() {
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.of(club));
        given(coachRepository.findById(3)).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventTemplateService.addEventTemplate(eventTemplate.getTitle(), eventTemplate.getDay(), eventTemplate.getTime(), eventTemplate.getDuration(), eventTemplate.getClubId(), 3))
                .isInstanceOf(CoachNotFoundException.class)
                .hasMessageContaining("Unknown coach id");
    }

    @Test
    public void addEventWithNonExistentClub() {
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventTemplateService.addEventTemplate(eventTemplate.getTitle(), eventTemplate.getDay(), eventTemplate.getTime(), eventTemplate.getDuration(), eventTemplate.getClubId(), eventTemplate.getCoachId()))
                .isInstanceOf(ClubNotFoundException.class)
                .hasMessageContaining("Unknown club id");
    }

    @Test
    public void eventsByValidClub() {
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.of(club));
        given(eventTemplateRepository.findByClubId(eventTemplate.getClubId())).willReturn(List.of(eventTemplate));

        var events = eventTemplateService.eventTemplatesByClub(eventTemplate.getClubId());
        assertThat(events).containsExactly(eventTemplate);
    }

    @Test
    public void eventsByNonExistentClub() {
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventTemplateService.eventTemplatesByClub(eventTemplate.getClubId()))
                .isInstanceOf(ClubNotFoundException.class)
                .hasMessageContaining("Unknown club id: " + eventTemplate.getClubId());
    }

    @Test
    public void eventsByValidCoach() {
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.of(coach));
        given(eventTemplateRepository.findByCoachId(eventTemplate.getCoachId())).willReturn(List.of(eventTemplate));

        var events = eventTemplateService.eventTemplatesByCoach(eventTemplate.getCoachId());
        assertThat(events).containsExactly(eventTemplate);
    }

    @Test
    public void eventsByNonExistentCoach() {
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventTemplateService.eventTemplatesByCoach(eventTemplate.getCoachId()))
                .isInstanceOf(CoachNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown coach id: " + eventTemplate.getCoachId());
    }

    @Test
    public void modifyValidEvent() {
        given(eventTemplateRepository.findById(eventTemplate.getId())).willReturn(Optional.of(eventTemplate));
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.of(club));

        var uut = new EventTemplate(0, "modified", eventTemplate.getDay(), eventTemplate.getTime(), eventTemplate.getDuration(), eventTemplate.getClubId(), eventTemplate.getCoachId());
        given(eventTemplateRepository.save(uut)).willReturn(uut);

        var serviceEvent = eventTemplateService.modifyEventTemplate(0, "modified", eventTemplate.getDay(), eventTemplate.getTime(), eventTemplate.getDuration(), eventTemplate.getClubId(), eventTemplate.getCoachId());
        Assertions.assertEquals(serviceEvent, uut);
    }

    @Test
    public void modifyNonExistentEvent() {
        given(eventTemplateRepository.findById(eventTemplate.getId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                eventTemplateService.modifyEventTemplate(eventTemplate.getId(), eventTemplate.getTitle(), eventTemplate.getDay(), eventTemplate.getTime(), eventTemplate.getDuration(), eventTemplate.getClubId(), eventTemplate.getCoachId()))
                .isInstanceOf(EventTemplateNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown event id: " + eventTemplate.getId());
    }

    @Test
    public void modifyEventWithNonExistentClub() {
        given(eventTemplateRepository.findById(eventTemplate.getId())).willReturn(Optional.of(eventTemplate));
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                eventTemplateService.modifyEventTemplate(eventTemplate.getId(), eventTemplate.getTitle(), eventTemplate.getDay(), eventTemplate.getTime(), eventTemplate.getDuration(), eventTemplate.getClubId(), eventTemplate.getCoachId()))
                .isInstanceOf(ClubNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown club id: " + eventTemplate.getClubId());
    }

    @Test
    public void modifyEventWithNonExistentCoach() {
        given(eventTemplateRepository.findById(eventTemplate.getId())).willReturn(Optional.of(eventTemplate));
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                eventTemplateService.modifyEventTemplate(eventTemplate.getId(), eventTemplate.getTitle(), eventTemplate.getDay(), eventTemplate.getTime(), eventTemplate.getDuration(), eventTemplate.getClubId(), eventTemplate.getCoachId()))
                .isInstanceOf(CoachNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown coach id: " + eventTemplate.getClubId());
    }

    @Test
    public void removeNonExistentEvent() {
        given(eventTemplateRepository.findById(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> eventTemplateService.removeEventTemplate(1))
                .isInstanceOf(EventTemplateNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown event id: 1");
    }

    @Test
    public void addEventLongerThan24Hours() {
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.of(club));
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.of(coach));

        assertThatThrownBy(() -> eventTemplateService.addEventTemplate(eventTemplate.getTitle(), eventTemplate.getDay(), eventTemplate.getTime(), Duration.ofHours(30), eventTemplate.getClubId(), eventTemplate.getCoachId()))
                .isInstanceOf(EventTemplateDurationException.class)
                .hasFieldOrPropertyWithValue("message", "Event duration too long: " + eventTemplate.getTitle());
    }

    @Test
    public void modifyEventToBeLongerThan24Hours() {
        given(eventTemplateRepository.findById(eventTemplate.getId())).willReturn(Optional.of(eventTemplate));
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.of(club));
        var uut = new EventTemplate(0, "modified", eventTemplate.getDay(), eventTemplate.getTime(), eventTemplate.getDuration(), eventTemplate.getClubId(), eventTemplate.getCoachId());

        assertThatThrownBy(() -> eventTemplateService.modifyEventTemplate(0, "modified", eventTemplate.getDay(), eventTemplate.getTime(), Duration.ofHours(30), eventTemplate.getClubId(), eventTemplate.getCoachId()))
                .isInstanceOf(EventTemplateDurationException.class)
                .hasMessageContaining("Event duration too long");
    }

    @Test
    public void addEventBeforeClubOpeningHour() {
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.of(club));

        assertThatThrownBy(() -> eventTemplateService.addEventTemplate(eventTemplate.getTitle(), eventTemplate.getDay(), LocalTime.of(6,0), Duration.ofHours(1), eventTemplate.getClubId(), eventTemplate.getCoachId()))
                .isInstanceOf(EventTemplateTimeException.class)
                .hasMessageContaining("Event not within the club's opening hours");
    }

    @Test
    public void addEventAfterClubClosingHour() {
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.of(club));

        assertThatThrownBy(() -> eventTemplateService.addEventTemplate(eventTemplate.getTitle(), eventTemplate.getDay(), LocalTime.of(23,0), Duration.ofMinutes(30), eventTemplate.getClubId(), eventTemplate.getCoachId()))
                .isInstanceOf(EventTemplateTimeException.class)
                .hasMessageContaining("Event not within the club's opening hours");
    }

    @Test
    public void addEvent_ClubOpen24per7() {
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.of(club));
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.of(coach));
        var event = new EventTemplate(0, "testEvent1", DayOfWeek.WEDNESDAY, LocalTime.of(22,0), Duration.ofHours(4), EventTemplateServiceTest.eventTemplate.getClubId(), EventTemplateServiceTest.eventTemplate.getCoachId());
        given(eventTemplateRepository.save(event)).willReturn(event);
        var serviceEvent = eventTemplateService.addEventTemplate(event.getTitle(), event.getDay(), event.getTime(), event.getDuration(), event.getClubId(), event.getCoachId());
        Assertions.assertEquals(serviceEvent, event);
    }

    @Test
    public void addEvent_EventStartingDuringOpeningHours_AndEndingWhenClubIsClosed() {
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.of(club));

        assertThatThrownBy(() -> eventTemplateService.addEventTemplate(eventTemplate.getTitle(), DayOfWeek.MONDAY, LocalTime.of(20,0), Duration.ofHours(5), eventTemplate.getClubId(), eventTemplate.getCoachId()))
                .isInstanceOf(EventTemplateTimeException.class)
                .hasMessageContaining("Event not within the club's opening hours");
    }

    @Test
    public void addEvent_EventStartingBeforeOpeningHours_AndEndingWhenClubIsOpen() {
        given(coachRepository.findById(eventTemplate.getCoachId())).willReturn(Optional.of(coach));
        given(clubRepository.findById(eventTemplate.getClubId())).willReturn(Optional.of(club));

        assertThatThrownBy(() -> eventTemplateService.addEventTemplate(eventTemplate.getTitle(), DayOfWeek.MONDAY, LocalTime.of(6,0), Duration.ofHours(5), eventTemplate.getClubId(), eventTemplate.getCoachId()))
                .isInstanceOf(EventTemplateTimeException.class)
                .hasMessageContaining("Event not within the club's opening hours");
    }


}
