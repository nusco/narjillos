module org.nusco.narjillos {
    requires javafx.graphics;
    requires java.desktop;
    requires java.sql;
    requires com.google.gson;
    requires commons.cli;
    requires org.yaml.snakeyaml;

    // Export main app to JavaFX:
    exports org.nusco.narjillos.application to javafx.graphics;

    // Exports and openings to Gson (used for serialization)
    exports org.nusco.narjillos.core.utilities to com.google.gson;
    exports org.nusco.narjillos.core.geometry to com.google.gson;
    exports org.nusco.narjillos.core.chemistry to com.google.gson;
    exports org.nusco.narjillos.core.things to com.google.gson;
    opens org.nusco.narjillos.experiment to com.google.gson;
    opens org.nusco.narjillos.core.utilities to com.google.gson;
    opens org.nusco.narjillos.creature to com.google.gson;
    opens org.nusco.narjillos.creature.body to com.google.gson;
    opens org.nusco.narjillos.core.geometry to com.google.gson;
    opens org.nusco.narjillos.creature.body.pns to com.google.gson;
    opens org.nusco.narjillos.core.things to com.google.gson;
    opens org.nusco.narjillos.core.chemistry to com.google.gson;
    opens org.nusco.narjillos.experiment.environment to com.google.gson;
}