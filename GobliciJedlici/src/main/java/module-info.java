module com.example.goblicijedlici {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.example.goblicijedlici to javafx.fxml;
    exports com.example.goblicijedlici;
}