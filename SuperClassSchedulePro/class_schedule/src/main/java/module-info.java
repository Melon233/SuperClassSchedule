module com.bamboo {
    requires javafx.fxml;
    requires javafx.base;
    requires transitive javafx.controls;

    opens com.bamboo to javafx.fxml;
    opens com.bamboo.controller to javafx.fxml;
    opens com.bamboo.module to javafx.fxml;

    exports com.bamboo.module;
    exports com.bamboo.controller;
    exports com.bamboo;
}
