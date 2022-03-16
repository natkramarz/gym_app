package uj.jwzp.kpnk.GymApp.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uj.jwzp.kpnk.GymApp.exception.club.ClubNotFoundException;
import uj.jwzp.kpnk.GymApp.model.Club;
import uj.jwzp.kpnk.GymApp.model.OpeningHours;
import uj.jwzp.kpnk.GymApp.repository.ClubRepository;

import java.time.DayOfWeek;
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
    @InjectMocks
    private ClubService clubService;

    @BeforeAll
    static void setUp() {
        Map<DayOfWeek, OpeningHours> whenOpen = new HashMap<>();
        whenOpen.put(DayOfWeek.MONDAY, new OpeningHours(LocalTime.of(7, 0), LocalTime.of(22, 0)));

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
        given(clubRepository.addClub(club.name(), club.address(), club.whenOpen())).willReturn(club);

        var serviceClub = clubService.addClub(club.name(), club.address(), club.whenOpen());
        Assertions.assertEquals(serviceClub, club);
    }

    @Test
    public void modifyValidClub() {
        given(clubRepository.club(1)).willReturn(Optional.of(club));

        var uut = new Club(1, "modified1", "modified2", club.whenOpen());
        given(clubRepository.modifyClub(1, uut)).willReturn(uut);

        var serviceClub = clubService.modifyClub(1, "modified1", "modified2", club.whenOpen());

        Assertions.assertEquals(serviceClub, uut);
    }

    @Test
    public void modifyNonExistentClub() {
        given(clubRepository.club(2)).willReturn(Optional.empty());

        assertThatThrownBy(() -> clubService.modifyClub(2, "test", "test", club.whenOpen()))
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
}
