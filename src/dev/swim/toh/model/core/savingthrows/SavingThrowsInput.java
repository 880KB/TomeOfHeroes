package dev.swim.toh.model.core.savingthrows;

import dev.swim.toh.model.core.InputBase;
import dev.swim.toh.model.data.savingthrow.SavingThrow;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.Map;

public class SavingThrowsInput extends InputBase {

    private final Map<SavingThrow, IntegerProperty> savingThrowMiscModPropertyMap;

    public SavingThrowsInput() {
        savingThrowMiscModPropertyMap = Map.of(
                SavingThrow.REFLEX, new SimpleIntegerProperty(0),
                SavingThrow.WILL, new SimpleIntegerProperty(0),
                SavingThrow.FORTITUDE, new SimpleIntegerProperty(0)
        );
    }

    public IntegerProperty getSavingThrowMiscModProperty(SavingThrow savingThrow) {
        return savingThrowMiscModPropertyMap.get(savingThrow);
    }
}
