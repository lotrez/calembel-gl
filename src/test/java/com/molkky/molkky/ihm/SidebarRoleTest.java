package com.molkky.molkky.ihm;

import com.molkky.molkky.MolkkyApplication;
import com.molkky.molkky.SeleniumConfig;
import com.molkky.molkky.domain.Team;
import com.molkky.molkky.domain.Tournament;
import com.molkky.molkky.domain.User;
import com.molkky.molkky.domain.UserTounamentRole;
import com.molkky.molkky.repository.TeamRepository;
import com.molkky.molkky.repository.TournamentRepository;
import com.molkky.molkky.repository.UserRepository;
import com.molkky.molkky.repository.UserTounamentRoleRepository;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import type.UserRole;

@SpringBootTest(classes = MolkkyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class SidebarRoleTest {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private TournamentRepository tournamentRepository;
    @Autowired
    private UserTounamentRoleRepository userTournamentRoleRepository;
    private SeleniumConfig config;
    @Value("${server.port}")
    private Integer port;
    private String url;
    final private String emailPlayer = "connexion.player@gmail.com";
    final private String passwordPlayer = "test123";
    final private String teamName = "TeamConnexionPlayer";
    final private String teamCode = "TeamCode";
    final private String tournamentName = "TournamentConnexion";
    final private String tournamentName2 = "TournamentConnexion2";
    final private String emailAdmin = "connexion.admin@gmail.com";
    final private String passwordAdmin = "admin123";
    final private String emailStaff = "connexion.staff@gmail.com";
    final private String passwordStaff = "staff123";

    @BeforeAll
    void setUp() {
        config = new SeleniumConfig();
        url = String.format("http://localhost:%s", port.toString());
        if(userRepository.findUserByEmail(emailPlayer)==null) {
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
            UserTounamentRole userTounamentRolePlayer = new UserTounamentRole();
            userTounamentRolePlayer.setRole(UserRole.PLAYER);
            userTounamentRolePlayer.setUser(player);
            userTounamentRolePlayer.setTournament(tournament);
            userTounamentRolePlayer.setTeam(team);
            userTournamentRoleRepository.save(userTounamentRolePlayer);
            User admin = new User();
            admin.setEmail(emailAdmin);
            admin.setPassword(passwordAdmin);
            userRepository.save(admin);
            UserTounamentRole userTounamentRoleAdmin = new UserTounamentRole();
            userTounamentRoleAdmin.setRole(UserRole.ADM);
            userTounamentRoleAdmin.setUser(admin);
            userTounamentRoleAdmin.setTournament(tournament);
            userTournamentRoleRepository.save(userTounamentRoleAdmin);
            UserTounamentRole userTournamentRoleAdmin2 = new UserTounamentRole();
            userTournamentRoleAdmin2.setRole(UserRole.ADM);
            userTournamentRoleAdmin2.setUser(admin);
            userTournamentRoleAdmin2.setTournament(tournament2);
            userTournamentRoleRepository.save(userTournamentRoleAdmin2);
            User staff = new User();
            staff.setEmail(emailStaff);
            staff.setPassword(passwordStaff);
            userRepository.save(staff);
            UserTounamentRole userTournamentRoleStaff = new UserTounamentRole();
            userTournamentRoleStaff.setRole(UserRole.STAFF);
            userTournamentRoleStaff.setUser(staff);
            userTournamentRoleStaff.setTournament(tournament);
            userTournamentRoleRepository.save(userTournamentRoleStaff);

        }
    }

    @Test
    void testCheckSidebarVisitor(){
        config.getDriver().get(url);
        WebDriverWait wait = new WebDriverWait(config.getDriver(), 30);
        wait.until(ExpectedConditions.visibilityOf(config.getDriver().findElement(new By.ById("homeDescription"))));
        Assertions.assertEquals("Accueil", config.getDriver().getTitle());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("home_v")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("tournamentongoing_v")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("tournament_v")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("teams_v")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("users_v")).isDisplayed());
    }

    @Test
    void testCheckSidebarPlayer(){
        config.getDriver().get(url + "/connexion");
        WebDriverWait wait = new WebDriverWait(config.getDriver(), 30);
        config.getDriver().findElement(new By.ById("email")).sendKeys(emailPlayer);
        config.getDriver().findElement(new By.ById("password")).sendKeys(passwordPlayer);
        config.getDriver().findElement(new By.ById("teamCode")).sendKeys(teamCode);
        config.getDriver().findElement(new By.ById("connexion")).click();
        wait.until(ExpectedConditions.visibilityOf(config.getDriver().findElement(new By.ById("homeDescription"))));
        Assertions.assertEquals("Accueil", config.getDriver().getTitle());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("home_p")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("tournamentongoing_p")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("mymatches_p")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("tournament_p")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("teams_p")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("users_p")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("infos_p")).isDisplayed());

    }

    @Test
    void testCheckSidebarStaff(){
        config.getDriver().get(url + "/connexion");
        WebDriverWait wait = new WebDriverWait(config.getDriver(), 30);
        config.getDriver().findElement(new By.ById("email")).sendKeys(emailStaff);
        config.getDriver().findElement(new By.ById("password")).sendKeys(passwordStaff);
        config.getDriver().findElement(new By.ById("connexion")).click();
        wait.until(ExpectedConditions.visibilityOf(config.getDriver().findElement(new By.ById("homeDescription"))));
        Assertions.assertEquals("Accueil", config.getDriver().getTitle());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("home_s")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("tournamentongoing_s")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("mymatches_s")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("tournament_s")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("teams_s")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("users_s")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("infos_s")).isDisplayed());
    }

    @Test
    void testCheckSidebarAdmin(){
        config.getDriver().get(url + "/connexion");
        WebDriverWait wait = new WebDriverWait(config.getDriver(), 30);
        config.getDriver().findElement(new By.ById("email")).sendKeys(emailAdmin);
        config.getDriver().findElement(new By.ById("password")).sendKeys(passwordAdmin);
        config.getDriver().findElement(new By.ById("connexion")).click();
        wait.until(ExpectedConditions.visibilityOf(config.getDriver().findElement(new By.ById("tournamentChoice"))));
        config.getDriver().findElement(new By.ById("buttonTournament")).click();
        wait.until(ExpectedConditions.visibilityOf(config.getDriver().findElement(new By.ById("rolesChoice"))));
        config.getDriver().findElement(new By.ById("buttonRole")).click();
        wait.until(ExpectedConditions.visibilityOf(config.getDriver().findElement(new By.ById("homeDescription"))));
        Assertions.assertEquals("Accueil", config.getDriver().getTitle());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("home_a")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("tournamentongoing_a")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("tournament_a")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("teams_a")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("users_a")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ById("infos_a")).isDisplayed());
    }

    @AfterAll
    void tearDown() {
        config.getDriver().quit();
    }
}
