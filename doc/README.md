| Layer          | Zweck                                            | Beispiele bei dir                                          |
| -------------- | ------------------------------------------------ | ---------------------------------------------------------- |
| **Input**      | vom Benutzer gesetzt, direkt editierbar          | `DescriptionInput`, Attribute-Properties, Klassenlevel     |
| **Computed**   | abgeleitete Werte, automatisch aktualisiert      | `DescriptionComputed.sizeCategory`, Attribut-Modifikatoren |
| **Rules**      | Logik, die entscheidet, ob etwas erlaubt ist     | `ClassRules.canAddClass()`, `FeatRules`                    |
| **Calculator** | Logik, die Werte berechnet, z. B. Summen, Maxima | `FeatCalculator.getMaxClassFeats()`                        |

Rules entscheiden, was erlaubt ist.
Input verwaltet den Zustand.

| Klasse           | Aufgabe                 |
| ---------------- | ----------------------- |
| `FeatRepository` | **Was existiert?**      |
| `FeatRules`      | **Was ist erlaubt?**    |
| `FeatCalculator` | **Was wird berechnet?** |
| `FeatsInput`     | **Was wird geändert?**  |
