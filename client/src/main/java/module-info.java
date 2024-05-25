module org.example.client {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires okhttp3;

    opens org.example.client to javafx.fxml;
    exports org.example.client;
}