package uj.jwzp.kpnk.GymApp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.jwzp.kpnk.GymApp.exception.club.ClubHasEventException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.exception.club.ClubOpeningHoursException;
import uj.jwzp.kpnk.GymApp.model.*;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;
import uj.jwzp.kpnk.GymApp.repository.CoachRepository;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class ClubServiceTest {

    private static Club club;
    private static Coach coach;
    private static Map<DayOfWeek, OpeningHours> whenOpen;
    private static Event event;

    @Mock
    private ClubRepository clubRepository;
    @Mock
    private EventService eventService;
    @Mock
    private EventTemplateService eventTemplateService;
    @Mock
    private CoachRepository coachRepository;
    @InjectMocks
    private ClubService clubService;

    @BeforeAll
    static void setUp() {
        whenOpen = new HashMap<>();
        whenOpen.put(DayOfWeek.MONDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(22, 0)));
        whenOpen.put(DayOfWeek.TUESDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(22, 0)));
        whenOpen.put(DayOfWeek.WEDNESDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(0, 0)));
        whenOpen.put(DayOfWeek.THURSDAY, new OpeningHours(LocalTime.of(0, 0), LocalTime.of(0, 0)));
        whenOpen.put(DayOfWeek.FRIDAY, new OpeningHours(LocalTime.of(1, 0), LocalTime.of(22, 0)));
        club = new Club(0, "testClub1", "testAddress1", whenOpen);
        coach = new Coach(1,"Kate", "Test", 2000);
        event = new Event(
                1,
                "testEvent1",
                DayOfWeek.MONDAY,
                LocalTime.of(8,0),
                Duration.ofHours(2),
                1,
                1,
                LocalDate.now(),
                8
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
        given(clubRepository.save(club)).willReturn(club);
        var newClub = clubService.addClub(club.getName(), club.getAddress(), club.getWhenOpen());
        Assertions.assertEquals(newClub, club);
    }

    @Test
    public void modifyValidClub() {
        given(clubRepository.findById(0)).willReturn(Optional.of(club));
        given(eventService.eventsByClub(0)).willReturn(List.of(event));
        var uut = new Club(0, "modified1", "modified2", club.getWhenOpen());
        given(clubRepository.save(uut)).willReturn(uut);
        given(eventService.eventsByClub(0)).willReturn(List.of());
        Mockito.doNothing().when(eventTemplateService).deleteEventTemplatesByClub(List.of());
        var serviceClub = clubService.modifyClub(0, "modified1", "modified2", club.getWhenOpen());

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
    public void deleteNonExistentClub() {
        given(clubRepository.findById(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.deleteClub(1))
                .isInstanceOf(ClubNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown club id: 1");
    }

    @Test
    public void deleteClubWithEvents() {
        given(clubRepository.findById(1)).willReturn(Optional.of(club));
        given(eventService.eventsByClub(1)).willReturn(List.of(new Event()));

        assertThatThrownBy(() -> clubService.deleteClub(1))
                .isInstanceOf(ClubHasEventException.class)
                .hasFieldOrPropertyWithValue("message", "There are events in club: 1");
    }

    @Test
    public void modifyClubWithEventsStandingOut() {
        given(clubRepository.findById(1)).willReturn(Optional.of(club));

        given(eventService.eventsByClub(1)).willReturn(List.of(event));

        Map<DayOfWeek, OpeningHours> newOpeningHours = new HashMap<>();
        newOpeningHours.put(DayOfWeek.MONDAY, new OpeningHours(LocalTime.of(10, 0), LocalTime.of(22, 0)));
        newOpeningHours.put(DayOfWeek.TUESDAY, new OpeningHours(LocalTime.of(13, 0), LocalTime.of(22, 0)));

        given(eventService.isEventBetweenOpeningHours(newOpeningHours, event.getDay(), event.getStartTime(), event.getDuration())).willReturn(false);

        assertThatThrownBy(() -> clubService.modifyClub(1, club.getName(), club.getAddress(), newOpeningHours))
                .isInstanceOf(ClubOpeningHoursException.class)
                .hasMessageContaining("standing out events");
    }

}
