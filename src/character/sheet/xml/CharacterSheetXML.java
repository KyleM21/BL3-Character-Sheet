// File name: CharacterSheetXML.java
// Written by: Kyle Marcoux
// Description:  This is a character sheet for planning characters in a game called Borderlands 3!
// Revision History:
// Date:         		By:     Action:
// ---------------------------------------------------
/*      09/15/21                KM      Created the program
        09/16/21                KM      Planned layout, created the form in Scene Builder
        09/17/21 - 09/19/21     KM      Added functionality and bug tested
*/
package character.sheet.xml;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CharacterSheetXML extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        
        Parent root = FXMLLoader.load(getClass().getResource("FXMLDocument.fxml"));
        
        Scene scene = new Scene(root);
        stage.setTitle("Borderlands Character Sheet");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
}
