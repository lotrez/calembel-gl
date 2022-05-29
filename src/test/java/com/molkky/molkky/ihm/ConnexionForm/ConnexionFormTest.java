package com.molkky.molkky.ihm.ConnexionForm;

import com.molkky.molkky.MolkkyApplication;
import com.molkky.molkky.SeleniumConfig;
import com.molkky.molkky.domain.Team;
import com.molkky.molkky.domain.Tournament;
import com.molkky.molkky.domain.User;
import com.molkky.molkky.domain.UserTournamentRole;
import com.molkky.molkky.repository.TeamRepository;
import com.molkky.molkky.repository.TournamentRepository;
import com.molkky.molkky.repository.UserRepository;
import com.molkky.molkky.repository.UserTournamentRoleRepository;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import type.UserRole;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;

@SpringBootTest(classes = MolkkyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class ConnexionFormTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private UserTournamentRoleRepository userTournamentRoleRepository;
    private SeleniumConfig config;
    @Value("${server.port}")
    private Integer port;
    private String url;
    private final String emailPlayer = randomAlphabetic(10)+"@"+randomAlphabetic(10)+".com";
    private final String passwordPlayer = randomAlphabetic(10);
    private final String teamName = randomAlphabetic(10);
    private final String teamCode = randomAlphabetic(10);
    private final String tournamentName = randomAlphabetic(10);
    private final String tournamentName2 = randomAlphabetic(10);
    private final String emailAdmin = randomAlphabetic(10)+"@"+randomAlphabetic(10)+".com";
    private final String passwordAdmin = randomAlphabetic(10);
    private final String emailStaff = randomAlphabetic(10)+"@"+randomAlphabetic(10)+".com";
    private final String passwordStaff = randomAlphabetic(15);


    @BeforeAll
    void setUp() {
        config = new SeleniumConfig();
        url = String.format("http://localhost:%s", port.toString());
        User player = new User();
        player.setEmail(emailPlayer);
        player.setPassword(passwordPlayer);
        userRepository.save(player);
        Team team = new Team();
        team.setName(teamName);
        team.setCode(teamCode);
        teamRepository.save(team);
        Tournament tournament = new Tournament();
        tournament.setName(tournamentName);
        tournament.setVisible(true);
        tournamentRepository.save(tournament);
        Tournament tournament2 = new Tournament();
        tournament2.setName(tournamentName2);
        tournament2.setVisible(true);
        tournamentRepository.save(tournament2);
        UserTournamentRole userTournamentRolePlayer = new UserTournamentRole();
        userTournamentRolePlayer.setRole(UserRole.PLAYER);
        userTournamentRolePlayer.setUser(player);
        userTournamentRolePlayer.setTournament(tournament);
        userTournamentRolePlayer.setTeam(team);
        userTournamentRoleRepository.save(userTournamentRolePlayer);
        User admin = new User();
        admin.setEmail(emailAdmin);
        admin.setPassword(passwordAdmin);
        userRepository.save(admin);
        UserTournamentRole userTournamentRoleAdmin = new UserTournamentRole();
        userTournamentRoleAdmin.setRole(UserRole.ADM);
        userTournamentRoleAdmin.setUser(admin);
        userTournamentRoleAdmin.setTournament(tournament);
        userTournamentRoleRepository.save(userTournamentRoleAdmin);
        UserTournamentRole userTournamentRoleAdmin2 = new UserTournamentRole();
        userTournamentRoleAdmin2.setRole(UserRole.ADM);
        userTournamentRoleAdmin2.setUser(admin);
        userTournamentRoleAdmin2.setTournament(tournament2);
        userTournamentRoleRepository.save(userTournamentRoleAdmin2);
        User staff = new User();
        staff.setEmail(emailStaff);
        staff.setPassword(passwordStaff);
        userRepository.save(staff);
        UserTournamentRole userTournamentRoleStaff = new UserTournamentRole();
        userTournamentRoleStaff.setRole(UserRole.STAFF);
        userTournamentRoleStaff.setUser(staff);
        userTournamentRoleStaff.setTournament(tournament);
        userTournamentRoleRepository.save(userTournamentRoleStaff);
        UserTournamentRole userTournamentRoleStaffAdmin = new UserTournamentRole();
        userTournamentRoleStaffAdmin.setRole(UserRole.ADM);
        userTournamentRoleStaffAdmin.setUser(staff);
        userTournamentRoleStaffAdmin.setTournament(tournament);
        userTournamentRoleRepository.save(userTournamentRoleStaffAdmin);
    }



    @Test
    void testConnexionFormIsDisplayed(){
        config.getDriver().get(url + "/connexion");
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("email")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("password")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("teamCode")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("connexion")).isDisplayed());
    }

    @Test
    void testCheckConnexionExistingPlayer(){
        config.getDriver().get(url + "/connexion");
        config.getDriver().findElement(new By.ById("email")).sendKeys(emailPlayer);
        config.getDriver().findElement(new By.ById("password")).sendKeys(passwordPlayer);
        config.getDriver().findElement(new By.ById("teamCode")).sendKeys(teamCode);
        config.getDriver().findElement(new By.ById("connexion")).click();
        Assertions.assertEquals("Accueil", config.getDriver().getTitle());
        config.getDriver().findElement(new By.ById("infos_sidebar")).click();
        Assertions.assertEquals(emailPlayer, config.getDriver().findElement(new By.ById("email")).getText());
        Assertions.assertEquals(teamName, config.getDriver().findElement(new By.ById("team")).getText());
        Assertions.assertEquals("PLAYER", config.getDriver().findElement(new By.ById("role")).getText());
        Assertions.assertEquals(tournamentName, config.getDriver().findElement(new By.ById("tournament")).getText());

    }

    @Test
    void testCheckConnexionWrongPasswordUser(){
        config.getDriver().get(url + "/connexion");
        config.getDriver().findElement(new By.ById("email")).sendKeys(emailPlayer);
        config.getDriver().findElement(new By.ById("password")).sendKeys("wgPwd");
        config.getDriver().findElement(new By.ById("teamCode")).sendKeys(teamCode);
        config.getDriver().findElement(new By.ById("connexion")).click();
        Assertions.assertEquals("Page de connexion", config.getDriver().getTitle());
    }
    @Test
    void testConnexionExistingAdmin2Tournaments(){
        config.getDriver().get(url + "/connexion");
        WebDriverWait wait = new WebDriverWait(config.getDriver(), 30);
        config.getDriver().findElement(new By.ById("email")).sendKeys(emailAdmin);
        config.getDriver().findElement(new By.ById("password")).sendKeys(passwordAdmin);
        config.getDriver().findElement(new By.ById("connexion")).click();
        wait.until(ExpectedConditions.visibilityOf(config.getDriver().findElement(new By.ById("tournamentChoice"))));
        Assertions.assertEquals("Choix du Role/Tournoi", config.getDriver().getTitle());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("buttonTournament")).isDisplayed());
        config.getDriver().findElement(new By.ById("buttonTournament")).click();
        wait.until(ExpectedConditions.visibilityOf(config.getDriver().findElement(new By.ById("rolesChoice"))));
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("buttonRole")).isDisplayed());
        config.getDriver().findElement(new By.ById("buttonRole")).click();
        wait.until(ExpectedConditions.visibilityOf(config.getDriver().findElement(new By.ById("homeDescription"))));
        Assertions.assertEquals("Accueil", config.getDriver().getTitle());
        config.getDriver().findElement(new By.ById("infos_sidebar")).click();
        Assertions.assertEquals(emailAdmin, config.getDriver().findElement(new By.ById("email")).getText());
        Assertions.assertEquals("ADM", config.getDriver().findElement(new By.ById("role")).getText());
        Assertions.assertEquals(tournamentName, config.getDriver().findElement(new By.ById("tournament")).getText());
    }

    @Test
    void testCheckConnexionWrongTeamCode(){
        config.getDriver().get(url + "/connexion");
        config.getDriver().findElement(new By.ById("email")).sendKeys(emailPlayer);
        config.getDriver().findElement(new By.ById("password")).sendKeys(passwordPlayer);
        config.getDriver().findElement(new By.ById("teamCode")).sendKeys(randomAlphabetic(10));
        config.getDriver().findElement(new By.ById("connexion")).click();
        Assertions.assertEquals("Page de connexion", config.getDriver().getTitle());
    }

    @Test
    void testCheckUserWithTwoRoles(){
        config.getDriver().get(url + "/connexion");
        config.getDriver().findElement(new By.ById("email")).sendKeys(emailStaff);
        config.getDriver().findElement(new By.ById("password")).sendKeys(passwordStaff);
        config.getDriver().findElement(new By.ById("connexion")).click();
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("buttonTournament")).isDisplayed());
        config.getDriver().findElement(new By.ById("buttonTournament")).click();
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("buttonRole")).isDisplayed());
        String role = config.getDriver().findElement(new By.ById("buttonRole")).getText();
        config.getDriver().findElement(new By.ById("buttonRole")).click();
        Assertions.assertEquals("Accueil", config.getDriver().getTitle());
        config.getDriver().findElement(new By.ById("infos_sidebar")).click();
        Assertions.assertEquals(emailStaff, config.getDriver().findElement(new By.ById("email")).getText());
        Assertions.assertEquals(role, config.getDriver().findElement(new By.ById("role")).getText());
        Assertions.assertEquals(tournamentName, config.getDriver().findElement(new By.ById("tournament")).getText());
    }

    @Test
    void testDestroySession(){
        config.getDriver().get(url + "/connexion");
        config.getDriver().findElement(new By.ById("email")).sendKeys(emailPlayer);
        config.getDriver().findElement(new By.ById("password")).sendKeys(passwordPlayer);
        config.getDriver().findElement(new By.ById("teamCode")).sendKeys(teamCode);
        config.getDriver().findElement(new By.ById("connexion")).click();
        Assertions.assertEquals("Accueil", config.getDriver().getTitle());
        String connected = config.getDriver().findElement(new By.ById("notLogged")).getText();
        Assertions.assertEquals("Se déconnecter", connected);
        config.getDriver().findElement(new By.ById("notLogged")).click();
        Assertions.assertEquals("Page de connexion", config.getDriver().getTitle());
        config.getDriver().findElement(new By.ByClassName("sidebarLogo")).click();
        Assertions.assertEquals("Accueil", config.getDriver().getTitle());
        String disconnected = config.getDriver().findElement(new By.ById("logged")).getText();
        Assertions.assertEquals("Se connecter", disconnected);
    }


    @AfterAll
    void tearDown() {
        config.getDriver().quit();
    }
}

