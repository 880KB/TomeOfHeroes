# Tome of Heroes

Ein D&D-3.5-Charakter-Editor als JavaFX-Desktop-App mit Spring Boot als schlankem DI-Container. Der Nutzer (Martin) codet das selbst und lässt Claude Code beim Refactoring, Bugfixing und UI-Feinschliff mitarbeiten — siehe [doc/ARBEITSPLAN.md](doc/ARBEITSPLAN.md) für den aktuellen Stand und die nächsten Schritte.

## Build & Run

```bash
./mvnw clean compile        # kompilieren
./mvnw javafx:run           # App starten
```

Kein `mvn spring-boot:run` — die Spring-Boot-Anwendung startet nur den DI-Context, das eigentliche Fenster kommt von JavaFX (`TomeOfHeroesApplication.main()` bootet Spring, dann `Application.launch(TomeOfHeroesApp.class)`).

Kein `module-info.java` mehr (bewusst entfernt) — JPMS und Spring's reflection-lastiges DI vertragen sich schlecht, und es gibt keinen jlink-Packaging-Bedarf, der die Reibung rechtfertigen würde.

## Regelwerk-Basis

Der Editor basiert einheitlich auf **D&D 3.5**, nicht 3.0 (Entscheidung 2026-09-07). Martins Gruppe spielt zwar mit den 3.0-Kernbüchern, nutzt aber Zusatzmaterial (u. a. Forgotten Realms), das es nur in 3.5 gibt, und bewegt sich damit ohnehin faktisch im 3.5-Rahmen; dazu soll der Editor perspektivisch auch an befreundete Gruppen weitergegeben werden, wo 3.5 die deutlich breitere Basis hat. Ein Mischbetrieb beider Regelwerke ist bewusst ausgeschlossen, obwohl die datengetriebene Struktur (`feats.yml`, `weapons.yml`, ...) ihn technisch erlauben würde — der doppelte Rechercheaufwand pro Talent/Waffe/Zauber lohnt sich erst, wenn ein Regelwerk vollständig steht.

Praktische Konsequenz: alle Datenquellen (PDF-Scans, Tabellen, Talentbeschreibungen) müssen aus 3.5-Material stammen. Bereits bekannter Verdachtsfall: `feats.yml` stammt vermutlich aus einer 3.0-Quelle (siehe [doc/ARBEITSPLAN.md](doc/ARBEITSPLAN.md), Punkt 4) und muss gegen 3.5 gegengeprüft werden.

## Architektur

**Schichtung** (siehe auch [doc/README.md](doc/README.md)):
- **Input** — vom Nutzer direkt editierbare Werte (`AttributesInput`, `DescriptionInput`, ...)
- **Computed** — abgeleitete/berechnete Werte, reaktiv über JavaFX-Properties (`AttributesComputed`, ...)
- **Rules** — statische Entscheidungslogik ("ist das erlaubt?", z.B. `ClassRules`, `FeatRules`)
- **Calculator** — reine Berechnungsfunktionen (z.B. `AttributeCalculator`, `FeatCalculator`)

**Wichtige Regel bei `ComputedBase`-Subklassen:** `addListeners()` muss den Wert *sofort einmal* berechnen, nicht nur einen Listener für künftige Änderungen registrieren — sonst ist der Anfangszustand falsch, bis irgendein anderes Event zufällig einen Refresh auslöst (war ein realer Bug, siehe Git-Historie `ed9d000`).

**Fortschritts-Historie vs. Neuberechnung:** Die App soll perspektivisch einen Charakter über die Zeit begleiten (Stufenaufstiege, Ausrüstungswechsel, ...), nicht nur einen Endzustand aus aktuellen Werten neu herleiten. Deshalb sind manche Entscheidungen bewusst **eingefroren** statt laufend neu berechnet, obwohl sie technisch als `Computed` herleitbar wären — Beispiel: welchen Talent-Slot-Pool ein gewähltes Talent verbraucht (`SelectedFeat.pool`, gesetzt einmalig bei der Auswahl über `FeatsComputed.assignPool()`, siehe unten). Faustregel: Werte, die *aktuellen* Zustand beschreiben (z.B. "wie viele Slots habe ich insgesamt"), bleiben `Computed`; Werte, die eine *vergangene Entscheidung* festhalten (z.B. "welchen Slot hat dieses konkrete Talent damals belegt"), werden `Input` und nur einmal beim entsprechenden Ereignis gesetzt.

**Mehrere Charaktere:** `CharacterModel` ist *kein* Spring-Singleton mehr, sondern wird über `CharacterModelFactory.createCharacter()` erzeugt (ein echter Spring-Bean, hält nur die geteilten, zustandslosen Services `FeatRepository`/`FeatRules`). Jeder Charakter bekommt eigene Instanzen von `Feats`/`FeatsInput` (ebenfalls keine Spring-Beans mehr, konsistent mit `Attributes`/`Classes`/etc., die schon immer plain POJOs waren).

**UI-Struktur:** Ein Hauptfenster (`MainWindowController` + `main-window.fxml`), darin ein `TabPane` — jeder Tab ist ein Charakter mit eigenem `CharacterSheetController`. Innerhalb eines Charakter-Tabs: Kopfzeile (Name, Klasse & Stufe — aggregiert über *alle* Klassen für Multiklassen-Charaktere) + Hintergrund direkt darunter, dann Kampf-Bereich (Attribute + Rettungswürfe) und Talente-Bereich (Klassen + Talente), alles in einer durchgehenden Ansicht ohne innere Tabs (die lohnen sich erst, wenn genug Inhalt für Platzmangel sorgt — z.B. sobald Fertigkeiten/Ausrüstung/Zauber dazukommen).

**Controller-Wiring:** Card-Controller (Attributes/Classes/Feats/...) werden *nicht* über Spring aufgelöst, sondern direkt mit `new XyzController(characterModel)` in `CharacterSheetController.controllerFactory()` erzeugt — jeder Charakter-Tab braucht seine eigenen, unabhängigen Controller-Instanzen.

**Datengetriebene Talente:** `feats.yml` → `FeatConfig`/`FeatDefinition` (Spring `@ConfigurationProperties`) → `FeatMapper` → `Feat`-Domänenmodell. Neue Talente sind Daten, kein Code.

## UI-Konventionen (in dieser Session erarbeitet)

- **`.field-computed`** (CSS-Klasse in `app.css`): grauer Hintergrund für schreibgeschützte/berechnete Felder, damit auf einen Blick klar ist, was editierbar ist und was nicht. Bei jedem neuen `TextField`, das an eine `Computed`-Property gebunden wird: `editable="false"` + `styleClass="field-computed"` nicht vergessen (sonst kann man reintippen und der bidirektionale Bind überschreibt den berechneten Wert stillschweigend).
- **`.icon-button`** (CSS-Klasse): transparenter, eng gepaddeter Button für Icon-only-Aktionen (Löschen/Hinzufügen in Tabellenzeilen). FontAwesome-Icons über Ikonli, einheitlich 14px, `setTranslateY(-1)` als Korrektur für die leichte optische Tiefenverschiebung von Icon-Fonts.
- **`.label-badge`** (CSS-Klasse): dunkler Hintergrund (aktuell Platzhalter-Navy, Farbpalette kommt erst ganz am Ende dran) + weiße Schrift für die kurzen Bezeichner-Labels vor einem Wert (ST/GE/KO/IN/WE/CH, RK, Initiative, TP, Reflex/Willen/Zähigkeit, RK Berührung/Auf dem falschen Fuß/Zauberresistenz), angelehnt an die schwarz hinterlegten Bezeichner auf dem Papier-Charakterbogen (`doc/Charakterbogen.pdf`). Braucht `maxWidth="Infinity"` + `alignment="CENTER"` auf dem `Label`, damit der Badge die volle Spaltenbreite ausfüllt; die erste Spalte ggf. breit genug wählen (mind. ~38px bei zweistelligen Kürzeln), sonst schneidet das Padding den Text zu "…" ab.
- **Übersetzer-Klassen** in `dev.swim.toh.translation`: `ClazzTranslator`, `RaceTranslator`, `SizeTranslator`, `AttributeNameTranslator`, `PrerequisiteFormatter` — Enums/Model-Objekte bekommen hier ihre deutschen Anzeigenamen, nie Rohtext im UI-Code.
- **`SelectionDialogController<T>`** (`ui/controller/dialog/`): generischer "wähle eine oder mehrere aus"-Dialog, aktuell von Klassen- und Talente-Auswahl genutzt. Neue Auswahl-Dialoge sollten diesen wiederverwenden statt einen eigenen zu bauen.
- **Tabellen sollen nie scrollen, immer alle Zeilen zeigen** (expliziter Nutzerwunsch). Dafür:
  - `Layout.updateTableHeight(tableView)` aufrufen (misst die *echte* Header-Höhe per Lookup, rät nicht) statt eine Höhe zu raten.
  - `TableView.CONSTRAINED_RESIZE_POLICY` statt manuell berechneter `prefWidthProperty()`-Bindings — JavaFX kennt die tatsächlich verfügbare Breite besser als jede eigene Formel.
  - **Nie** mit einer geschätzten Pixel-Konstante "Puffern" — das war ein Holzweg in dieser Session (siehe Git-Historie: ein zu knapper Wert löst einen Scrollbalken aus, der wiederum Platz frisst und einen zweiten Scrollbalken nach sich zieht). Bei neuen Höhen-/Breitenproblemen: mit einem kleinen Diagnose-Programm (`Platform.runLater` + `tableView.lookup(...)`) echte Werte messen, nicht raten.
- Lange Charakterbogen-Inhalte: der Charakterbogen (`character-sheet.fxml`) ist in ein `ScrollPane` gewrappt (`fitToWidth=true`) — ein äußerer Scroll-Rahmen statt verschachtelter Tabellen-Scrollbalken.

## Bekannte, bewusst unfertige Stellen

- **Magie-Mod bei Rettungswürfen** (`SavingThrowsComputed`) ist ein `Computed`-Platzhalter, immer 0 (`// TODO: magic mods`). Absichtlich nicht auf Input umgestellt, da unklar ist, ob es später aus einem Ausrüstungssystem berechnet werden soll.
- **Talent-Slot-Zeilen** unterhalb der Talente-Tabelle zeigen "belegt/max" für alle vier Pools (Allg., Kämpfer, Magier, Mensch — die letzten drei ausgegraut über `opacityProperty()`, wenn ihr Maximum 0 ist). Welchen Pool ein gewähltes Talent verbraucht, wird **einmalig bei der Auswahl** entschieden und danach eingefroren (`SelectedFeat.pool`, `Input`-Zustand, gesetzt über `FeatsComputed.assignPool()` direkt nach dem Hinzufügen) — nicht laufend aus dem aktuellen Zustand neu hergeleitet. Grund: ein späterer Stufenaufstieg (neuer Kämpfer-/Magier-Bonus-Slot) darf ein *bereits gewähltes* Talent nicht rückwirkend umkategorisieren, nur *künftige* Wahlen sehen die neue Kapazität — siehe die Fortschritts-Historie-Regel oben. `assignPool` ruft die reine Funktion `FeatSlotCalculator.allocate()` (`model.calculation`, unverändert seit ihrer Einführung) mit den *aktuell verbleibenden* Kapazitäten auf (`max - bereits eingefroren`, letzteres über vier `FilteredList`-basierte `usedX`-Properties in `FeatsComputed`, reaktiv dank Extractor auf `SelectedFeat.poolProperty()` in `FeatsInput.featList`). Feste Priorität Kämpfer-Bonus → Magier-Bonus → Mensch-Bonus → Allg. Kämpfer-Eignung ist ein manuelles YAML-Tag (`bonusFeatClasses: [FIGHTER]`, 30 Talente aus der PHB-Liste, zwei absichtlich nicht getaggte Grenzfälle `GESCHOSSE_ABWEHREN`/`NIEDERREITEN` noch gegen das Buch zu prüfen), Magier-Eignung leitet sich strukturell aus `FeatType` (ITEM_CREATION/METAMAGIC) ab. **Allg. ist der einzige unbeschränkte Auffangpool** — `FeatSlotCalculator` lehnt dort nie ein Talent ab, auch über die eigene Kapazität hinaus (die Zuordnung "welches Talent ist das überzählige" wäre willkürlich, das Feld `maxClassFeatsTextField` bekommt stattdessen die CSS-Klasse `field-exceeded` und färbt sich rot, sobald `usedClassFeats > maxClassFeats`). Regressionstest fürs Einfrieren: `FeatPoolFreezeTest`.
- **Magier-Freischriftrolle**: `AutomaticFeatGrants` (`model.core.feats`) vergibt „Schriftrolle anfertigen" automatisch, sobald der Charakter eine Magier-Klasse hat, entzieht sie wieder beim Entfernen. Bewusst **keine** rückwirkende Übernahme: hatte der Spieler das Talent schon manuell gewählt, bevor er Magier wurde, bleibt es dauerhaft eine normale, slot-kostende Auswahl (`FeatsInput.addAutomaticFeat` ist dann ein No-op) — nach den Regeln wird das Freitalent beim *Erreichen* der ersten Magierstufe vergeben, nicht rückwirkend neu bewertet. Beim Laden gespeicherter Charaktere werden Talente bewusst **vor** Klassen wiederhergestellt (`CharacterDataMapper.applyTo`, über `Feats.restoreFeat` — Pool und Automatik-Status sind dort historische Fakten, keine Neuberechnung), sonst würde `AutomaticFeatGrants` beim Laden der Magier-Klasse sofort eine frische Schriftrolle vergeben, bevor die gespeicherte (ggf. manuell bezahlte) wiederhergestellt ist, und diese als Duplikat verdrängen. `addAutomaticFeat` gibt bei einer bereits vorhandenen, schon-automatischen Instanz diese zurück (statt `null`), damit `AutomaticFeatGrants` seine interne Referenz auch nach dem Laden korrekt setzt — sonst würde eine geladene automatische Schriftrolle beim späteren Entfernen der Magier-Klasse nie aufgeräumt. Zählt, solange automatisch, nicht gegen den Magier-Bonus-Pool, kein Löschen-Button in der Tabelle (siehe `isAutomaticGrant`). Beide Szenarien (Einfrieren, Automatik-Rundtrip inkl. Aufräumen) sind in `FeatPoolFreezeTest` abgedeckt.
- Sprachumschalter (DE/EN) ist auf Nutzerwunsch zurückgestellt — würde ein echtes i18n-Ressourcen-Bundle-Setup brauchen, kein Quick-Fix.

Details und Priorisierung der nächsten Schritte: [doc/ARBEITSPLAN.md](doc/ARBEITSPLAN.md).
