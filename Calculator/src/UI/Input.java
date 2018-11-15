package UI;

import Compiler.ANS.Answer;
import Compiler.LEX.Lexer;
import Compiler.PAR.Parser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.layout.AnchorPane;
import javax.swing.*;
import java.io.BufferedReader;
import java.io.StringReader;

public class Input {
    @FXML
    private AnchorPane Pane1;

    @FXML
    private Button calBtn;
    @FXML
    private Button clcBtn;
    @FXML
    private Button extBtn;


    @FXML
    private TextArea expText;
    @FXML
    private TextArea LText;
    @FXML
    private TextArea SText;
    @FXML
    private TextArea RText;


    @FXML
    private void extBtnClicked(ActionEvent event){
        System.exit(0);

    }

    @FXML
    private void clcBtnClicked(ActionEvent event){
        expText.setText("");
        LText.setText("");
        SText.setText("");
        RText.setText("");
    }

    @FXML
    private void calBtnClicked(ActionEvent event){
        LText.setText("");
        SText.setText("");
        RText.setText("");
        StringReader content = new StringReader(expText.getText());
        if (expText.getText().equals("")) {
            //JOptionPane.showMessageDialog(null, "Please input the expressions!");
            RText.setText("Error:\nPlease input the expressions!");
        } else {
            BufferedReader br = new BufferedReader(content);
            Answer.AStart(br);
            LText.setText(Lexer.LOutput);
            SText.setText(Parser.POutput);
            RText.setText(Answer.AOutput);
        }

    }
}
