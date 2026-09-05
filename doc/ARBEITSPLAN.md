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

## Nächste Schritte (priorisiert)

1. **Persistenz (Speichern/Laden)** — aktuell geht jeder Charakter beim Schließen des Tabs/der App verloren. `jackson-databind` liegt schon ungenutzt in der `pom.xml`. Größter Hebel für tatsächliche Nutzbarkeit.
2. **Trefferpunkte (HP) & Rüstungsklasse (AC)** — fehlen im Modell komplett, die zwei grundlegendsten Kampfwerte. Vorschlag: erstmal ohne volles Ausrüstungssystem, mit manuellen Bonusfeldern (Muster wie der "Sonst."-Modifikator bei Rettungswürfen).
3. **Tests** für `AttributeCalculator`/`FeatCalculator`/`ClassRules`/`FeatRules` — reine Funktionen, aktuell keine Testabdeckung, lohnt sich vor weiterem Ausbau der Regellogik.
4. **Kleinere offene Fäden** (siehe auch CLAUDE.md "Bekannte, bewusst unfertige Stellen"):
   - Magie-Mod bei Rettungswürfen ist fest auf 0 (kein Ausrüstungs-/Zaubersystem dahinter).
   - "verfügbar"-Feld bei Talenten zeigt nie etwas (nie verdrahtet).
   - "Talent durch Klasse gewährt"-Konzept fehlt komplett (bräuchte Talent-Slot-Buchhaltung: welcher gewählte Talent kam aus einem normalen Slot vs. einem Klassen-Bonusslot).
   - Talentregeln aus `doc/rules-feats.md` nur teilweise umgesetzt: Rassen-Bonustalent (Menschen, Stufe 1), Kämpfer-Bonustalentliste als eigene Filterregel, tatsächliche mechanische Auswirkungen ("Benefits") eines Talents — aktuell nur Beschreibungstext, keine Wirkung.
   - Nur 4 Talente in `feats.yml` — Dateninhalt ist noch sehr dünn.
5. **Später, auf Rückstellung:** Sprachumschalter DE/EN (echtes i18n-Setup nötig, aktueller Text noch zu instabil, um sich zu lohnen).

## Arbeitsweise, die sich bewährt hat

- Bei jeder FXML/Layout-Änderung: App über `mvn javafx:run` im Hintergrund starten und auf Fehler im Log prüfen, bevor der Nutzer es sieht.
- Bei nicht direkt sichtbaren Layout-Bugs (Scrollbalken, Größenberechnung): lieber ein kleines Diagnose-Programm schreiben, das echte Werte misst (`Platform.runLater` + `Node.lookup(...)`), statt Pixelwerte zu raten oder mit Puffern zu arbeiten — Letzteres wurde vom Nutzer explizit und zu Recht abgelehnt.
- Änderungen werden erst nach ausdrücklicher Bestätigung committet, nicht automatisch.
- Der Nutzer bevorzugt klare, direkte Kritik/Rückfragen ("sieht bescheuert aus") — darauf mit echter Ursachenanalyse reagieren, nicht mit weiteren Trial-and-Error-Anpassungen.
