package nu.jixa.its;

import nu.jixa.its.model.ModelUtilTest;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;

/*
 ____________________________________________________
| FUNKTIONSKRAV                                      |
|____________________________________________________|
| Location  | Requirement                            |
USER
✓User       | Skapa en User
✓User       | Uppdatera en User
✓User       | Ta bort* en User
✓User       | Hämta en User baserat på user id (inte entity id)
✓User       | Söka efter en User baserat på förnamn eller efternamn eller användarnamn
✓UserTeam   | Hämta alla User som ingår i ett visst team

TEAM
✓Team       | Skapa ett team
✓Team       | Uppdatera ett team
✓Team       | Ta bort* ett team
✓Team       | Hämta alla team
✓UserTeam   | Lägga till en User till ett team

WORKITEM
✓WorkItem   | Skapa en work item
✓WorkItem   | Ändra status på en work item
✓WorkItem   | Ta bort* en work item
✓WorkItem   | Tilldela en work item till en User
✓WorkItem   | Hämta alla work item baserat på status
✓WorkItem   | Hämta alla work item för ett Team
✓WorkItem   | Hämta alla work item för en User
✓WorkItem   | Söka efter work item som innehåller en viss text i sin beskrivning

Issue
✓WorkItem   | Skapa en Issue
✓WorkItem   | Uppdatera en Issue
✓WorkItem   | Lägga till en Issue till en work item
✓WorkItem   | Hämta alla work item som har en Issue
____________|________________________________________|
*/

@RunWith(Suite.class)
@Suite.SuiteClasses({
    ModelUtilTest.class,
    UserRepositoryTest.class,
    UserTeamRepositoryTest.class,
    WorkItemRepositoryTest.class,
    TeamRepositoryTest.class
})
public class AllTestsSuite {
}
