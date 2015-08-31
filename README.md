#Laboration - JPA, Spring Data JPA

##Innehåll
<!-- MarkdownTOC -->

- Syfte
- Beskrivning
- Generella krav
- Funktionskrav - Godkänd
- Redovisning

<!-- /MarkdownTOC -->

## Syfte
Laborationens syfte är att befästa de kunskaper ni har inom JAX-RS samt validera era kunskaper inom JPA/Spring Data JPA.

## Beskrivning
Under denna laboration ska ni konstruera ett pragmatiskt ärendehanteringssystem. 

Ni kommer att bygga det lager som har hand om lagring av data samt ett tillhörande webb-api.

Systemet ska tillåta en användare lägga upp ärenden som sedan kan **prioriteras** och hanteras. Ni ska med andra ord digitalisera era Scrum-tavlor fast göra det mera “Kanban-style”. 

Detta projekt kommer att ligga till grund för nästa kurs då ni kommer att bygga ett grafiskt gränssnitt till detta.

Du kan hämta inspiration från https://www.pivotaltracker.com och http://leankit.com/

## Generella krav
Det ska vara minst två maven-projekt. Ett som har hand om webb-api:et (JAX-RS) och ett för ert datalager (Spring Data JPA). 

Koden MÅSTE vara formaterad korrekt och får INTE innehålla några System.out (förutom i en Main-klass om ni har en sådan). 

All funktionalitet ska exponeras som webb-tjänster. Ni kommer alltså ha en liknande arkitektur som webb-shoppen. 

Notera att det är ett krav att använda Spring Data JPA och JAX-RS. 

I övrigt MÅSTE koden vara väl strukturerad och följa en bra objektorienterad design.

## Funktionskrav - Godkänd
###User - en användare i systemet som tillhör ett team
Funktioner:
- Skapa en User
- Uppdatera en User
- Ta bort* en User
- Hämta en User baserat på user id (inte entity id)
- Söka efter en User baserat på förnamn eller efternamn eller användarnamn
- Hämta alla User som ingår i ett visst team

### Team - en gruppering av User
Funktioner:
- Skapa ett team
- Uppdatera ett team
- Ta bort* ett team 
- Hämta alla team
- Lägga till en User till ett team

### Work item - ett ärende som tilldelas en User
Funktioner:
- Skapa en work item
- Ändra status på en work item
- Ta bort* en work item
- Tilldela en work item till en User
- Hämta alla work item baserat på status
- Hämta alla work item för ett Team
- Hämta alla work item för en User
- Söka efter work item som innehåller en viss text i sin beskrivning

### Issue - en anmärkning som kan ges en work item när den inte accepteras
Funktioner:
- Skapa en Issue
- Uppdatera en Issue
- Lägga till en Issue till en work item
- Hämta alla work item som har en Issue 

Exakt vilka datamedlemmar som varje entitet har samt hur dessa hänger ihop är upp till er att bestämma. Det finns dock några givna datamedlemmar som behöver finnas utifrån de funktionskrav som finns. 

Det är tillåtet att ha fler entiteter än dessa om ni anser att ni behöver det.

_* När ni tar bort en entitet behöver ni fundera på hur detta ska påverka eventuellt relaterade entiteter, dvs, vilken/vilka cascade type(s) ska användas_
## Redovisning
Redovisning sker i grupp fredagen den *11/9 för nivå Godkänd* och enskilt fredagen den *18/9 för nivå Väl Godkänd*. 

Notera: alla i gruppen måste vara med och redovisa för att bli godkänd.