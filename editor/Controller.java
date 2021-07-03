package editor;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import java.io.*;

public class Controller {
    private  View view;
    private HTMLDocument document;
    private File currentFile;

    public HTMLDocument getDocument() {
        return document;
    }

    public Controller(View view) {
        this.view = view;
    }

    public static void main(String args[]){
        View view = new View();
        Controller controller = new Controller(view);
        view.setController(controller);
        view.init();
        controller.init();
    }
    public void init(){
        createNewDocument();
    }
    public void createNewDocument(){
        view.selectHtmlTab();
        resetDocument();
        view.setTitle("HTML редактор");
        currentFile = null;

    }
    public  void openDocument(){
        view.selectHtmlTab();
        JFileChooser chooser = new JFileChooser();
        HTMLFileFilter htmlFileFilter = new HTMLFileFilter();
        chooser.setFileFilter(htmlFileFilter);
        int result =  chooser.showOpenDialog(view);
        if(result == JFileChooser.APPROVE_OPTION){
            currentFile = chooser.getSelectedFile();
            resetDocument();
            view.setTitle(currentFile.getName());
            view.resetUndo();
            try {
                FileReader read = new FileReader(currentFile);
                HTMLEditorKit htmlKit = new HTMLEditorKit();
                htmlKit.read(read, document, 0);
            }
            catch (IOException | BadLocationException e) {
                ExceptionHandler.log(e);
            }
        }
    }

    public  void saveDocument(){
        view.selectHtmlTab();
        if(currentFile != null){
            view.setTitle(currentFile.getName());
            try {
                FileWriter writer = new FileWriter(currentFile);
                HTMLEditorKit htmlKit = new HTMLEditorKit();
                try {
                    htmlKit.write(writer, document, 0, document.getLength());
                } catch (IOException | BadLocationException e) {
                    ExceptionHandler.log(e);
                }
            } catch (IOException e) {
                ExceptionHandler.log(e);
            }
        }
        else {
            saveDocumentAs();
        }
    }

    public void saveDocumentAs(){
        view.selectHtmlTab();
        JFileChooser chooser = new JFileChooser();
        HTMLFileFilter htmlFileFilter = new HTMLFileFilter();
        chooser.setFileFilter(htmlFileFilter);
        int result =  chooser.showSaveDialog(view);
        if(result == JFileChooser.APPROVE_OPTION){
            currentFile = chooser.getSelectedFile();
            view.setTitle(currentFile.getName());
            try {
                FileWriter writer = new FileWriter(currentFile);
                HTMLEditorKit htmlKit = new HTMLEditorKit();
                try {
                    htmlKit.write(writer, document, 0, document.getLength());
                } catch (IOException | BadLocationException e) {
                    ExceptionHandler.log(e);
                }
            } catch (IOException e) {
                ExceptionHandler.log(e);
            }
        }
    }

    public void exit(){
        System.exit(0);
    }
    public void resetDocument(){
        if (document != null) {
            document.removeUndoableEditListener(view.getUndoListener());
        }
        HTMLEditorKit htmlKit = new HTMLEditorKit();
        document = (HTMLDocument) htmlKit.createDefaultDocument();
        document.addUndoableEditListener(view.getUndoListener());
        view.update();
    }
    public void setPlainText(String text){
        resetDocument();
        StringReader readString =  new StringReader(text);
        HTMLEditorKit htmlKit = new HTMLEditorKit();
        try {
            htmlKit.read(readString, document, 0);
        } catch (IOException | BadLocationException e) {
            ExceptionHandler.log(e);
        }

    }
    public String getPlainText(){
        StringWriter writer =new StringWriter();
        HTMLEditorKit htmlKit = new HTMLEditorKit();
        try {
            htmlKit.write(writer, document, 0, document.getLength());
        } catch (IOException | BadLocationException e) {
            ExceptionHandler.log(e);
        }
        return writer.toString();
    }
}
