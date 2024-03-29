// File name: FXMLDocumentController.java
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import static java.lang.Boolean.parseBoolean;
import static java.lang.Integer.parseInt;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

public class FXMLDocumentController implements Initializable {

    // Initializing all of my FXML variables
    @FXML
    private TextField name;
    @FXML
    private TextField buildname;
    @FXML
    private ComboBox classname;
    @FXML
    private Spinner level;
    @FXML
    private Spinner baRank;

    @FXML
    private TextField gun1;
    @FXML
    private Label gun1Label;
    @FXML
    private TextField gun2;
    @FXML
    private Label gun2Label;
    @FXML
    private TextField gun3;
    @FXML
    private Label gun3Label;
    @FXML
    private TextField gun4;
    @FXML
    private Label gun4Label;

    @FXML
    private TextField grenade;
    @FXML
    private Label grenadeLabel;
    @FXML
    private TextField artifact;
    @FXML
    private Label artifactLabel;
    @FXML
    private TextField classmod;
    @FXML
    private Label classmodLabel;
    @FXML
    private TextField shield;
    @FXML
    private Label shieldLabel;
    @FXML
    private Spinner legend;
    @FXML
    private Label legendLabel;

    @FXML
    private TextArea skillsGreen;
    @FXML
    private TextArea skillsBlue;
    @FXML
    private TextArea skillsRed;
    @FXML
    private TextArea skillsPurple;

    //@FXML
    //private ProgressBar progressBar;

    @FXML
    private ImageView classImg;

    // Creates filechooser for later use
    FileChooser fileChooser = new FileChooser();

    // Declares boolean variables for label color changing
    boolean g1,g2,g3,g4,a,cm,gr,sh,lg;

    // Declares styles for easier to read color changing code in the handleLegendAction function
    String defCSS = "-fx-background-color: white;"
                    + " -fx-border-color: black;"
                    + " -fx-border-width: 1;"
                    + " -fx-padding: 4;"
                    + " -fx-border-radius: 4;"
                    + " -fx-background-radius: 5;";

    String legendCSS = "-fx-background-color: #fad487;"
                    + " -fx-border-color: black;"
                    + " -fx-border-width: 1;"
                    + " -fx-padding: 4;"
                    + " -fx-border-radius: 4;"
                    + " -fx-background-radius: 5;";

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        // Initalizes booleans as false
        g1 = g2 = g3 = g4 = a = cm = gr = sh = lg = false;

        // Initialize spinners and combobox
        initSpinner();
        initCombo();

        // Attempts to get the filepath of the program to use in saving/loading data
        try {
            String path = new File(".").getCanonicalPath();
            fileChooser.setInitialDirectory(new File(path));
        } catch (IOException ex) {
            Logger.getLogger(FXMLDocumentController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    // Initializes spinners to have a valueFactory, and sets their min/max value
    // It also adds a listener to the spinners to ensure the values are commited
    // to the field even if the user does not hit enter.
    private void initSpinner() {
        level.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 72));
        level.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                level.increment(0); // incrementing the spinner by 0 will force it to update if the user does not hit enter
            }
        });
        baRank.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 10000));
        baRank.focusedProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue) {
                baRank.increment(0); // incrementing the spinner by 0 will force it to update if the user does not hit enter
            }
        });
        legend.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 8));
        legend.getStyleClass().clear();
    }

    // Initialies the combobox's options
    private void initCombo() {
        classname.getItems().addAll(
            "Amara",
            "FL4K",
            "Moze",
            "Zane"
        );
    }

    // This function handles the act of saving the program, it creates a file chooser window and presents the user with a save dialog
    @FXML
    private void handleSaveAction(ActionEvent event){

        Window stage = name.getScene().getWindow();
        fileChooser.setTitle("Save Dialog");
        fileChooser.setInitialFileName("BL3Sheet");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML File", "*.xml"));
        File file = fileChooser.showSaveDialog(stage);

        // If the user does which to save a file, the form will be compiled by compileText() and sent to saveFile() to be written
        if(file != null){
            saveFile(compileText(), file);
        }

    }

    // Compiles the form into an ArrayList<String> to be sent to saveFile()
    private ArrayList compileText(){

        ArrayList<String> x = new ArrayList<>();

        // I tried to seperate these statements by how the form is divided!
        x.add(" <name>" + name.getText() + "</name>");
        x.add(" <build>" + buildname.getText() + "</build>");
        x.add(" <classname>" + (String) classname.getValue() + "</classname>");
        x.add(" <level>" + String.valueOf(level.getValue()) + "</level>");
        x.add(" <barank>" + String.valueOf(baRank.getValue()) + "</barank>");

        x.add(" <gun1>" + gun1.getText() + "</gun1>");
        x.add(" <gun2>" + gun2.getText() + "</gun2>");
        x.add(" <gun3>" + gun3.getText() + "</gun3>");
        x.add(" <gun4>" + gun4.getText() + "</gun4>");
        x.add(" <grenade>" + grenade.getText() + "</grenade>");
        x.add(" <artifact>" + artifact.getText() + "</artifact>");
        x.add(" <classmod>" + classmod.getText() + "</classmod>");
        x.add(" <shield>" + shield.getText() + "</shield>");
        x.add(" <legends>" + String.valueOf(legend.getValue()) + "</legends>");

        x.add(" <skillsGreen>" + skillsGreen.getText() + "</skillsGreen>");
        x.add(" <skillsBlue>" + skillsBlue.getText() + "</skillsBlue>");
        x.add(" <skillsRed>" + skillsRed.getText() + "</skillsRed>");
        x.add(" <skillsPurple>" + skillsPurple.getText() + "</skillsPurple>");

        x.add(" <g1>" + g1 + "</g1>");
        x.add(" <g2>" + g2 + "</g2>");
        x.add(" <g3>" + g3 + "</g3>");
        x.add(" <g4>" + g4 + "</g4>");
        x.add(" <a>" + a + "</a>");
        x.add(" <cm>" + cm + "</cm>");
        x.add(" <gr>" + gr + "</gr>");
        x.add(" <sh>" + sh + "</sh>");
        x.add(" <lg>" + lg + "</lg>");

        return x;
    }

    // This function takes the compiled array and writes it on an xml file
    private void saveFile(ArrayList content, File file){
        try {
            PrintWriter writer;
            writer = new PrintWriter(file);
            writer.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + "\n" + "<sheet>" + "\n" + "<class>");
            for(int i=0; i<content.size(); i++){
                writer.println(content.get(i));
            }
            writer.println("</class>" + "\n" + "</sheet>");
            writer.close();
        }
        catch (IOException ex) {

        }
    }

    // This function handles the act of loading the file, bringing up the dialogue and sending the file to load to loadFile()
    @FXML
    private void handleLoadAction(ActionEvent event) throws IOException, FileNotFoundException, ParserConfigurationException, SAXException{
        Window stage = name.getScene().getWindow();
        fileChooser.setTitle("Open Dialog");
        fileChooser.setInitialFileName("BL3Sheet");
        fileChooser.getExtensionFilters().addAll(new FileChooser.ExtensionFilter("XML File", "*.xml"));
        File file = fileChooser.showOpenDialog(stage);

        if(file != null){
            loadFile(file);
        }
    }

    // This uses DocumentBuilder to parse the .xml file and fills the form out with its elements
    private void loadFile(File file) throws FileNotFoundException, IOException, ParserConfigurationException, SAXException{
        DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document dom = (Document) builder.parse(file);
        Element root = dom.getDocumentElement();

        name.setText(root.getElementsByTagName("name").item(0).getTextContent());
        buildname.setText(root.getElementsByTagName("build").item(0).getTextContent());
        classname.setValue(root.getElementsByTagName("classname").item(0).getTextContent());
        classImg.setImage(new Image("file:src/character/sheet/xml/IMG/" + (String) classname.getValue() + ".png"));
        level.getValueFactory().setValue(parseInt(root.getElementsByTagName("level").item(0).getTextContent()));
        baRank.getValueFactory().setValue(parseInt(root.getElementsByTagName("barank").item(0).getTextContent()));
        legend.getValueFactory().setValue(parseInt(root.getElementsByTagName("legends").item(0).getTextContent()));

        gun1.setText(root.getElementsByTagName("gun1").item(0).getTextContent());
        gun2.setText(root.getElementsByTagName("gun2").item(0).getTextContent());
        gun3.setText(root.getElementsByTagName("gun3").item(0).getTextContent());
        gun4.setText(root.getElementsByTagName("gun4").item(0).getTextContent());

        grenade.setText(root.getElementsByTagName("grenade").item(0).getTextContent());
        artifact.setText(root.getElementsByTagName("artifact").item(0).getTextContent());
        classmod.setText(root.getElementsByTagName("classmod").item(0).getTextContent());
        shield.setText(root.getElementsByTagName("shield").item(0).getTextContent());

        skillsGreen.setText(root.getElementsByTagName("skillsGreen").item(0).getTextContent());
        skillsBlue.setText(root.getElementsByTagName("skillsBlue").item(0).getTextContent());
        skillsRed.setText(root.getElementsByTagName("skillsRed").item(0).getTextContent());
        skillsPurple.setText(root.getElementsByTagName("skillsPurple").item(0).getTextContent());

        // This just checks if any of the inventory items are legendary, and marks them accordingly
        g1 = parseBoolean(root.getElementsByTagName("g1").item(0).getTextContent());
        if(g1){gun1Label.setStyle(legendCSS);}
        else{gun1Label.setStyle(defCSS);}
        g2 = parseBoolean(root.getElementsByTagName("g2").item(0).getTextContent());
        if(g2){gun2Label.setStyle(legendCSS);}
        else{gun2Label.setStyle(defCSS);}
        g3 = parseBoolean(root.getElementsByTagName("g3").item(0).getTextContent());
        if(g3){gun3Label.setStyle(legendCSS);}
        else{gun3Label.setStyle(defCSS);}
        g4 = parseBoolean(root.getElementsByTagName("g4").item(0).getTextContent());
        if(g4){gun4Label.setStyle(legendCSS);}
        else{gun4Label.setStyle(defCSS);}
        a = parseBoolean(root.getElementsByTagName("a").item(0).getTextContent());
        if(a){artifactLabel.setStyle(legendCSS);}
        else{artifactLabel.setStyle(defCSS);}
        cm = parseBoolean(root.getElementsByTagName("cm").item(0).getTextContent());
        if(cm){classmodLabel.setStyle(legendCSS);}
        else{classmodLabel.setStyle(defCSS);}
        gr = parseBoolean(root.getElementsByTagName("gr").item(0).getTextContent());
        if(gr){grenadeLabel.setStyle(legendCSS);}
        else{grenadeLabel.setStyle(defCSS);}
        sh = parseBoolean(root.getElementsByTagName("sh").item(0).getTextContent());
        if(sh){shieldLabel.setStyle(legendCSS);}
        else{shieldLabel.setStyle(defCSS);}
        lg = parseBoolean(root.getElementsByTagName("lg").item(0).getTextContent());
        if(lg){legendLabel.setStyle(legendCSS);}
        else{legendLabel.setStyle(defCSS);}
    }

    // This is just a listener to change the image to the selected class
    @FXML
    private void handleImageAction(ActionEvent event){
        classImg.setImage(new Image("file:src/character/sheet/xml/IMG/" + (String) classname.getValue() + ".png"));
    }

    // handleClearAction resets everything to default: text, images, legendary status and handles the CSS styling to make it default again
    @FXML
    private void handleClearAction(ActionEvent event){
        name.setText("");
        buildname.setText("");
        classname.setValue("Class");
        level.getValueFactory().setValue(0);
        baRank.getValueFactory().setValue(0);
        classImg.setImage(new Image("file:src/character/sheet/xml/IMG/BL3.png"));

        gun1.setText("");
        gun2.setText("");
        gun3.setText("");
        gun4.setText("");

        grenade.setText("");
        artifact.setText("");
        classmod.setText("");
        shield.setText("");
        legend.getValueFactory().setValue(0);

        skillsGreen.setText("");
        skillsBlue.setText("");
        skillsRed.setText("");
        skillsPurple.setText("");

        g1 = g2 = g3 = g4 = a = cm = gr = sh = false;
        gun1Label.setStyle(defCSS);
        gun2Label.setStyle(defCSS);
        gun3Label.setStyle(defCSS);
        gun4Label.setStyle(defCSS);
        artifactLabel.setStyle(defCSS);
        classmodLabel.setStyle(defCSS);
        grenadeLabel.setStyle(defCSS);
        shieldLabel.setStyle(defCSS);
    }

    // I'll admit this is very lengthy, but what this does is very simple!
    // This just checks to see which label was pressed for the legendary toggle using a switch,
    // then increments/decrements the legendary total and styles it accordingly
    @FXML
    private void handleLegendAction(ActionEvent event){
        Button btn = (Button) event.getSource();

        switch(btn.getId()){
            case "gun1Btn":
                if(g1){
                    gun1Label.setStyle(defCSS);
                    legend.decrement();
                    g1 = false;
                }
                else{
                    gun1Label.setStyle(legendCSS);
                    legend.increment();
                    g1 = true;
                }
                break;
            case "gun2Btn":
                if(g2){
                    gun2Label.setStyle(defCSS);
                    legend.decrement();
                    g2 = false;
                }
                else{
                    gun2Label.setStyle(legendCSS);
                    legend.increment();
                    g2 = true;
                }
                break;
            case "gun3Btn":
                if(g3){
                    gun3Label.setStyle(defCSS);
                    legend.decrement();
                    g3 = false;
                }
                else{
                    gun3Label.setStyle(legendCSS);
                    legend.increment();
                    g3 = true;
                }
                break;
            case "gun4Btn":
                if(g4){
                    gun4Label.setStyle(defCSS);
                    legend.decrement();
                    g4 = false;
                }
                else{
                    gun4Label.setStyle(legendCSS);
                    legend.increment();
                    g4 = true;
                }
                break;
            case "artifactBtn":
                if(a ){
                    artifactLabel.setStyle(defCSS);
                    legend.decrement();
                    a = false;
                }
                else{
                    artifactLabel.setStyle(legendCSS);
                    legend.increment();
                    a = true;
                }
                break;
            case "classmodBtn":
                if(cm){
                    classmodLabel.setStyle(defCSS);
                    legend.decrement();
                    cm = false;
                }
                else{
                   classmodLabel.setStyle(legendCSS);
                    legend.increment();
                    cm = true;
                }
                break;
            case "grenadeBtn":
                if(gr){
                    grenadeLabel.setStyle(defCSS);
                    legend.decrement();
                    gr = false;
                }
                else{
                    grenadeLabel.setStyle(legendCSS);
                    legend.increment();
                    gr = true;
                }
                break;
            case "shieldBtn":
                if(sh){
                    shieldLabel.setStyle(defCSS);
                    legend.decrement();
                    sh = false;
                }
                else{
                    shieldLabel.setStyle(legendCSS);
                    legend.increment();
                    sh = true;
                }
                break;
            case "legendBtn":
                if(sh){
                    legendLabel.setStyle(defCSS);
                    sh = false;
                }
                else{
                    legendLabel.setStyle(legendCSS);
                    sh = true;
                }
                break;
        }
    }
}