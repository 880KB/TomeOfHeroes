module dev.swim.toh {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;


    opens dev.swim.toh.app to javafx.fxml;
    exports dev.swim.toh.app;

    opens dev.swim.toh.ui.controller to javafx.fxml;
    exports dev.swim.toh.ui.controller;
    opens dev.swim.toh.ui.controller.dialog to javafx.fxml;
    exports dev.swim.toh.ui.controller.dialog;

    exports dev.swim.toh.model.core;
    opens dev.swim.toh.model.core to javafx.fxml;
    exports dev.swim.toh.model.core.description;
    opens dev.swim.toh.model.core.description to javafx.fxml;
    exports dev.swim.toh.model.core.attributes;
    opens dev.swim.toh.model.core.attributes to javafx.fxml;
    exports dev.swim.toh.model.core.savingthrows;
    opens dev.swim.toh.model.core.savingthrows to javafx.fxml;
    exports dev.swim.toh.model.core.classes;
    opens dev.swim.toh.model.core.classes to javafx.fxml;
    exports dev.swim.toh.model.core.feats;
    opens dev.swim.toh.model.core.feats to javafx.fxml;
    exports dev.swim.toh.model.rules.feat.prerequisites;
    opens dev.swim.toh.model.rules.feat.prerequisites to javafx.fxml;
    exports dev.swim.toh.model.calculation;
    opens dev.swim.toh.model.calculation to javafx.fxml;

    exports dev.swim.toh.model.data.clazz;
    opens dev.swim.toh.model.data.clazz to javafx.fxml;
    exports dev.swim.toh.model.data.attribute;
    opens dev.swim.toh.model.data.attribute to javafx.fxml;
    exports dev.swim.toh.model.data.alignment;
    opens dev.swim.toh.model.data.alignment to javafx.fxml;
    exports dev.swim.toh.model.data.race;
    opens dev.swim.toh.model.data.race to javafx.fxml;
    exports dev.swim.toh.model.data.size;
    opens dev.swim.toh.model.data.size to javafx.fxml;
    exports dev.swim.toh.model.data.savingthrow;
    opens dev.swim.toh.model.data.savingthrow to javafx.fxml;
    exports dev.swim.toh.model.data.feat;
    opens dev.swim.toh.model.data.feat to javafx.fxml;
}