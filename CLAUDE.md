# Tome of Heroes

Ein D&D-3.5-Charakter-Editor als JavaFX-Desktop-App mit Spring Boot als schlankem DI-Container. Der Nutzer (Martin) codet das selbst und lässt Claude Code beim Refactoring, Bugfixing und UI-Feinschliff mitarbeiten — siehe [doc/ARBEITSPLAN.md](doc/ARBEITSPLAN.md) für den aktuellen Stand und die nächsten Schritte.

## Build & Run

```bash
./mvnw clean compile        # kompilieren
./mvnw javafx:run           # App starten
```

Kein `mvn spring-boot:run` — die Spring-Boot-Anwendung startet nur den DI-Context, das eigentliche Fenster kommt von JavaFX (`TomeOfHeroesApplication.main()` bootet Spring, dann `Application.launch(TomeOfHeroesApp.class)`).

Kein `module-info.java` mehr (bewusst entfernt) — JPMS und Spring's reflection-lastiges DI vertragen sich schlecht, und es gibt keinen jlink-Packaging-Bedarf, der die Reibung rechtfertigen würde.

## Architektur

**Schichtung** (siehe auch [doc/README.md](doc/README.md)):
- **Input** — vom Nutzer direkt editierbare Werte (`AttributesInput`, `DescriptionInput`, ...)
- **Computed** — abgeleitete/berechnete Werte, reaktiv über JavaFX-Properties (`AttributesComputed`, ...)
- **Rules** — statische Entscheidungslogik ("ist das erlaubt?", z.B. `ClassRules`, `FeatRules`)
- **Calculator** — reine Berechnungsfunktionen (z.B. `AttributeCalculator`, `FeatCalculator`)

**Wichtige Regel bei `ComputedBase`-Subklassen:** `addListeners()` muss den Wert *sofort einmal* berechnen, nicht nur einen Listener für künftige Änderungen registrieren — sonst ist der Anfangszustand falsch, bis irgendein anderes Event zufällig einen Refresh auslöst (war ein realer Bug, siehe Git-Historie `ed9d000`).

**Mehrere Charaktere:** `CharacterModel` ist *kein* Spring-Singleton mehr, sondern wird über `CharacterModelFactory.createCharacter()` erzeugt (ein echter Spring-Bean, hält nur die geteilten, zustandslosen Services `FeatRepository`/`FeatRules`). Jeder Charakter bekommt eigene Instanzen von `Feats`/`FeatsInput` (ebenfalls keine Spring-Beans mehr, konsistent mit `Attributes`/`Classes`/etc., die schon immer plain POJOs waren).

**UI-Struktur:** Ein Hauptfenster (`MainWindowController` + `main-window.fxml`), darin ein `TabPane` — jeder Tab ist ein Charakter mit eigenem `CharacterSheetController`. Innerhalb eines Charakter-Tabs: Kopfzeile (Name, Klasse & Stufe — aggregiert über *alle* Klassen für Multiklassen-Charaktere) + Hintergrund direkt darunter, dann Kampf-Bereich (Attribute + Rettungswürfe) und Talente-Bereich (Klassen + Talente), alles in einer durchgehenden Ansicht ohne innere Tabs (die lohnen sich erst, wenn genug Inhalt für Platzmangel sorgt — z.B. sobald Fertigkeiten/Ausrüstung/Zauber dazukommen).

**Controller-Wiring:** Card-Controller (Attributes/Classes/Feats/...) werden *nicht* über Spring aufgelöst, sondern direkt mit `new XyzController(characterModel)` in `CharacterSheetController.controllerFactory()` erzeugt — jeder Charakter-Tab braucht seine eigenen, unabhängigen Controller-Instanzen.

**Datengetriebene Talente:** `feats.yml` → `FeatConfig`/`FeatDefinition` (Spring `@ConfigurationProperties`) → `FeatMapper` → `Feat`-Domänenmodell. Neue Talente sind Daten, kein Code.

## UI-Konventionen (in dieser Session erarbeitet)

- **`.field-computed`** (CSS-Klasse in `app.css`): grauer Hintergrund für schreibgeschützte/berechnete Felder, damit auf einen Blick klar ist, was editierbar ist und was nicht. Bei jedem neuen `TextField`, das an eine `Computed`-Property gebunden wird: `editable="false"` + `styleClass="field-computed"` nicht vergessen (sonst kann man reintippen und der bidirektionale Bind überschreibt den berechneten Wert stillschweigend).
- **`.icon-button`** (CSS-Klasse): transparenter, eng gepaddeter Button für Icon-only-Aktionen (Löschen/Hinzufügen in Tabellenzeilen). FontAwesome-Icons über Ikonli, einheitlich 14px, `setTranslateY(-1)` als Korrektur für die leichte optische Tiefenverschiebung von Icon-Fonts.
- **Übersetzer-Klassen** in `dev.swim.toh.translation`: `ClazzTranslator`, `RaceTranslator`, `SizeTranslator`, `AttributeNameTranslator`, `PrerequisiteFormatter` — Enums/Model-Objekte bekommen hier ihre deutschen Anzeigenamen, nie Rohtext im UI-Code.
- **`SelectionDialogController<T>`** (`ui/controller/dialog/`): generischer "wähle eine oder mehrere aus"-Dialog, aktuell von Klassen- und Talente-Auswahl genutzt. Neue Auswahl-Dialoge sollten diesen wiederverwenden statt einen eigenen zu bauen.
- **Tabellen sollen nie scrollen, immer alle Zeilen zeigen** (expliziter Nutzerwunsch). Dafür:
  - `Layout.updateTableHeight(tableView)` aufrufen (misst die *echte* Header-Höhe per Lookup, rät nicht) statt eine Höhe zu raten.
  - `TableView.CONSTRAINED_RESIZE_POLICY` statt manuell berechneter `prefWidthProperty()`-Bindings — JavaFX kennt die tatsächlich verfügbare Breite besser als jede eigene Formel.
  - **Nie** mit einer geschätzten Pixel-Konstante "Puffern" — das war ein Holzweg in dieser Session (siehe Git-Historie: ein zu knapper Wert löst einen Scrollbalken aus, der wiederum Platz frisst und einen zweiten Scrollbalken nach sich zieht). Bei neuen Höhen-/Breitenproblemen: mit einem kleinen Diagnose-Programm (`Platform.runLater` + `tableView.lookup(...)`) echte Werte messen, nicht raten.
- Lange Charakterbogen-Inhalte: der Charakterbogen (`character-sheet.fxml`) ist in ein `ScrollPane` gewrappt (`fitToWidth=true`) — ein äußerer Scroll-Rahmen statt verschachtelter Tabellen-Scrollbalken.

## Bekannte, bewusst unfertige Stellen

- **Magie-Mod bei Rettungswürfen** (`SavingThrowsComputed`) ist ein `Computed`-Platzhalter, immer 0 (`// TODO: magic mods`). Absichtlich nicht auf Input umgestellt, da unklar ist, ob es später aus einem Ausrüstungssystem berechnet werden soll.
- **"verfügbar"-Feld** bei Talenten (`availableFeatsCountTextField`) ist nie an echte Daten gebunden.
- **"durch Klasse"-Spalte** bei Talenten wurde entfernt statt zum Schein verdrahtet — die Daten dafür existieren nirgends (`Feat.isGrantedByClasses` war ein totes, nie befülltes Feld und wurde entfernt). Würde eine eigene Funktion brauchen: pro ausgewähltem Talent festhalten, ob es einen normalen Slot verbraucht oder über einen Klassen-Bonus (z.B. Kämpfer-Bonustalente) kostenlos war.
- Sprachumschalter (DE/EN) ist auf Nutzerwunsch zurückgestellt — würde ein echtes i18n-Ressourcen-Bundle-Setup brauchen, kein Quick-Fix.

Details und Priorisierung der nächsten Schritte: [doc/ARBEITSPLAN.md](doc/ARBEITSPLAN.md).
