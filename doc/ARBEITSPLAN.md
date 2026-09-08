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

**Talente: deutsche PHB-Daten, Auswahldialog-Redesign, Talent-Slot-Pools** (`e5482ce`, `d123c6e`, `b554bab`, `5d069c5`)
- `feats.yml` von 4 auf 74 Talente aus dem PHB erweitert (Kurzbeschreibungen vorhanden, volle Beschreibungen für die meisten noch offen, siehe unten).
- Talent-Voraussetzungen nach `model.data.prerequisites` verschoben, eigene Typen je Voraussetzungsart (`AttributePrerequisite`, `ClassLevelPrerequisite`, `CharacterLevelPrerequisite`, `BaseAttackBonusPrerequisite`, `FeatPrerequisite`), plus bewusste Platzhalter `CasterLevelPrerequisite`/`SkillPrerequisite`/`ProficiencyWithWeaponPrerequisite`, die mangels Zauber-/Fertigkeitssystem immer `true` liefern, aber im Auswahldialog trotzdem sichtbar sind.
- Auswahldialog zeigt jetzt an, *warum* ein Talent nicht wählbar ist (Verfügbarkeits-Spalte + Begründung), und gruppiert Talente, die auf einem anderen aufbauen, eingerückt darunter.
- Talent-Slot-Pools (Allg./Kämpfer-Bonus/Magier-Bonus/Rassen-Bonus) mit fester, einmaliger Zuordnung bei Auswahl: `FeatSlotCalculator.allocate()` (reine Funktion, feste Priorität Kämpfer→Magier→Rasse→Allg.) + `FeatsComputed.assignPool()` (Freeze-Punkt), `SelectedFeat.pool` als `Input`-Historienwert. Formeln (`FeatCalculator`) gegen die PHB-Tabellen geprüft: Allg. `1+Stufe/3`, Kämpfer `1+KämpferStufe/2`, Magier `MagierStufe/5`, Mensch `1` — alle korrekt.
- Automatische Magier-Freischriftrolle (`AutomaticFeatGrants`) — wird bei Erreichen der Magier-Klasse vergeben, beim Entfernen wieder entzogen, zählt nicht gegen den Bonus-Pool, überlebt Speichern/Laden korrekt.
- Regressionstest `FeatPoolFreezeTest`: deckt sowohl das Einfrieren (Kleriker→Magier-Multiklasse darf ein bereits gewähltes Talent nicht rückwirkend umkategorisieren) als auch den Automatik-Rundtrip über Speichern/Laden inkl. Aufräumen ab. `FeatCalculatorTest`/`FeatSlotCalculatorTest` decken die reinen Formeln/die Zuteilungslogik ab.
- Statusfeld-Überhang bei Kämpfer-/Magier-/Rassen-Bonus-Pool: entfernt man z. B. die Magier-Klasse, nachdem ein Magier-Bonustalent schon eingefroren wurde, bleibt das Talent bestehen (frozen-history-Prinzip, kein automatisches Löschen) und das Statusfeld bekommt jetzt wie der Allg.-Pool die `field-exceeded`-Rotmarkierung (`FeatsViewController.bindExceeded`/`bindPoolOpacity`) statt einfach nur auf 0.35 Opazität wegzublenden.

**Waffenkatalog (3a)**
- `weapons.yml` (67 Waffen) → `WeaponDefinition` (`@ConfigurationProperties`) → `WeaponMapper` → `Weapon` → `WeaponRepository`, strukturell wie Talente.
- **Wichtiger Quellenfund unterwegs:** Die ursprünglich für die Waffentabelle genutzte PDF ("Kapitel 7 - Ausrüstung (Seiten 117-140).pdf", Tabelle 7-4, Gruppierung nach Sehr klein/Klein/Mittel/Groß mit einer Schadensspalte) ist strukturell die **D&D-3.0-Tabelle**, nicht 3.5. Die tatsächlich verwendete Quelle ist jetzt `doc/datenquellen/Waffen.pdf` (Tabelle 7-5, korrektes 3.5-Format: getrennte Schaden(Klein)/Schaden(Mittel)-Spalten, Gruppierung nach Leicht-/Einhand-/Zweihandwaffen statt absoluter Waffengröße, Fußnoten 1-5 exakt wie im echten SRW). **Offener Verdacht:** "Kapitel 5 - Talente (Seiten 95-106).pdf" (Quelle für alle 74 Talente in `feats.yml`) stammt nach Namensmuster vermutlich aus demselben (3.0er) Buch wie die alte Kapitel-7-PDF — noch nicht verifiziert, ob die Talente dadurch auch 3.0- statt 3.5-Inhalt sind. Siehe Punkt 4 unten.
- Datenmodell dadurch anders als ursprünglich im Konzept skizziert: kein `Size`-Feld (Waffengröße existiert in 3.5 nicht als Waffeneigenschaft) - stattdessen `handedness` (LIGHT/ONE_HANDED/TWO_HANDED, direkt aus der Tabelle, nur bei Nahkampfwaffen), `damageSmall`/`damageMedium` (+ `secondaryDamage*` bei Doppelwaffen) getrennt für kleine/mittelgroße Charaktere statt einem Wert, `damageTypeChoice` (true = Spieler wählt Schadensart beim Angriff = "oder", false = beide gleichzeitig = "und"), `reach` als eigenes Flag (Fußnote 4). Gewicht bleibt der Mittelgroß-Basiswert (Halbierung/Verdopplung nach Größe ist eine spätere Rechenfunktion, keine gespeicherten Daten).
- Bewusst ausgelassen: Munition (kein eigener Schaden), Netz (kein regulärer Schaden, passt nicht ins Modell), Schilde/Rüstungsstacheln (eigener RK-Ausrüstungsslot, keine Waffe), Preis (kein Anwendungsfall ohne Kauflogik).
- Ein Sonderfall bewusst vereinfacht: Gnomischer Hakenhammer hat laut Tabelle unterschiedliche Kritisch-Werte pro Waffenkopf (x3/x4) - Modell kennt nur einen Wert pro Waffe, Differenz steht in `description`.

**Waffen-Slots / "Angriff"-Karte (3b)**
- `dev.swim.toh.model.core.weapons`: `WeaponSlot`-Enum (`MAIN_HAND`/`OFF_HAND`/`RANGED`, fest, keine dynamische Liste — auf Nutzerwunsch, da der Papierbogen zwar generische wiederholte Angriffs-Boxen hat, hier aber bewusst drei benannte Slots gewählt wurden), `SelectedWeaponSlot` (Waffe + Anmerkung pro Slot), `WeaponsInput`/`WeaponsComputed`/`Weapons` strukturell wie bei Talenten, aber ohne die Pool-Freeze-Maschinerie (nicht nötig bei fest benannten 1:1-Slots statt konkurrierender Pools).
- Zweihändige Haupthand-Waffe sperrt/leert automatisch die Nebenhand (`WeaponsComputed`, reaktiv, inkl. sofortiger Erstberechnung nach der `ComputedBase`-Regel).
- Neue Karte `WeaponsViewController`/`weapons-view.fxml`: drei feste Zeilen (keine Add/Remove-Tabelle, Slots werden befüllt/geleert), Spalten wie auf dem Papierbogen (Slot, Waffe, Schaden, Krit., Reichweite, Art, Anmerkung), Angriffsbonus als sichtbarer, bewusst leerer Platzhalter (kein Grund-Angriffsbonus-System vorhanden, analog zum Magie-Mod-Platzhalter bei Rettungswürfen). Waffenauswahl weiterhin über den bestehenden `SelectionDialogController<Weapon>`, gefiltert nach `rangeType` (Haupt-/Nebenhand: Nahkampf, Fernkampf: Fernkampf) — der Dialog ist eigentlich Mehrfachauswahl, wird hier aber pro Slot einzeln geöffnet und nimmt nur den ersten angehakten Wert.
- Waffen kommen vorerst direkt aus dem vollen Katalog (`WeaponRepository`), nicht aus einem Inventar — das Inventar-Konzept ist bewusst zurückgestellt, siehe "Nächste Schritte" Punkt 8.
- Persistenz erweitert (`CharacterData`/`CharacterDataMapper`, Schema-Version 2→3): pro Slot Waffen-Id (null wenn leer) + Anmerkung.
- `CharacterModel`/`CharacterModelFactory` brauchen jetzt zusätzlich eine `WeaponRepository` (analog zu `FeatRepository`/`FeatRules`).

## Nächste Schritte (priorisiert)

1. **Bonus-Aggregator erweitern** — bisher nur an TP-Max verankert. Als Nächstes: RK-Teilkomponenten (Rüstungs-/Schild-/Ablenkungsbonus etc. sollen ebenfalls Bonus-Ziele werden, nicht nur reine Eingabefelder) und der "Sonstiges"-Modifikator bei Rettungswürfen.
2. **Violation-Konzept auf weitere Bereiche ausweiten** — aktuell nur Talente. Kandidaten: Klassen (z. B. Multiklassen-Regeln), sobald mehr Regellogik existiert.
3. **Talent-Optionen ("welche Waffe/Schule/Fertigkeit")** — `FeatRules.canAdd` hat noch `// TODO: add feat options`, `SelectedFeat` nur einen Options-Platzhalterkommentar. Betrifft 9 `repeatType: MULTIPLE`-Talente (Waffenfokus, Waffenspezialisierung, Zauberfokus, Zaubermeisterschaft, Fertigkeitsfokus, Umgang mit exotischen/Kriegswaffen, Verbesserter Kritischer Treffer, Waffenfinesse): aktuell nirgends gespeichert, *welche* Wahl hinter einer Mehrfachauswahl steckt, dadurch auch keine Prüfung von Voraussetzungsketten, die genau diese Wahl referenzieren (z. B. Waffenspezialisierung braucht Waffenfokus *in derselben Waffe*). Konzept (Session vom 2026-09-07), in drei Schritten:
   - **3a. Waffenkatalog — erledigt**, siehe "Erledigt" oben (Datenmodell wich vom ursprünglichen Konzept-Entwurf ab, siehe dortige Erklärung: `handedness` statt `size`, getrennter Klein-/Mittel-Schaden).
   - **3b. "Angriff"-Karte — erledigt**, siehe "Erledigt" oben. Anders als hier ursprünglich skizziert: keine offene Liste, sondern drei feste, benannte Slots (Haupt-/Nebenhand/Fernkampf), da Waffen künftig aus einem Inventar kommen sollen (noch nicht gebaut, siehe Punkt 8) statt frei aus dem Katalog gewählt zu werden.
   - **3c. Talent-Optionen selbst** — `SelectionItem<T>` bekommt ein optionales Options-Feld (Kandidatenliste + `selectedOption`-Property), nur befüllt bei Talenten mit `requiresOption` (neues `FeatDefinition`-Feld). Namensspalte im Auswahldialog zeigt dann zusätzlich eine ComboBox statt eines zweiten Dialog-Schritts. Kandidatenliste wird beim Öffnen einmalig passend vorgefiltert, dadurch keine Live-Prüfung nötig: unabhängige Talente (Waffenfokus, Waffenfinesse, Verbesserter Kritischer Treffer) sehen den ganzen Katalog (bzw. bei "Umgang mit exotischen/Kriegswaffen" nur die passende Kompetenz-Kategorie, abzüglich schon kompetenter Waffen); abhängige Talente (Waffenspezialisierung) sehen nur Waffen, für die bereits ein `SelectedFeat` mit Waffenfokus existiert. `SelectedFeat` bekommt dafür ein `FeatOption`-Feld (erster konkreter Typ `WeaponFeatOption`), `FeatPrerequisite` eine Variante "dasselbe Talent mit derselben Option". **Bekannte, akzeptierte Einschränkung:** Waffenfokus und ein davon abhängiges Talent (Waffenspezialisierung) für dieselbe Waffe in *einer* Dialog-Sitzung wählen geht nicht, weil die Kandidatenliste beim Öffnen nur den bereits committeten Zustand kennt (z. B. Kämpfer Stufe 6, Allg.- und Bonustalent gleichzeitig) — Waffenfokus muss erst per OK bestätigt werden, dann den Dialog erneut öffnen für Waffenspezialisierung. Bewusst nicht gelöst (bräuchte Live-Abhängigkeitsverfolgung zwischen Dialogzeilen), da selten und über zwei Dialog-Durchläufe ohnehin funktioniert.
   - Nicht Teil dieser drei Schritte: automatische Waffenkompetenz nach Klasse (eigene Tabelle aus dem Klassen-Kapitel, `ProficiencyWithWeaponPrerequisite` bleibt bis dahin Platzhalter), Angriffsbonus-Berechnung (`BaseAttackBonusPrerequisite` ist ebenfalls noch Platzhalter, braucht Grund-Angriffsbonus nach Klasse/Stufe), Wurf-/Schusswaffen-Sonderregel beim Schadensbonus, Optionen für die 4 nicht-Waffen-Talente (Fertigkeitsfokus/Zauberfokus/Zaubermeisterschaft — warten auf ein Fertigkeiten-/Zaubersystem, wie die bestehenden Platzhalter-Voraussetzungen).
4. **`feats.yml` gegen 3.5 statt 3.0 gegenprüfen** — Entscheidung getroffen (2026-09-07, siehe CLAUDE.md "Regelwerk-Basis"): der Editor basiert einheitlich auf 3.5, nicht 3.0. Beim Waffenkatalog (3a) stellte sich heraus, dass "Kapitel 7 - Ausrüstung (Seiten 117-140).pdf" strukturell die 3.0er-Waffentabelle ist (siehe "Erledigt" oben) und `feats.yml` (Quelle: "Kapitel 5 - Talente (Seiten 95-106).pdf") dem Namensmuster nach vermutlich aus demselben 3.0er-Buch stammt. Zu klären: ob und wo sich das tatsächlich auf die 74 Talente auswirkt (Stichprobe z. B. über die genauen Bonusfeat-Listen, die zwischen 3.0 und 3.5 abweichen) und ggf. einzelne Talente/Voraussetzungen auf 3.5-Stand nachziehen — kein Rewrite, aber vor weiterer Feinarbeit an Talenten wichtiger als jede einzelne Detailfrage unten.
5. **Zwei Kämpfer-Bonustalent-Grenzfälle gegen das PHB prüfen**: `GESCHOSSE_ABWEHREN`/`NIEDERREITEN` sind (noch) nicht mit `bonusFeatClasses: [FIGHTER]` getaggt — unklar, ob zu Recht.
6. **Tests** für `AttributeCalculator`/`ClassRules`/`BonusCalculator`/`ArmorClassCalculator` — reine Funktionen, aktuell keine Testabdeckung (Talente sind bereits über `FeatCalculatorTest`/`FeatSlotCalculatorTest`/`FeatPoolFreezeTest` abgedeckt).
7. **Trefferpunkte/HP-Berechnung vertiefen** — TP-Basis ist noch ein reines manuelles Eingabefeld (kein Trefferwürfel-System, kein KO-Mod. pro Stufe). RK/Initiative/Zauberresistenz ebenso: alle Teilwerte (Rüstungsbonus, Naturrüstung, …) sind manuelle Felder ohne Ausrüstungs- oder Zaubersystem dahinter — bewusst so, siehe CLAUDE.md.
8. **Inventar-System** — die neue "Angriff"-Karte (siehe "Erledigt": Waffen-Slots) wählt Waffen aktuell direkt aus dem vollen Waffenkatalog. Auf Nutzerwunsch (Session vom 2026-09-07) soll das perspektivisch stattdessen aus einem echten, pro Charakter geführten Inventar kommen (was der Charakter tatsächlich besitzt, wovon nur ein Teil aktiv geführt wird) — bewusst zurückgestellt, da Ausrüstung/Inventar aktuell komplett fehlt (siehe CLAUDE.md) und das ein eigener, größerer Baustein ist, kein Anhängsel an die Waffen-Slots.
9. **Später, auf Rückstellung:**
   - Platzhalter-Voraussetzungen (`CasterLevelPrerequisite`/`SkillPrerequisite`/`ProficiencyWithWeaponPrerequisite`) bleiben bewusst offen, bis es ein Zauber-/Fertigkeitssystem gibt.
   - Volle Beschreibungstexte für die meisten der 74 Talente in `feats.yml` — reine Fleißarbeit, keine Architekturfrage.
   - Edit/Play-Modus-Schalter: zentraler Umschalter, der im Play-Modus Editier-Buttons (Klasse/Talent hinzufügen/löschen, Attribute ändern, ...) ausblendet und nur spielrelevante Felder (Ausrüstung, aktuelle TP, verbrauchte Zauberplätze, ...) editierbar lässt. Passt zur bestehenden Input/Computed-Schichtung, braucht aber eine zusätzliche, orthogonale Markierung pro Feld/Button ("play-editable" ja/nein) — die Menge "Input" und die Menge "im Play-Modus editierbar" sind nicht deckungsgleich (Ausrüstung ist Input *und* play-editierbar, Attribute sind Input aber *nicht* play-editierbar). Technisch unproblematisch (ein `editModeProperty`, gebunden an `visibleProperty()`/`disableProperty()` je Controller), aber Fleißarbeit, weil jeder bestehende Button/jedes Feld einmal klassifiziert werden muss. Erster Schnitt: Schalter + ein, zwei Karten, nicht sofort alle Controller.
   - Sprachumschalter DE/EN (echtes i18n-Setup nötig, aktueller Text noch zu instabil, um sich zu lohnen).
   - Windows-Portierung + natives macOS-App-Bundling zusammen als ein Paket: `jpackage`-Setup für beide Plattformen. Löst nebenbei auch, dass die Mac-Menüleiste aktuell "java" statt "Tome of Heroes" zeigt und es keine echten "Über …"/"Einstellungen…"-Einträge gibt — das kommt aus `CFBundleName`/Info.plist einer echten `.app`-Bundle und lässt sich im Dev-Modus (`mvn javafx:run`) nicht zuverlässig per JVM-Property fixen. "Ausblenden"/"Beenden" funktionieren schon automatisch, nur falsch beschriftet.

## Arbeitsweise, die sich bewährt hat

- Bei jeder FXML/Layout-Änderung: App über `mvn javafx:run` im Hintergrund starten und auf Fehler im Log prüfen, bevor der Nutzer es sieht.
- Änderungen werden erst nach ausdrücklicher Bestätigung committet, nicht automatisch.
- Der Nutzer bevorzugt klare, direkte Kritik/Rückfragen ("sieht bescheuert aus") — darauf mit echter Ursachenanalyse reagieren, nicht mit weiteren Trial-and-Error-Anpassungen.
- Neue Architekturkonzepte (z. B. Violation, Bonus-Aggregator) erst als kleinen, für sich nutzbaren ersten Schnitt bauen, dann erst verallgemeinern — nicht gleich die volle Abstraktion für alle künftigen Anwendungsfälle vorwegnehmen.
- Der Charakterbogen soll perspektivisch nicht nur zum Charakterbau, sondern auch fürs eigentliche Spielen taugen (z. B. TP aktuell vs. max) — bei neuen Werten prüfen, ob sie diese "Baustein vs. Spielzustand"-Unterscheidung auch brauchen.
