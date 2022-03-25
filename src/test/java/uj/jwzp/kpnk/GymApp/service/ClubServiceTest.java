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
import uj.jwzp.kpnk.GymApp.model.Event;
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
    private EventService eventService;
    @Mock
    private CoachRepository coachRepository;
    @InjectMocks
    private ClubService clubService;

    @BeforeAll
    static void setUp() {
        Map<DayOfWeek, OpeningHours> whenOpen = new HashMap<>();
        whenOpen.put(DayOfWeek.MONDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(22, 0)));
        whenOpen.put(DayOfWeek.TUESDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(22, 0)));
        whenOpen.put(DayOfWeek.WEDNESDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(0, 0)));
        whenOpen.put(DayOfWeek.THURSDAY, new OpeningHours(LocalTime.of(0, 0), LocalTime.of(0, 0)));
        whenOpen.put(DayOfWeek.FRIDAY, new OpeningHours(LocalTime.of(1, 0), LocalTime.of(22, 0)));
        club = new Club(1, "testClub1", "testAddress1", whenOpen);
    }

    @Test
    public void getAllClubsWithOneClub() {
        given(clubRepository.allClubs()).willReturn(Set.of(club));
        var clubs = clubService.allClubs();

        assertThat(clubs).containsExactly(club);
    }

    @Test
    public void getAllClubsEmpty() {
        given(clubRepository.allClubs()).willReturn(Collections.emptySet());

        Assertions.assertTrue(clubService.allClubs().isEmpty());
    }

    @Test
    public void addValidClub() {
        given(clubRepository.addClub(club.getName(), club.getAddress(), club.getWhenOpen())).willReturn(club);

        var serviceClub = clubService.addClub(club.getName(), club.getAddress(), club.getWhenOpen());
        Assertions.assertEquals(serviceClub, club);
    }

    @Test
    public void modifyValidClub() {
        given(clubRepository.club(1)).willReturn(Optional.of(club));

        var uut = new Club(1, "modified1", "modified2", club.getWhenOpen());
        given(clubRepository.modifyClub(1, uut)).willReturn(uut);

        var serviceClub = clubService.modifyClub(1, "modified1", "modified2", club.getWhenOpen());

        Assertions.assertEquals(serviceClub, uut);
    }

    @Test
    public void modifyNonExistentClub() {
        given(clubRepository.club(2)).willReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.modifyClub(2, "test", "test", club.getWhenOpen()))
                .isInstanceOf(ClubNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown club id: 2");
    }

    @Test
    public void removeNonExistentClub() {
        given(clubRepository.club(1)).willReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.removeClub(1))
                .isInstanceOf(ClubNotFoundException.class)
                .hasFieldOrPropertyWithValue("message", "Unknown club id: 1");
    }

    @Test
    public void removeClubWithEvents() {
        given(clubRepository.club(1)).willReturn(Optional.of(club));
        given(eventService.eventsByClub(1)).willReturn(Set.of(
                new Event(
                        1,
                        "test",
                        DayOfWeek.MONDAY,
                        LocalTime.NOON,
                        Duration.ofMinutes(5),
                        1,
                        1
                )));

        assertThatThrownBy(() -> clubService.removeClub(1))
                .isInstanceOf(ClubHasEventException.class)
                .hasFieldOrPropertyWithValue("message", "There are events in club: 1");
    }

    @Test
    public void modifyClubWithEventsStandingOut() {
        given(clubRepository.club(1)).willReturn(Optional.of(club));
        given(eventService.eventsByClub(1)).willReturn(Set.of(
                new Event(
                        1,
                        "testEvent1",
                        DayOfWeek.MONDAY,
                        LocalTime.of(8,0),
                        Duration.ofHours(2),
                        1,
                        1
                ),
                new Event(
                        1,
                        "testEvent2",
                        DayOfWeek.TUESDAY,
                        LocalTime.of(12,0),
                        Duration.ofHours(4),
                        1,
                        1
                )));

        Map<DayOfWeek, OpeningHours> newOpeningHours = new HashMap<>();
        newOpeningHours.put(DayOfWeek.MONDAY, new OpeningHours(LocalTime.of(10, 0), LocalTime.of(22, 0)));
        newOpeningHours.put(DayOfWeek.TUESDAY, new OpeningHours(LocalTime.of(13, 0), LocalTime.of(22, 0)));
        assertThatThrownBy(() -> clubService.modifyClub(club.getId(), club.getName(), club.getAddress(), newOpeningHours))
                .isInstanceOf(ClubOpeningHoursException.class)
                .hasFieldOrPropertyWithValue("message", "There are standing out events in club: " + club.getId());
    }
}
