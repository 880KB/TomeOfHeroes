package dev.swim.toh.model.core.savingthrows;

import dev.swim.toh.model.core.CoreBase;
import dev.swim.toh.model.data.savingthrow.SavingThrow;
import javafx.beans.property.IntegerProperty;

public class SavingThrows extends CoreBase<SavingThrowsInput, SavingThrowsComputed> {

    public SavingThrows() {
        input = new SavingThrowsInput();
        computed = new SavingThrowsComputed();
    }

    public IntegerProperty getSavingThrowTotalProperty(SavingThrow savingThrow) {
        return computed.getSavingThrowTotalProperty(savingThrow);
    }

    public IntegerProperty getSavingThrowBaseProperty(SavingThrow savingThrow) {
        return computed.getSavingThrowBaseProperty(savingThrow);
    }

    public IntegerProperty getSavingThrowAttributeModProperty(SavingThrow savingThrow) {
        return computed.getSavingThrowAttributeModProperty(savingThrow);
    }

    public IntegerProperty getSavingThrowMagicModProperty(SavingThrow savingThrow) {
        return computed.getSavingThrowMagicModProperty(savingThrow);
    }

    public IntegerProperty getSavingThrowMiscModProperty(SavingThrow savingThrow) {
        return input.getSavingThrowMiscModProperty(savingThrow);
    }
}
