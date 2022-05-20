package com.molkky.molkky.ihm.tournament;

import com.molkky.molkky.MolkkyApplication;
import com.molkky.molkky.SeleniumConfig;
import com.molkky.molkky.domain.*;
import com.molkky.molkky.repository.*;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import type.UserRole;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest(classes= MolkkyApplication.class, webEnvironment=SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TournamentViewTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoundRepository roundRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private CourtRepository courtRepository;
    @Autowired
    private UserTournamentRoleRepository userTournamentRoleRepository;
    @Autowired
    private MatchRepository matchRepository;
    @Autowired
    private SetRepository setRepository;
    private SeleniumConfig config;

    @Value("${server.port}")
    private Integer port;
    private String url;
    private String emailStaff = "connexion.admin@gmail.com";
    private String passwordStaff = "admin123";
    private String name = "tournament test pool service";

    @BeforeAll
    void setUp() {
        config = new SeleniumConfig();
        url = String.format("http://localhost:%s", port.toString());
        config.getDriver().get(url + "/connexion");
        config.getDriver().findElement(new By.ById("email")).sendKeys(emailStaff);
        config.getDriver().findElement(new By.ById("password")).sendKeys(passwordStaff);
        config.getDriver().findElement(new By.ById("connexion")).click();
        config.getDriver().findElement(new By.ById("buttonTournament")).click();
        config.getDriver().findElement(new By.ById("buttonRole")).click();
        config.getDriver().findElement(new By.ById("tournament_sidebar")).click();
        config.getDriver().findElement(new By.ByXPath("//span[contains(text(),'"+name+"')]")).click();
    }

    @Test
    void testTournamentInfo() {
        Tournament tournament = tournamentRepository.findByName(name);
        // Informations Generales Part
        String info_generales = "Le tournoi tournament test pool service du "+tournament.getDate()+" aura lieu à "+tournament.getLocation()+" débutera le "+tournament.getDate()+".";
        String teams = "Le nombre d'équipes pour ce tournoi est compris entre "+tournament.getMinTeam()+" et "+tournament.getMaxTeam()+".";
        //String nbTeams = "Il y a actuellement "+tournament.getTeams().size()+" équipes inscrites à ce tournoi.";
        String currentPhase = "La phase courante est la phase n°"+Integer.toString(tournament.getIndexPhase())+".";
        Assertions.assertEquals(tournament.getName(), config.getDriver().findElement(new By.ById("tournament_name")).getText());
        Assertions.assertEquals("("+tournament.getStatus()+")", config.getDriver().findElement(new By.ById("tournament_status")).getText());
        Assertions.assertEquals(info_generales, config.getDriver().findElement(new By.ById("date-and-location")).getText());
        Assertions.assertEquals(teams, config.getDriver().findElement(new By.ById("teams-extremums")).getText());
        //Assertions.assertEquals(nbTeams, config.getDriver().findElement(new By.ById("nbTeam")).getText());
        Assertions.assertEquals(currentPhase, config.getDriver().findElement(new By.ById("current-phase")).getText());

        // Format du tournoi Part
        

        // Participants Part
        List<WebElement> registeredTeams = config.getDriver().findElements(new By.ByClassName("teamCard"));
        for(int i=0; i<registeredTeams.size(); i++) {
            Assertions.assertTrue(registeredTeams.get(i).findElement(new By.ByClassName("teamName")).isDisplayed());
            Assertions.assertTrue(registeredTeams.get(i).findElement(new By.ByClassName("cardContentContainer")).isDisplayed());
            Assertions.assertTrue(registeredTeams.get(i).findElement(new By.ByClassName("teamLogo")).isDisplayed());
        }
        List<WebElement> players = config.getDriver().findElements(new By.ByClassName("roundsList"));
        for(int i=0; i<players.size(); i++) {
            Assertions.assertTrue(players.get(i).findElement(new By.ByClassName("teamMember")).isDisplayed());
        }


        // Ajouter Staff Part
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("staff-counter")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("addStaff")).isDisplayed());
    }
}