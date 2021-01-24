## Pattern & Frameworks

Sehr geehrter Herr Ehlers,

herzlich willkommen in unserem Multiplayer-Spiel.

Damit Sie sich direkt zurechtfinden ein paar Informationen vorab:

###Einstiegspunkt

Nachdem Sie unser Repository ausgechecked haben, finden Sie in der Projektstruktur das Modul 'multiplayer'. Darin befinden sich die beiden Module 'client' und 'server'.

###Datenbankkonfiguration

Im Modul 'server' finden Sie im Verzeichnis 'resources' die 'application.properties' mit den Informationen zur Datenbank-Konfiguration. Weiterhin finden Sie in diesem Verzeichnis ein SQL-Script mit Testdaten, welche Sie in die Datenbank importieren können.

###Testdaten

####User:
Jorn // Password123!
Nora // Password123!
HerrEhlers // Password123!

####History:
Zwei Spielhistorien je User

###Anwendung starten

Um die Anwendung zur Starten, führen Sie zunächst 'ServerApplication.java' aus. Sobald der Server gestartet ist, führen Sie zwei Instanzen von 'Client.java' aus.

Melden Sie sich in beiden Clients an oder legen Sie neue Benutzer an. Haben Sie sich angemeldet, werden Sie zur Lobby weitergeleitet. An oberster Position befindet sich ein Default-Profilbild, welches bei der Anmeldung vom Server an den Client geschickt wird. Darunter können Sie zwischen 'Lets play!', 'Statistic' und 'Logout' wählen.

####Lets play!
Nachdem beide User auf 'Lets play!' geklickt haben, werden die User zur Charakter-Auswahl weitergeleitet. Beide User wählen einen Charakter aus und bestätigen die Auswahl mit einem Klick auf "submit". Anschließend startet das Spiel und ein Countdown wird gestartet. Die Charaktere werden mit den Pfeiltasten Links/Rechts gesteuert und greifen mit Klick auf die Leertaste an. Fällt die Lebensanzahl eines Charakters auf 0, erscheint eine Meldung, ob das Spiel gewonnen oder verloren wurde. Wenige Sekunden später erscheint der Button 'Exit Game', mit dem das laufende Spiel beendet und ein Eintrag in die Historie der jeweiligen User in der Datenbank angelegt wird. Nachdem das Spiel beendet wurde, werden die User zum Bereich 'Statistic' weitergeleitet, in dem die Historie des jeweiliges User eingesehen werden kann. Von dort aus gelangt der User mit Klick auf den Button 'back' zurück zur Lobby.

####Statistic
Mit einem Klick auf 'Statistic' kann der User jederzeit seine Spielhistorie einsehen. In diesen Bereich werden die User automatisch nach dem Beenden eines Spiels geleitet. Mit Klick auf den Button 'back' gelangt der User zurück zur Lobby.

####Logout
Der User wird ausgeloggt und der JWT zur Authentifizierung wird verworfen.

###Hinweise
Nachdem wir Projekt initial im Gitlab-Repository angelegt hatten, hatten wir Probleme bei der Versionierung und haben das Spiel in unserem Privaten Github-Repository eingechecked, wo wir den Großteil der Arbeit vorgenommen haben. Der Github-Repository ist öffentlich und unter diesem Link erreichbar: https://github.com/Pokar447/multiplayer. Abschließend haben wir das Projekt zur Abgabe wieder in das bereitgestellt GitLab-Repository eingechecked.

Viel Spaß beim Ausprobieren.

Mit freundlichen Grüßen

Nora & Jorn
