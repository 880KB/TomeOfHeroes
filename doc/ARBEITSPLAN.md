# Arbeitsplan

Stand: siehe letzten Commit. Diese Datei hält fest, was in den letzten Claude-Code-Sessions gemacht wurde und was als Nächstes ansteht, damit eine neue Session ohne Kontextverlust anknüpfen kann.

## Erledigt

**Grundlegende Bugfixes** (`ed9d000`)
- FXML-Ladepfade korrigiert (`/main/resources/fxml/...` → `/fxml/...`, funktionierte nur durch einen veralteten `target`-Ordner zufällig).
- `ComputedBase`-Subklassen berechnen ihren Anfangswert jetzt sofort, statt nur auf künftige Änderungen zu warten.

**Architektur** (`64dde89`)
- `module-info.java` entfernt (Reibung mit Spring, kein jlink-Bedarf).
- Mehrere Charaktere pro Programmlauf möglich: `CharacterModel` ist kein Spring-Singleton mehr, sondern kommt aus `CharacterModelFactory`. Ein Hauptfenster mit `TabPane`, ein Tab pro Charakter.

**Layout-Neugestaltung** (`295f391`)
- Kopfzeile pro Charakter (Name, Klasse & Stufe — summiert über alle Klassen bei Multiklassen-Charakteren, Rasse).
- Hintergrund (Volk/Alter/Größe) direkt unter der Kopfzeile statt isoliert am Seitenende.
- Innere Tabs (Kampf/Talente/Hintergrund) wieder verworfen — bei so wenig Inhalt nur Klicklast ohne Nutzen. Lohnt sich erst mit mehr Karten (Fertigkeiten, Ausrüstung, Zauber).
- Klassen/Talente-Tabellen mit Beschriftung, damit zwei Tabellen nebeneinander nicht wie eine wirken.
- Berechnete Felder optisch von editierbaren unterschieden (`.field-computed`-CSS-Klasse).
- Deutsche Übersetzungen für Klassen/Rassen statt Rohtext-Enums.
- Debug-Button "Print Character" entfernt.

**Tabellen-Usability** (`9b7d784`)
- Einheitlicher Auswahl-Dialog (`SelectionDialogController<T>`) für Klassen *und* Talente statt zwei unterschiedlicher Mechanismen; zeigt bei Talenten jetzt auch, *warum* eine Voraussetzung nicht erfüllt ist.
- "1. Klasse" ist eine echte, klickbare Checkbox (mit Invariante: immer genau eine erste Klasse).
- Stufe wird über einen Spinner mit sichtbaren +/- Pfeilen bearbeitet statt per unentdeckbarem Doppelklick.
- Sortierung im Auswahl-Dialog gefixt (lief vorher ins Leere, weil vor dem Befüllen der Liste ausgeführt).
- Scrollbalken-Problem bei wachsenden Tabellen ursächlich behoben: echte Header-Höhe wird gemessen statt geraten, Spaltenbreiten über `CONSTRAINED_RESIZE_POLICY` statt eigener Formel.
- "+"-Buttons zum Hinzufügen sitzen jetzt neben der Abschnittsüberschrift statt in der Kennzahlen-Zeile.
- Icon-Konsistenz zwischen Tabellen (einheitliches FontAwesome-Icon-Set statt Icon/Emoji-Mix).

**Regelkonformitäts-Anzeige (Violation-Konzept)**
- `dev.swim.toh.model.validation.Violation`/`Severity`: generisches Format für "Charakter verletzt gerade eine Regel", mit Nachricht + Bezug auf das betroffene Objekt.
- `FeatsValidation` (in `model.core.feats`) prüft kontinuierlich (nicht nur beim Hinzufügen) alle gewählten Talente gegen `FeatRules.prerequisitesSatisfied()` — reagiert auf Änderungen an Attributen und der Talentliste selbst.
- `FeatsViewController` zeigt nicht mehr nur rote Schrift bei unerfüllter Voraussetzung, sondern auch einen Tooltip mit der konkreten Begründung.

**Kampfwerte: TP, RK, Initiative**
- Neue Domänen ins Modell aufgenommen: `HitPoints` (TP aktuell/max), `ArmorClass` (RK mit voller Formel: Basis 10 + Rüstungs-/Schild-/Ablenkungsbonus + natürl. Rüstung + GE-Mod. + Größen-Mod. + Sonst., dazu RK Berührung und Auf dem falschen Fuß als abgeleitete Teilmengen derselben Formel), `Initiative` (GE-Mod. + Sonst.), `SpellResistance` (manueller Platzhalter, noch ohne Formel).
- UI: eigene Karten pro Wert, alle vier untereinander neben der Attribute-Tabelle platziert (`hit-points-view`, `armor-class-view`, `initiative-view`); Rettungswürfe dafür in eine eigene Zeile darunter verschoben.
- `SizeCalculator.getAcModifier(Size)` neu für den Größen-Modifikator auf die RK.

**Bonus-Aggregator (erster Schnitt)**
- `dev.swim.toh.model.core.bonus`: `BonusPool` (pro Charakter, nach `BonusTarget` partitioniert), `Bonus`-Record (Typ, Wert, Quelle), `BonusType`-Enum mit den D&D-3.5-Bonustypen und ihrer Stacking-Regel (`stacks()` — nur Dodge/Untyped stacken mit sich selbst).
- `BonusCalculator.getTotal()`: reine Funktion, gruppiert nach Typ, nicht-stackende Typen nehmen das Maximum, stackende Typen die Summe.
- Verankert an TP-Max (`HitPointsComputed` liest `bonusPool.getBonuses(HpBonusTarget.MAX_HIT_POINTS)`), noch nicht an RK/Rettungswürfen.
- `Feat.benefits` reaktiviert: `BonusBenefit` trägt beim Hinzufügen eines Talents einen Bonus in den Pool ein und entfernt ihn beim Löschen wieder (`FeatsInput`). `feats.yml` hat dafür ein `benefits:`-Schema bekommen; Toughness gibt jetzt tatsächlich +3 TP und stackt korrekt bei mehrfacher Wahl.
- Bugfix dabei gefunden: `FeatRules.getNotSelectedFeats()` blendete jedes bereits gewählte Talent aus dem Auswahldialog aus, auch wiederholbare (`STACKS`/`MULTIPLE`) — Toughness ließ sich dadurch nur einmal wählen. Nutzt jetzt dieselbe `notSelectedOrRepeatable()`-Prüfung wie `canAdd()`.
- Bugfix: Klassen-/Talente-Tabellen standen in ihrem `BorderPane` vertikal zentriert statt oben, sobald die jeweils andere Tabelle höher wurde (`BorderPane`-Center-Default) — `BorderPane.alignment="TOP_CENTER"` auf beiden `TableView`s ergänzt.

**Persistenz (Speichern/Laden)**
- Neues Package `dev.swim.toh.persistence`: `CharacterData` (reines, Jackson-freundliches Snapshot-DTO nur der Input-Werte — Computed wird beim Laden neu hergeleitet statt mitgespeichert), `CharacterDataMapper` (`toData`/`applyTo`), `CharacterFileService` (Spring-Bean, liest/schreibt `.json` über `jackson-databind`).
- Ein Charakter = eine `.json`-Datei, die der Nutzer selbst per `FileChooser` ablegt (kein impliziter App-Speicherordner) — passt zum Papier-Vorbild "ein Bogen = eine Datei".
- `CharacterModelFactory` unterscheidet jetzt `createCharacter()` (mit Testdaten, für "Neuer Charakter") und `createEmptyCharacter()` (ohne, fürs Laden — `CharacterModel` selbst ruft `initTestData()` nicht mehr automatisch im Konstruktor auf).
- `MainWindowController` merkt sich pro Tab die zuletzt geladene/gespeicherte Datei (`Tab.setUserData(...)`), damit "Speichern" ohne Dialog überschreibt und nur "Speichern unter" den Dialog zeigt.
- UI: Menüleiste ("Datei" → Neuer Charakter/Öffnen/Speichern/Speichern unter, mit Tastenkürzeln) plus Buttons in der Kopfzeile.
- Beim Laden werden Klassen/Talente über die normalen `addClass`/`addFeatNoPrerequisitesCheck`-Wege wiederhergestellt (nicht direkt in die Liste geschrieben), damit Seiteneffekte wie Talent-Boni (Bonus-Aggregator) und "genau eine Erstklasse" weiterhin greifen. Per Standalone-Skript verifiziert: Speichern → Laden liefert identische Werte, inklusive korrekt neu angewandtem Toughness-Bonus auf die Max-TP.

## Nächste Schritte (priorisiert)

1. **Bonus-Aggregator erweitern** — bisher nur an TP-Max verankert. Als Nächstes: RK-Teilkomponenten (Rüstungs-/Schild-/Ablenkungsbonus etc. sollen ebenfalls Bonus-Ziele werden, nicht nur reine Eingabefelder) und der "Sonstiges"-Modifikator bei Rettungswürfen.
2. **Violation-Konzept auf weitere Bereiche ausweiten** — aktuell nur Talente. Kandidaten: Klassen (z. B. Multiklassen-Regeln), sobald mehr Regellogik existiert.
3. **Trefferpunkte/HP-Berechnung vertiefen** — TP-Basis ist noch ein reines manuelles Eingabefeld (kein Trefferwürfel-System, kein KO-Mod. pro Stufe). RK/Initiative/Zauberresistenz ebenso: alle Teilwerte (Rüstungsbonus, Naturrüstung, …) sind manuelle Felder ohne Ausrüstungs- oder Zaubersystem dahinter — bewusst so, siehe CLAUDE.md.
4. **Tests** für `AttributeCalculator`/`FeatCalculator`/`ClassRules`/`FeatRules`/`BonusCalculator`/`ArmorClassCalculator` — reine Funktionen, aktuell keine Testabdeckung, lohnt sich vor weiterem Ausbau der Regellogik.
5. **Kleinere offene Fäden** (siehe auch CLAUDE.md "Bekannte, bewusst unfertige Stellen"):
   - "verfügbar"-Feld bei Talenten zeigt nie etwas (nie verdrahtet).
   - "Talent durch Klasse gewährt"-Konzept fehlt komplett (bräuchte Talent-Slot-Buchhaltung: welcher gewählte Talent kam aus einem normalen Slot vs. einem Klassen-Bonusslot).
   - Rassen-Bonustalent (Menschen, Stufe 1) und Kämpfer-Bonustalentliste als eigene Filterregel fehlen noch.
   - Nur 4 Talente in `feats.yml` — Dateninhalt ist noch sehr dünn.
6. **Später, auf Rückstellung:**
   - Sprachumschalter DE/EN (echtes i18n-Setup nötig, aktueller Text noch zu instabil, um sich zu lohnen).
   - Windows-Portierung + natives macOS-App-Bundling zusammen als ein Paket: `jpackage`-Setup für beide Plattformen. Löst nebenbei auch, dass die Mac-Menüleiste aktuell "java" statt "Tome of Heroes" zeigt und es keine echten "Über …"/"Einstellungen…"-Einträge gibt — das kommt aus `CFBundleName`/Info.plist einer echten `.app`-Bundle und lässt sich im Dev-Modus (`mvn javafx:run`) nicht zuverlässig per JVM-Property fixen. "Ausblenden"/"Beenden" funktionieren schon automatisch, nur falsch beschriftet.

## Arbeitsweise, die sich bewährt hat

- Bei jeder FXML/Layout-Änderung: App über `mvn javafx:run` im Hintergrund starten und auf Fehler im Log prüfen, bevor der Nutzer es sieht.
- Änderungen werden erst nach ausdrücklicher Bestätigung committet, nicht automatisch.
- Der Nutzer bevorzugt klare, direkte Kritik/Rückfragen ("sieht bescheuert aus") — darauf mit echter Ursachenanalyse reagieren, nicht mit weiteren Trial-and-Error-Anpassungen.
- Neue Architekturkonzepte (z. B. Violation, Bonus-Aggregator) erst als kleinen, für sich nutzbaren ersten Schnitt bauen, dann erst verallgemeinern — nicht gleich die volle Abstraktion für alle künftigen Anwendungsfälle vorwegnehmen.
- Der Charakterbogen soll perspektivisch nicht nur zum Charakterbau, sondern auch fürs eigentliche Spielen taugen (z. B. TP aktuell vs. max) — bei neuen Werten prüfen, ob sie diese "Baustein vs. Spielzustand"-Unterscheidung auch brauchen.
