module tech.octopusdragon.pokemontypegame {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.desktop;
    requires uk.co.caprica.vlcj;


    opens tech.octopusdragon.pokemontypegame.gui to javafx.graphics, javafx.fxml;
    opens tech.octopusdragon.pokemontypegame to javafx.graphics, javafx.fxml;
    exports tech.octopusdragon.pokemontypegame;
}