package com.molkky.molkky.ihm.TeamForm;

import com.molkky.molkky.MolkkyApplication;
import com.molkky.molkky.SeleniumConfig;
import com.molkky.molkky.domain.Team;
import com.molkky.molkky.domain.Tournament;
import com.molkky.molkky.domain.User;
import com.molkky.molkky.domain.UserTournamentRole;
import com.molkky.molkky.model.TournamentModel;
import com.molkky.molkky.repository.TeamRepository;
import com.molkky.molkky.repository.TournamentRepository;
import com.molkky.molkky.repository.UserRepository;
import com.molkky.molkky.repository.UserTournamentRoleRepository;
import org.junit.jupiter.api.*;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import type.UserRole;

import java.text.ParseException;
import java.util.Random;

@SpringBootTest(classes = MolkkyApplication.class, webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PlayerFormTest {
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

    @BeforeAll
    void setUp() throws ParseException {
        config = new SeleniumConfig();
        url = String.format("http://localhost:%s", port.toString());
        this.createTournament();
    }

    void createTournament() throws ParseException {
        TournamentModel tournament = new TournamentModel();
        tournament.setName("Tournoi " + Math.floor(Math.random() * 1000));
        tournament.setLocation("location de test");
        tournament.setDate("2019-01-01");
        tournament.setCutOffDate("2019-01-01");
        tournament.setMinTeam(5);
        tournament.setMaxTeam(20);
        tournament.setNbRounds(1);
        tournament.setNbCourts(1);
        tournament.setNbPlayersPerTeam(2);
        tournament.setVisible(true);

        Tournament tournament1 = new Tournament(tournament);
        tournamentRepository.save(tournament1);
    }

    @BeforeEach
    void enterTeam(){
        config.getDriver().get(url + "/team/create");
        String teamName = "Test" + Math.floor(Math.random() * 1000);
        config.getDriver().findElement(new By.ById("nom")).sendKeys(teamName);
        Select select = new Select(config.getDriver().findElement(new By.ById("tournament")));
        select.selectByIndex(select.getOptions().size() - 1);

        config.getDriver().findElement(new By.ById("newClub")).click();

        config.getDriver().findElement(new By.ById("createClubInput")).sendKeys("new club");

        config.getDriver().findElement(new By.ById("sendTeam")).click();
    }

    @Test
    void testPlayerFormGetPage(){
        Assertions.assertEquals("Create Team", config.getDriver().getTitle());
    }

    @Test
    void testFormIsDisplayed(){
        Assertions.assertTrue(config.getDriver().findElement(new By.ByClassName("contentTitle")).isDisplayed());
        Assertions.assertEquals("Ajouter un joueur ?? l'??quipe",config.getDriver().findElement
                (new By.ByClassName("contentTitle")).getText());
        Assertions.assertTrue(config.getDriver().findElement
                (new By.ByXPath("/html/body/div/div[2]/form/div[1]")).isDisplayed());

        //Joueur 1
        Assertions.assertTrue(config.getDriver().findElement
                (new By.ByXPath("/html/body/div/div[2]/form/div[2]/div/div[1]")).isDisplayed());
        Assertions.assertEquals("Joueur 1",config.getDriver().findElement
                (new By.ByXPath("/html/body/div/div[2]/form/div[2]/div/div[1]")).getText());
        Assertions.assertTrue(config.getDriver().findElement(new By.ByName("players[0].forename")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ByName("players[0].surname")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ByName("players[0].mail")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ByName("players[0].pseudo")).isDisplayed());

        Assertions.assertEquals("Pr??nom",config.getDriver().findElement
                (new By.ByCssSelector("body > div > div.contentContainer > form > div.formContainer " +
                        "> div:nth-child(1) > div > div:nth-child(1) > label")).getText());
        Assertions.assertEquals("Nom de famille",config.getDriver().findElement
                (new By.ByCssSelector("body > div > div.contentContainer > form > div.formContainer " +
                        "> div:nth-child(1) > div > div:nth-child(2) > label")).getText());
        Assertions.assertEquals("Mail",config.getDriver().findElement
                (new By.ByCssSelector("body > div > div.contentContainer > form > div.formContainer " +
                        "> div:nth-child(1) > div > div:nth-child(3) > label")).getText());
        Assertions.assertEquals("Pseudo",config.getDriver().findElement
                (new By.ByCssSelector("body > div > div.contentContainer > form > div.formContainer " +
                        "> div:nth-child(1) > div > div:nth-child(4) > label")).getText());


        Assertions.assertTrue(config.getDriver().findElement(new By.ByName("players[1].forename")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ByName("players[1].surname")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ByName("players[1].mail")).isDisplayed());
        Assertions.assertTrue(config.getDriver().findElement(new By.ByName("players[1].pseudo")).isDisplayed());

        Assertions.assertTrue(config.getDriver().findElement(new By.ById("sendTeam")).isDisplayed());
        Assertions.assertEquals("Finaliser",config.getDriver().findElement(new By.ById("sendTeam")).getText());

    }

    @Test
    void testFormAddInfo() {
        String nom = this.generateName();
        String prenom = this.generateName();
        String mail = prenom+"."+nom+"@gmail.com";

        String nom2 = this.generateName();
        String prenom2 = this.generateName();
        String mail2 = prenom2+"."+nom2+"@gmail.com";

        String tagTeam = config.getDriver().findElement(new By.ByXPath("/html/body/div/div[2]/form/div[1]")).getText();
        String[] teamName = tagTeam.split(" ");

        config.getDriver().findElement(new By.ByName("players[0].forename")).sendKeys(prenom);
        config.getDriver().findElement(new By.ByName("players[0].surname")).sendKeys(nom);
        config.getDriver().findElement(new By.ByName("players[0].mail")).sendKeys(mail);
        config.getDriver().findElement(new By.ByName("players[0].pseudo")).sendKeys("test1");

        config.getDriver().findElement(new By.ByName("players[1].forename")).sendKeys(prenom2);
        config.getDriver().findElement(new By.ByName("players[1].surname")).sendKeys(nom2);
        config.getDriver().findElement(new By.ByName("players[1].mail")).sendKeys(mail2);
        config.getDriver().findElement(new By.ByName("players[1].pseudo")).sendKeys("test2");

        config.getDriver().findElement(new By.ById("sendTeam")).click();

        User user = userRepository.findUserByEmail(mail);
        User user2 = userRepository.findUserByEmail(mail2);

        Team team = teamRepository.findByName(teamName[1]);
        UserTournamentRole userTournamentRole1 = userTournamentRoleRepository.findByUserAndTeam(user,team);
        UserTournamentRole userTournamentRole2 = userTournamentRoleRepository.findByUserAndTeam(user2,team);

        Assertions.assertNotNull(user);
        Assertions.assertEquals(nom,user.getSurname());
        Assertions.assertEquals(prenom,user.getForename());
        Assertions.assertEquals(mail,user.getEmail());
        Assertions.assertEquals("test1",user.getPseudo());
        Assertions.assertNotNull(userTournamentRole1);
        Assertions.assertEquals(UserRole.PLAYER,userTournamentRole1.getRole());
        Assertions.assertEquals(team.getTournament().getId(),userTournamentRole1.getTournament().getId());

        Assertions.assertNotNull(user);
        Assertions.assertEquals(nom2,user2.getSurname());
        Assertions.assertEquals(prenom2,user2.getForename());
        Assertions.assertEquals(mail2,user2.getEmail());
        Assertions.assertEquals("test2",user2.getPseudo());
        Assertions.assertNotNull(userTournamentRole2);
        Assertions.assertEquals(UserRole.PLAYER,userTournamentRole2.getRole());
        Assertions.assertEquals(team.getTournament().getId(),userTournamentRole2.getTournament().getId());
    }

    @Test
    void testFormErrorSameMail(){
        String nom = this.generateName();
        String prenom = this.generateName();
        String mail = prenom+"."+nom+"@gmail.com";

        config.getDriver().findElement(new By.ByName("players[0].forename")).sendKeys(nom);
        config.getDriver().findElement(new By.ByName("players[0].surname")).sendKeys(prenom);
        config.getDriver().findElement(new By.ByName("players[0].mail")).sendKeys(mail);
        config.getDriver().findElement(new By.ByName("players[0].pseudo")).sendKeys("test1");

        config.getDriver().findElement(new By.ByName("players[1].forename")).sendKeys(nom);
        config.getDriver().findElement(new By.ByName("players[1].surname")).sendKeys(prenom);
        config.getDriver().findElement(new By.ByName("players[1].mail")).sendKeys(mail);
        config.getDriver().findElement(new By.ByName("players[1].pseudo")).sendKeys("test2");

        config.getDriver().findElement(new By.ById("sendTeam")).click();

        Assertions.assertTrue(config.getDriver().findElement
                (new By.ByXPath("/html/body/div/div[2]/form/div[2]/span")).isDisplayed());
    }

    @AfterAll
    void tearDown() {
        config.getDriver().quit();
    }

    private String generateName(){
        String str = "";
        for(int i = 0; i < 5 ; i++){
            char c = (char)(new Random().nextInt(25)+'a');
            str +=c;
        }
        return str;
    }
}

