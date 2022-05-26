package uj.jwzp.kpnk.GymApp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.jwzp.kpnk.GymApp.exception.club.ClubHasEventException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubOpeningHoursException;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.EventTemplate;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;
import uj.jwzp.kpnk.GymApp.repository.CoachRepository;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ClubServiceTest {

    private static Club club;
    @Mock
    private ClubRepository clubRepository;
    @Mock
    private EventTemplateService eventTemplateService;
    @Mock
    private CoachRepository coachRepository;
    @InjectMocks
    private ClubService clubService;
    private static Map<DayOfWeek, OpeningHours> whenOpen;
    private static EventTemplate eventTemplate;

    @BeforeAll
    static void setUp() {
        whenOpen = new HashMap<>();
        whenOpen.put(DayOfWeek.MONDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(22, 0)));
        whenOpen.put(DayOfWeek.TUESDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(22, 0)));
        whenOpen.put(DayOfWeek.WEDNESDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(0, 0)));
        whenOpen.put(DayOfWeek.THURSDAY, new OpeningHours(LocalTime.of(0, 0), LocalTime.of(0, 0)));
        whenOpen.put(DayOfWeek.FRIDAY, new OpeningHours(LocalTime.of(1, 0), LocalTime.of(22, 0)));
        club = new Club(1, "testClub1", "testAddress1", whenOpen);
        eventTemplate = new EventTemplate(
                1,
                "testEvent1",
                DayOfWeek.MONDAY,
                LocalTime.of(8,0),
                Duration.ofHours(2),
                1,
                1
        );
    }

    @Test
    public void getAllClubsWithOneClub() {
        given(clubRepository.findAll()).willReturn(List.of(club));
        var clubs = clubService.allClubs();

        assertThat(clubs).containsExactly(club);
    }

    @Test
    public void getAllClubsEmpty() {
        Assertions.assertTrue(clubService.allClubs().isEmpty());
    }

    @Test
    public void addValidClub() {
        var clubWithIdZero = new Club(0, "testClub", "testAddress", whenOpen);
        given(clubRepository.save(clubWithIdZero)).willReturn(clubWithIdZero);
        var serviceClub = clubService.addClub(clubWithIdZero.getName(), clubWithIdZero.getAddress(), clubWithIdZero.getWhenOpen());
        Assertions.assertEquals(serviceClub, clubWithIdZero);
    }

    @Test
    public void modifyValidClub() {
        given(clubRepository.findById(1)).willReturn(Optional.of(club));
        given(eventTemplateService.eventTemplatesByClub(1)).willReturn(List.of(eventTemplate));
        given(eventTemplateService.isEventTemplateBetweenOpeningHours(whenOpen, eventTemplate.getDay(), eventTemplate.getTime(), eventTemplate.getDuration())).willReturn(true);
        var uut = new Club(1, "modified1", "modified2", club.getWhenOpen());
        given(clubRepository.save(uut)).willReturn(uut);

        var serviceClub = clubService.modifyClub(1, "modified1", "modified2", club.getWhenOpen());

        Assertions.assertEquals(serviceClub, uut);
    }

    @Test
    public void modifyNonExistentClub() {
        given(clubRepository.findById(2)).willReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.modifyClub(2, "test", "test", club.getWhenOpen()))
                .isInstanceOf(ClubNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown club id: 2");
    }

    @Test
    public void removeNonExistentClub() {
        given(clubRepository.findById(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.removeClub(1))
                .isInstanceOf(ClubNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown club id: 1");
    }

    @Test
    public void removeClubWithEvents() {
        given(clubRepository.findById(1)).willReturn(Optional.of(club));
        given(eventTemplateService.eventTemplatesByClub(1)).willReturn(List.of(new EventTemplate()));

        assertThatThrownBy(() -> clubService.removeClub(1))
                .isInstanceOf(ClubHasEventException.class)
                .hasFieldOrPropertyWithValue("message", "There are events in club: 1");
    }

    @Test
    public void modifyClubWithEventsStandingOut() {
        given(clubRepository.findById(1)).willReturn(Optional.of(club));

        given(eventTemplateService.eventTemplatesByClub(1)).willReturn(List.of(eventTemplate));

        Map<DayOfWeek, OpeningHours> newOpeningHours = new HashMap<>();
        newOpeningHours.put(DayOfWeek.MONDAY, new OpeningHours(LocalTime.of(10, 0), LocalTime.of(22, 0)));
        newOpeningHours.put(DayOfWeek.TUESDAY, new OpeningHours(LocalTime.of(13, 0), LocalTime.of(22, 0)));

        given(eventTemplateService.isEventTemplateBetweenOpeningHours(newOpeningHours, eventTemplate.getDay(), eventTemplate.getTime(), eventTemplate.getDuration())).willReturn(false);

        assertThatThrownBy(() -> clubService.modifyClub(club.getId(), club.getName(), club.getAddress(), newOpeningHours))
                .isInstanceOf(ClubOpeningHoursException.class)
                .hasMessageContaining("standing out events");
    }

}
