module dev.swim.toh {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.kordamp.ikonli.javafx;
    requires org.kordamp.ikonli.fontawesome5;
    requires spring.context;
    requires spring.beans;
    requires spring.boot;
    requires spring.boot.autoconfigure;

    // --- App Package ---
    opens dev.swim.toh.app to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.app;

    // --- Config Packages ---
    opens dev.swim.toh.definition.feat to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.definition.feat;

    // --- UI Controllers ---
    opens dev.swim.toh.ui.controller to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.ui.controller;
    opens dev.swim.toh.ui.controller.dialog to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.ui.controller.dialog;

    // --- Model Core ---
    opens dev.swim.toh.model.core to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.core;

    opens dev.swim.toh.model.core.description to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.core.description;

    opens dev.swim.toh.model.core.attributes to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.core.attributes;

    opens dev.swim.toh.model.core.savingthrows to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.core.savingthrows;

    opens dev.swim.toh.model.core.classes to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.core.classes;

    opens dev.swim.toh.model.core.feats to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.core.feats;

    opens dev.swim.toh.model.rules.feat.prerequisites to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.rules.feat.prerequisites;

    opens dev.swim.toh.model.calculation to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.calculation;

    // --- Data Packages ---
    opens dev.swim.toh.model.data.clazz to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.data.clazz;

    opens dev.swim.toh.model.data.attribute to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.data.attribute;

    opens dev.swim.toh.model.data.alignment to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.data.alignment;

    opens dev.swim.toh.model.data.race to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.data.race;

    opens dev.swim.toh.model.data.size to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.data.size;

    opens dev.swim.toh.model.data.savingthrow to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.data.savingthrow;

    opens dev.swim.toh.model.data.feat to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.data.feat;

    // --- Rules Packages ---
    opens dev.swim.toh.model.rules.feat to javafx.fxml, spring.core, spring.beans;
    exports dev.swim.toh.model.rules.feat;
}
